package ru.xmn.concert.view;


import android.app.SearchManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.transition.Slide;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.graphics.Palette;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.squareup.picasso.Callback;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import br.com.customsearchable.contract.CustomSearchableConstants;
import br.com.customsearchable.model.ResultItem;
import ru.xmn.concert.R;
import ru.xmn.concert.model.data.BandLastfm;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.presenter.BandPresenter;
import ru.xmn.concert.view.adapters.EventsAdapter;
import ru.xmn.concert.view.common.MvpAppCompatActivity;

public class BandActivity extends MvpAppCompatActivity implements BandView {
    @InjectPresenter
    BandPresenter presenter;
    private static final String EXTRA_IMAGE = "com.antonioleiva.materializeyourapp.extraImage";
    private static final String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";
    private CollapsingToolbarLayout collapsingToolbarLayout;
//    private BandPresenter presenter = new BandPresenter(this);

    private ImageView image;
    private TextView descriptionTxt;
    private RecyclerView eventsRecView;

    private EventsAdapter adapter = new EventsAdapter();
//        public static void navigate(AppCompatActivity activity, View transitionImage, ViewModel viewModel) {
//        Intent intent = new Intent(activity, BandActivity.class);
//        intent.putExtra(EXTRA_IMAGE, viewModel.getImage());
//        intent.putExtra(EXTRA_TITLE, viewModel.getText());
//
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_IMAGE);
//        ActivityCompat.startActivity(activity, intent, options.toBundle());
//    }
    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("image "+image);

//        setTheme(R.style.AppTheme);
        Intent intent = getIntent();

        initActivityTransitions();

        setContentView(R.layout.activity_band);
        image = (ImageView) findViewById(R.id.image);
        descriptionTxt = (TextView) findViewById(R.id.expandable_text);
        eventsRecView = (RecyclerView)  findViewById(R.id.eventsRecyclerView);
        eventsRecView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecView.setAdapter(adapter);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get band name from intent
        String itemTitle = handleIntent(intent);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        //get band info from API
        try {
            presenter.getBandInfo(itemTitle);
        } catch (IOException e) {
            e.printStackTrace();
        }
        presenter.getBandEvents(itemTitle);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(itemTitle);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    // Handles the intent that carries user's choice in the Search Interface
    private String handleIntent(Intent intent) {
        String bandname = "";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            bandname = query;
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
                bandname =
                 receivedItem.getHeader();
            }
        }
        return bandname;
    }


    @Override public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        int primary = getResources().getColor(R.color.primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.accent));

        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }

    @Override
    public void showData(BandLastfm bandLastfmDTO) {
        descriptionTxt.setText(Html.fromHtml(bandLastfmDTO.getWiki()));
        descriptionTxt.setMovementMethod(LinkMovementMethod.getInstance());

        Picasso
                .with(this)
                .load(bandLastfmDTO.getImageUrl())
                .into(image, new Callback() {
                    @Override public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                applyPalette(palette);
                            }
                        });
                    }

                    @Override public void onError() {

                    }
                });
    }

    @Override
    public void showEvents(List<EventGig> data) {
        adapter.setGigList(data);
    }
}
