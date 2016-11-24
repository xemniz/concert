package ru.xmn.concert.splash;

import ru.xmn.concert.BasePresenter;
import ru.xmn.concert.BaseView;

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
