package internship.batch2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.messaging.FirebaseMessaging;

import internship.batch2.notifications.Config;

public class DashboardActivity extends AppCompatActivity {

    MeowBottomNavigation mBottomNavigation;

    int HOME_MENU = 1;
    int WISHLIST_MENU = 2;
    int PROFILE_MENU = 3;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String regId;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    //Update FCM ID CODE
                    updateFCM();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                }
            }
        };
        updateFCM();

        mBottomNavigation = findViewById(R.id.dashboard_bottom);

        mBottomNavigation.add(new MeowBottomNavigation.Model(HOME_MENU, R.drawable.ic_home));
        mBottomNavigation.add(new MeowBottomNavigation.Model(WISHLIST_MENU, R.drawable.heart));
        mBottomNavigation.add(new MeowBottomNavigation.Model(PROFILE_MENU, R.drawable.ic_profile));

        mBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                if (item.getId() == HOME_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative, new HomeFragment()).commit();
                    mBottomNavigation.show(HOME_MENU, true);
                }
                else if (item.getId() == WISHLIST_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative, new WishlistFragment()).commit();
                    mBottomNavigation.show(WISHLIST_MENU, true);
                }
                else if (item.getId() == PROFILE_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative, new ProfileFragment()).commit();
                    mBottomNavigation.show(PROFILE_MENU, true);
                } else {

                }
            }
        });

        mBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        mBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.dashboard_relative, new HomeFragment()).commit();
        mBottomNavigation.show(HOME_MENU, true);

    }

    private void updateFCM() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.toString();
            regId = newToken;
            Log.e("newToken_FCM", newToken);
            sp.edit().putString(ConstantSp.FCM_ID, newToken).commit();
        });

    }

}