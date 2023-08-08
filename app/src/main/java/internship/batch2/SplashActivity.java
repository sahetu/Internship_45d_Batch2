package internship.batch2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.splash_image);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        Glide.with(SplashActivity.this).asGif().load("https://www.icegif.com/wp-content/uploads/smiley-face-icegif-3.gif").into(imageView);

        /*AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(3000);
        animation.setRepeatCount(4);
        imageView.startAnimation(animation);*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sp.getString(ConstantSp.ID,"").equalsIgnoreCase("")) {
                    new CommonMethod(SplashActivity.this, MainActivity.class);
                }
                else{
                    new CommonMethod(SplashActivity.this, DashboardActivity.class);
                }
            }
        },2000);

    }
}