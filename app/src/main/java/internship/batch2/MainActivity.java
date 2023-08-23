package internship.batch2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button login,signup;
    EditText email;
    //password

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("Internship_Batch2",MODE_PRIVATE,null);
        String tabelQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INTEGER(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tabelQuery);

        login = findViewById(R.id.main_login);
        email = findViewById(R.id.main_email);
        //password = findViewById(R.id.main_password);

        signup = findViewById(R.id.main_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);*/
                new CommonMethod(MainActivity.this,SignupActivity.class);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().equals("")) {
                    email.setError("Contact No. Required");
                    /*if(email.getText().toString().trim().matches(emailPattern)){
                        System.out.println("Email Pattern Match");
                    }
                    else{
                        System.out.println("Email Pattern Not Match");
                    }*/
                }
                else if(email.getText().toString().length()<10){
                    email.setError("Valid Contact No. Required");
                }
                /*else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Invalid EMail Id");
                }else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                }
                else if (password.getText().toString().trim().length()<6) {
                    password.setError("Min. 6 Char Password Required");
                }*/
                else {

                    //String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' AND PASSWORD='"+password.getText().toString()+"'";
                    String selectQuery = "SELECT * FROM USERS WHERE CONTACT='"+email.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0) {
                        String sOTP = getRandomNumberString();
                        SmsManager smsManager = SmsManager.getDefault();
                        ArrayList<String> parts = smsManager.divideMessage("Your OTP Code IS : " + sOTP);
                        smsManager.sendMultipartTextMessage(email.getText().toString(), null, parts, null, null);

                        while (cursor.moveToNext()){
                            String sUserId = cursor.getString(0);
                            String sName = cursor.getString(1);
                            String sEmail = cursor.getString(2);
                            String sContact = cursor.getString(3);
                            String sPassword = cursor.getString(4);
                            String sGender = cursor.getString(5);
                            String sCity = cursor.getString(6);
                            String sDob = cursor.getString(7);

                            sp.edit().putString(ConstantSp.ID,sUserId).commit();
                            sp.edit().putString(ConstantSp.NAME,sName).commit();
                            sp.edit().putString(ConstantSp.EMAIL,sEmail).commit();
                            sp.edit().putString(ConstantSp.CONTACT,sContact).commit();
                            sp.edit().putString(ConstantSp.PASSWORD,sPassword).commit();
                            sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                            sp.edit().putString(ConstantSp.CITY,sCity).commit();
                            sp.edit().putString(ConstantSp.DOB,sDob).commit();
                            sp.edit().putBoolean(ConstantSp.IS_OTP_VERIFY,false).commit();
                            sp.edit().putString(ConstantSp.OTP,sOTP).commit();

                            Log.d("RESPONSE_USER_DETAIL",sUserId+"\n"+sName+"\n"+sEmail+"\n"+sContact+"\n"+sPassword+"\n"+sGender+"\n"+sCity+"\n"+sDob);
                        }

                        //System.out.println("Login Successfully\nEmail:" + email.getText().toString() + "\nPassword:" + password.getText().toString());
                        //Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        new CommonMethod(MainActivity.this, "Sms Send Successfully");
                        //Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();
                        new CommonMethod(view, "Sms Send Successfully");

                        /*Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(intent);*/
                        new CommonMethod(MainActivity.this, LoginOtpActivity.class);
                    }
                    else{
                        new CommonMethod(MainActivity.this, "Invalid Contact no.");
                    }
                }
            }
        });
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
    }
}