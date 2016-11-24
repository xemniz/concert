package ru.xmn.concert;

import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;
//import ru.xmn.concert.mvp.view.MainActivity;

public class Application extends android.app.Application {

    private static Application instance;

    public static Application get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        vkInit();
        realmInit();
    }

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Toast.makeText(Application.this, "AccessToken invalidated", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Application.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        }
    };

    private void realmInit() {
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder(Application.get().getApplicationContext())
                        .deleteRealmIfMigrationNeeded()
                        .name("myOtherRealm.realm")
                        .build()
        );
    }

    private void vkInit() {
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }
}