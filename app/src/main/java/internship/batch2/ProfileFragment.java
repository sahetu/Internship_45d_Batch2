package internship.batch2;

import static android.content.Context.MODE_PRIVATE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    //TextView name;
    SharedPreferences sp;

    Button logout, updateProfile, editProfile;
    EditText name, email, contact, dob;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    RadioButton male,female;
    RadioGroup gender;

    Spinner city;

    ArrayList<String> arrayList;
    Calendar calendar;
    String sCity = "";
    String sGender;

    SQLiteDatabase db;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sp = getActivity().getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        /*name = findViewById(R.id.home_name);

        name.setText(
                sp.getString(ConstantSp.ID,"")+"\n"+
                        sp.getString(ConstantSp.NAME,"")+"\n"+
                        sp.getString(ConstantSp.EMAIL,"")+"\n"+
                        sp.getString(ConstantSp.CONTACT,"")+"\n"+
                        sp.getString(ConstantSp.PASSWORD,"")+"\n"+
                        sp.getString(ConstantSp.GENDER,"")+"\n"+
                        sp.getString(ConstantSp.DOB,"")+"\n"+
                        sp.getString(ConstantSp.CITY,""));*/
        db = getActivity().openOrCreateDatabase("Internship_Batch2",MODE_PRIVATE,null);
        String tabelQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INTEGER(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tabelQuery);

        logout = view.findViewById(R.id.home_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                new CommonMethod(getActivity(),MainActivity.class);
            }
        });

        name = view.findViewById(R.id.home_name);
        email = view.findViewById(R.id.home_email);
        contact = view.findViewById(R.id.home_contact);
        dob = view.findViewById(R.id.home_dob);

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                dob.setText(sdf.format(calendar.getTime()));

            }
        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        city = view.findViewById(R.id.home_city);

        arrayList = new ArrayList<>();

        arrayList.add("Ahmedabad");
        arrayList.add("Rajkot");
        arrayList.add("Surat");
        arrayList.add("Demo");
        arrayList.add("XYZ");
        arrayList.add("Gandhinagar");

        arrayList.remove(3);
        arrayList.set(3, "Vadodara");

        arrayList.add(0, "Jamnagar");
        arrayList.add(0, "Select City");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sCity = "";
                }
                else {
                    sCity = arrayList.get(i);
                    new CommonMethod(getActivity(), arrayList.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        male = view.findViewById(R.id.home_male);
        female = view.findViewById(R.id.home_female);
        gender = view.findViewById(R.id.home_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = view.findViewById(i); //i = R.id.home_male,R.id.home_female;
                sGender = radioButton.getText().toString();
                new CommonMethod(getActivity(), sGender);
            }
        });

        updateProfile = view.findViewById(R.id.home_update_profile);

        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Invalid EMail Id");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().trim().length() < 10) {
                    contact.setError("Valid Contact No. Required");
                } 
                else if(gender.getCheckedRadioButtonId() == -1){
                    new CommonMethod(getActivity(),"Please Select Gender");
                }
                else if(sCity.equals("")){
                    new CommonMethod(getActivity(),"Please Select City");
                }
                else if (dob.getText().toString().trim().equals("")) {
                    dob.setError("Please Select Date Of Birth");
                }
                else {
                    String selectQuery = "SELECT * FROM USERS WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        String updateQuery = "UPDATE USERS SET NAME='"+name.getText().toString()+"',EMAIL='"+email.getText().toString()+"',CONTACT='"+contact.getText().toString()+"',GENDER='"+sGender+"',CITY='"+sCity+"',DOB='"+dob.getText().toString()+"' WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"'";
                        db.execSQL(updateQuery);
                        new CommonMethod(getActivity(),"Profile Update Success");

                        sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                        sp.edit().putString(ConstantSp.CITY,sCity).commit();
                        sp.edit().putString(ConstantSp.DOB,dob.getText().toString()).commit();

                        setData(false);
                    }
                    else {
                        new CommonMethod(getActivity(),"Invalid User Id");
                    }
                }
            }
        });

        editProfile = view.findViewById(R.id.home_edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        setData(false);

        return view;
    }

    private void setData(boolean isEnable) {

        name.setEnabled(isEnable);
        email.setEnabled(isEnable);
        contact.setEnabled(isEnable);
        dob.setEnabled(isEnable);

        male.setEnabled(isEnable);
        female.setEnabled(isEnable);

        city.setEnabled(isEnable);

        if(isEnable){
            editProfile.setVisibility(View.GONE);
            updateProfile.setVisibility(View.VISIBLE);
        }
        else{
            editProfile.setVisibility(View.VISIBLE);
            updateProfile.setVisibility(View.GONE);
        }

        name.setText(sp.getString(ConstantSp.NAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));
        dob.setText(sp.getString(ConstantSp.DOB,""));

        sGender = sp.getString(ConstantSp.GENDER,"");
        if(sGender.equalsIgnoreCase("Male")) {
            male.setChecked(true);
        }
        else if(sGender.equalsIgnoreCase("Female")) {
            female.setChecked(true);
        }
        else{

        }

        //city.setSelection(2);
        sCity = sp.getString(ConstantSp.CITY,"");
        int iCityPosition = 0;
        for(int i=0;i<arrayList.size();i++){
            if(sCity.equalsIgnoreCase(arrayList.get(i))){
                iCityPosition = i;
                break;
            }
        }
        city.setSelection(iCityPosition);
    }

}