package internship.batch2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginOtpActivity extends AppCompatActivity {

    EditText otpEdit;
    Button submit;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        otpEdit = findViewById(R.id.login_otp_edit);
        submit = findViewById(R.id.login_otp_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otpEdit.getText().toString().trim().equals("")){
                    otpEdit.setError("OTP Required");
                }
                else if(otpEdit.getText().toString().trim().length()<6){
                    otpEdit.setError("Valid OTP Required");
                }
                else{
                    if(sp.getString(ConstantSp.OTP,"").equals(otpEdit.getText().toString().trim())){
                        new CommonMethod(LoginOtpActivity.this,"Login Successfully");
                        sp.edit().putBoolean(ConstantSp.IS_OTP_VERIFY,true).commit();
                        new CommonMethod(LoginOtpActivity.this,DashboardActivity.class);
                    }
                    else{
                        new CommonMethod(LoginOtpActivity.this,"Invalid OTP");
                    }
                }
            }
        });

    }
}