package ru.xmn.concert.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.List;

import br.com.customsearchable.model.CustomSearchableInfo;
import io.realm.Realm;
import ru.xmn.concert.R;
import ru.xmn.concert.model.api.RealmApi;
import ru.xmn.concert.model.api.RockGigApi;
import ru.xmn.concert.model.api.VkApiBridge;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.presenter.MainPresenter;
import ru.xmn.concert.view.common.MvpAppCompatActivity;
import ru.xmn.concert.view.fragments.FragmentVk;
import ru.xmn.concert.view.fragments.FragmentThree;
import ru.xmn.concert.view.fragments.FragmentTwo;
import ru.xmn.concert.view.fragments.FragmentVkSettings;
import rx.schedulers.Schedulers;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    @InjectPresenter
    MainPresenter presenter;
    private Drawer.Result drawerResult = null;
    private AlertDialog mErrorDialog;

    private FragmentVk fragmentVk = new FragmentVk();

    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS,
            VKScope.AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //vk
        if (!VKSdk.wakeUpSession(this)) VKSdk.login(this, sMyScope);
        else startVkFragment();

        //Все для поиска
        CustomSearchableInfo.setTransparencyColor(Color.parseColor("#ccE3F2FD"));

        // Инициализируем Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Инициализируем спиннер
        String[] category = getResources().getStringArray(R.array.category);
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.toolbar_spinner_item_dropdown);
        Spinner navigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
        navigationSpinner.setAdapter(spinnerAdapter);
        toolbar.addView(navigationSpinner, 0);

        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                        "you selected: " + category[position],
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Создание диалога для ошибки
        mErrorDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setOnCancelListener(dialog -> presenter.closeError())
                .create();


        // Инициализируем Navigation Drawer
        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withBadge("99").withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
                        new SectionDrawerItem().withName(R.string.drawer_item_settings),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_github).withBadge("12+").withIdentifier(1)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Скрываем клавиатуру при открытии Navigation Drawer
                        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    // Обработка клика
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                            selectItem(position);
                        }
                        if (drawerItem instanceof Badgeable) {
                            Badgeable badgeable = (Badgeable) drawerItem;
                            if (badgeable.getBadge() != null) {
                                // учтите, не делайте так, если ваш бейдж содержит символ "+"
                                try {
                                    int badge = Integer.valueOf(badgeable.getBadge());
                                    if (badge > 0) {
                                        drawerResult.updateBadge(String.valueOf(badge - 1), position);
                                    }
                                } catch (Exception e) {
                                    Log.d("test", "Не нажимайте на бейдж, содержащий плюс! :)");
                                }
                            }
                        }
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    // Обработка длинного клика, например, только для SecondaryDrawerItem
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof SecondaryDrawerItem) {
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(((SecondaryDrawerItem) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .build();
        test();
    }

    private void selectItem(int position) {
        // Update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new FragmentVk();
                break;
            case 2:
                fragment = new FragmentTwo();
                break;
            case 3:
                fragment = new FragmentThree();
                break;
            default:
                break;
        }

// Insert the fragment by replacing any existing fragment          ​
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.content_frame, fragment, String.valueOf(fragment.getId())).commit();
            System.out.println(String.valueOf(fragment.getId()));
            // Highlight the selected item, update the title, and close the drawer        
        } else {
            Log.e(this.getClass().getName(), "Error. Fragment is not created");
        }
    }

    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    //работа с меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    // Заглушка, работа с меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_search) {
            // Calls Custom Searchable Activity
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showData(List<Band> list) {
    }

    @Override
    public void showError(String message) {
        mErrorDialog.setMessage(message);
        mErrorDialog.show();
    }

    @Override
    public void hideError() {
        mErrorDialog.hide();
    }

    @Override
    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                presenter.setFragment(fragmentVk);
                // User passed Authorization
            }

            @Override
            public void onError(VKError error) {
                showError(error.errorMessage);
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startVkFragment() {
        //Start fragment
        presenter.setFragment(fragmentVk);
    }

    private void test(){
        RealmApi realmApi = new RealmApi();
        VkApiBridge vkApiBridge = new VkApiBridge();
        RockGigApi rockGigApi = new RockGigApi();
        realmApi.soutGigs();

        Realm realmclean = Realm.getDefaultInstance();
//        realmclean.beginTransaction();
//        realmclean.deleteAll();
//        realmclean.commitTransaction();



//        realmApi.GigsToRealm(rockGigApi.getEventsRockGig().subscribeOn(Schedulers.io()).toBlocking().single());
//        realmApi.setImages();

//        vkApiBridge.bandList().subscribe(strings -> {
//            RealmQuery<EventRealm> query = RealmApi.myRealm.where(EventRealm.class);
////            query.equalTo("bandRockGigs.name", strings.get(0));
//            query.equalTo("bandRockGigs.name", strings.get(0), Case.INSENSITIVE);
//            for (String s :
//                    strings) {
//                query.or().equalTo("bandRockGigs.name", s.trim().toLowerCase(), Case.INSENSITIVE);
//            }
//            RealmResults<EventRealm> eventRealms = query.findAll();
//            System.out.println("SIZEEEEEEEE " + eventRealms.size());
//            eventRealms.asObservable().flatMap(eventRealms1 -> Observable.from(eventRealms1))
//                    .subscribe(eventRealm -> System.out.println(eventRealm.toString()));
//        });
}}