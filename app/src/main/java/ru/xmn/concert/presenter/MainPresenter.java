package ru.xmn.concert.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.xmn.concert.view.MainView;

/**
 * Created by xmn on 11.06.2016.
 */
@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    public void closeError() {
        getViewState().hideError();
    }
}
