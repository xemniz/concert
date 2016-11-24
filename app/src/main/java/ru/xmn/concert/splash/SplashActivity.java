package ru.xmn.concert.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.xmn.concert.R;
import ru.xmn.concert.gigs.MainActivity;

/**
 * Created by xmn on 19.11.2016.
 */

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private SplashContract.Presenter mSplashPresenter;

    //region Lifecycle
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashPresenter = new SplashPresenter(
                SplashModel.getInstance(),
                this
        );
        mSplashPresenter.subscribe();
    }
    //endregion

    //region Contract
    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mSplashPresenter = presenter;
    }

    @Override
    public void showLoadingError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void showEventsActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    //endregion
}
