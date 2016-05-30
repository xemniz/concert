package ru.xmn.concert.view;

import android.app.Activity;
import android.app.Application;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
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
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.customsearchable.contract.CustomSearchableConstants;
import br.com.customsearchable.model.CustomSearchableInfo;
import br.com.customsearchable.model.ResultItem;
import butterknife.Bind;
import ru.xmn.concert.R;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.presenter.Presenter;
import ru.xmn.concert.view.adapters.EventsAdapter;
import ru.xmn.concert.view.fragments.FragmentOne;
import ru.xmn.concert.view.fragments.FragmentThree;
import ru.xmn.concert.view.fragments.FragmentTwo;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String VK_APP_ID = "c5ciaBldtbUggy09v9m1";
    private Drawer.Result drawerResult = null;
    private EventsAdapter adapter = new EventsAdapter();
    private Presenter presenter = new Presenter(this);
    private SearchView searchView;
    RecyclerView recyclerView;

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



        presenter = new Presenter(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //vk
        VKSdk.login(this, sMyScope);

        //Все для поиска
        CustomSearchableInfo.setTransparencyColor(Color.parseColor("#ccE3F2FD"));
        Intent intent = getIntent();
        handleIntent(intent);

        // Инициализируем Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        FragmentOne fragmentOne = new FragmentOne();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, fragmentOne).commit();
    }


    // Handles the intent that carries user's choice in the Search Interface
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Log.i("Main", "Received query: " + query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Bundle bundle = this.getIntent().getExtras();

            assert (bundle != null);

            if (bundle != null) {
                ResultItem receivedItem = bundle.getParcelable(CustomSearchableConstants.CLICKED_RESULT_ITEM);

                Log.i("RI.header", receivedItem.getHeader());
                Log.i("RI.subHeader", receivedItem.getSubHeader());
                Log.i("RI.leftIcon", receivedItem.getLeftIcon().toString());
                Log.i("RI.rightIcon", receivedItem.getRightIcon().toString());
            }
        }
    }


    private void selectItem(int position) {
        // Update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new FragmentOne();
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
//        RxSearchView.queryTextChangeEvents(searchView)
//                .debounce(1000, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<SearchViewQueryTextEvent>() {
//                    @Override
//                    public void call(final SearchViewQueryTextEvent s) {
//                        presenter.onstart(s.queryText().toString());
//                    }
//                });
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
    public void showData(List<EventGig> list) {
        FragmentOne fragmentOne = (FragmentOne) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        for(EventGig gig:list)
            fragmentOne.getAdapter().add(gig, 0);
//        fragmentOne.getAdapter().setGigList(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // User passed Authorization
                System.out.println("VKTEST123");
                presenter.bandList();
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);


        }
    }

}