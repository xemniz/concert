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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.transition.Slide;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.graphics.Palette;
import com.squareup.picasso.Callback;

import com.squareup.picasso.Picasso;

import br.com.customsearchable.contract.CustomSearchableConstants;
import br.com.customsearchable.model.CustomSearchableInfo;
import br.com.customsearchable.model.ResultItem;
import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import ru.xmn.concert.R;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.presenter.BandPresenter;

public class BandActivity extends AppCompatActivity implements BandView {
    private static final String EXTRA_IMAGE = "com.antonioleiva.materializeyourapp.extraImage";
    private static final String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private BandPresenter presenter = new BandPresenter(this);

    private ImageView image;
    private TextView descriptionTxt;
    private CardView descrCardView;


    //    public static void navigate(AppCompatActivity activity, View transitionImage, ViewModel viewModel) {
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
        descrCardView = (CardView) findViewById(R.id.cardView);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String itemTitle = handleIntent(intent);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        System.out.println(itemTitle);
        presenter.getBandInfo(itemTitle);

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
    public void showData(Band bandDTO) {
        descriptionTxt.setText(Html.fromHtml(bandDTO.getWiki()));
        descriptionTxt.setMovementMethod(LinkMovementMethod.getInstance());

        Picasso
                .with(this)
                .load(bandDTO.getImageUrl())
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
}
