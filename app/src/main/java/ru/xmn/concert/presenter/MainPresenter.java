package ru.xmn.concert.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vk.sdk.VKScope;

import ru.xmn.concert.R;
import ru.xmn.concert.view.MainView;

/**
 * Created by xmn on 11.06.2016.
 */
@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS,
            VKScope.AUDIO
    };

    public void closeError() {
        getViewState().hideError();
    }

    public void setFragment ()
    {
        getViewState().setFragment();
    }
}
