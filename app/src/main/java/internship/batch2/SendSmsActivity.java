package internship.batch2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class SendSmsActivity extends AppCompatActivity {

    Button send;
    EditText contact, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        send = findViewById(R.id.send_sms_button);
        contact = findViewById(R.id.send_sms_contact_no);
        message = findViewById(R.id.send_sms_message);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().length() < 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (message.getText().toString().trim().equals("")) {
                    message.setError("Message Required");
                } else {
                    String sOTP = getRandomNumberString();
                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> parts = smsManager.divideMessage(message.getText().toString() + " : " + sOTP);
                    smsManager.sendMultipartTextMessage(contact.getText().toString(), null, parts, null, null);
                    Toast.makeText(SendSmsActivity.this, "Sms Send Successfully", Toast.LENGTH_SHORT).show();
                    contact.setText("");
                    message.setText("");
                    contact.requestFocus();
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

}