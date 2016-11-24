package ru.xmn.concert.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import ru.xmn.concert.R;


public abstract class SingleFragmentActivity extends AppCompatActivity {
    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS,
            VKScope.AUDIO
    };
    private Fragment mFragment;
    private FragmentManager mManager;

    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mManager = getSupportFragmentManager();
        mFragment = mManager.findFragmentById(R.id.fragment_container);
        Log.d(this.getClass().getSimpleName(), "BREAKPOINT");
        if (!VKSdk.wakeUpSession(this)) VKSdk.login(this, sMyScope);
        else
        if (mFragment == null) {
            mFragment = createFragment();
            mFragment.setArguments(getIntent().getExtras());
            mManager.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();}

        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                if (mFragment == null) {
                    mFragment = createFragment();
                    mFragment.setArguments(getIntent().getExtras());
                    mManager.beginTransaction()
                            .add(R.id.fragment_container, mFragment)
                            .commit();}
            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
