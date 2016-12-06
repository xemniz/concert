package ru.xmn.concert.gigs;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import ru.xmn.concert.R;
import ru.xmn.concert.databinding.ActivityMainBinding;
import ru.xmn.concert.mvp.view.ActivityUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private GigsContract.Presenter mGigsPresenter;
    private ActivityMainBinding mBinding;
    boolean mVkIsApply = false;

    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS,
            VKScope.AUDIO
    };
    private GigsFragment mGigsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initToolbar();

        mGigsFragment = (GigsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (mGigsFragment == null) {
            // Create the fragment
            mGigsFragment = GigsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mGigsFragment, R.id.fragment_container);
        }

        // Create the presenter
        mGigsPresenter = new GigsPresenter(
                GigsModel.getInstance(),
                mGigsFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");
        switch (item.getItemId()) {
            case R.id.vk_item:
                mVkIsApply = !mVkIsApply;
                mGigsPresenter.changeFilter(GigsFragment.FILTER_VK, mVkIsApply);
                if (VKSdk.wakeUpSession(this))
                    mGigsPresenter.loadGigs();
                else
                    VKSdk.login(this, sMyScope);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                mGigsPresenter.loadGigs();
                mGigsFragment.setLoadingIndicator(true);
            }

            @Override
            public void onError(VKError error) {
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
