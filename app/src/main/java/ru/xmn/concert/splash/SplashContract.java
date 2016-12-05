package ru.xmn.concert.splash;

import ru.xmn.concert.mvp.BasePresenter;
import ru.xmn.concert.mvp.view.BaseView;

/**
 * Created by xmn on 19.11.2016.
 */

public interface SplashContract {
    public interface View extends BaseView<Presenter>{
        void showLoadingError(Throwable throwable);
        void showEventsActivity();
    }

    public interface Presenter extends BasePresenter {

    }
}
