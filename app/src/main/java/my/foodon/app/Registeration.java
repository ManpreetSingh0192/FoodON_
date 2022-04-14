package my.foodon.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;

public class Registeration extends AppCompatActivity {
    String[] Maharashtra = {"Mumbai", "Pune", "Aurangabad"};
    String[] Gujarat = {"Ahemdabad", "Rajkot", "Surat"};


    String[] Mumbai = {"Churchgate", "Marine Lines", "Charni Road", "Grant Road", "Mumbai Central", "Mahalakshmi", "Lower Parel", "Prabhadevi",
            "Dadar", "Matunga", "Mahim", "Bandra", "Khar", "Santacruz", "Vile Parle", "Andheri", "Jogeshwari", "Ram Mandir",
            "Goregaon", "Malad", "Kandivai", "Borivali", "Dahisar", "MiraRoad", "Bhayander", "Naigaon", "Vasai Road", "Nalla Sopara", "Virar"};


    String[] Pune = {"Hinjewadi", "Wagholi", " Ambegaon", "Undri", "Katraj"};
    String[] Aurangabad = {"Aarif Colony", "Baiji Pura", "Balaji Nagar", "Angoori Bagh"};

    TextInputLayout Fname, Lname, Email, Pass, cfpass, mobileno, localaddress, area, postcode;
    Spinner statespin, Cityspin, Suburban;
    Button signup, Emaill, Phone;
    CountryCodePicker Cpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname;
    String lname;
    String emailid;
    String password;
    String confirmpassword;
    String mobile;
    TextInputLayout Localaddress;
    String Area;
    String Postcode;
    String role = "Customer";
    String statee;
    String cityy;
    String suburban;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        Fname = (TextInputLayout) findViewById(R.id.Fname);
        Lname = (TextInputLayout) findViewById(R.id.Lname);
        Email = (TextInputLayout) findViewById(R.id.Emailid);
        Pass = (TextInputLayout) findViewById(R.id.Password);
        cfpass = (TextInputLayout) findViewById(R.id.Confirmpass);
        mobileno = (TextInputLayout) findViewById(R.id.Mobileno);
        Localaddress = (TextInputLayout) findViewById(R.id.Localaddress);
        area = (TextInputLayout) findViewById(R.id.Area);
        statespin = (Spinner) findViewById(R.id.Statee);
        Cityspin = (Spinner) findViewById(R.id.Citys);
        Suburban = (Spinner) findViewById(R.id.Suburban);

        signup = (Button) findViewById(R.id.Signup);
        Emaill = (Button) findViewById(R.id.emaill);
        Phone = (Button) findViewById(R.id.phone);
        Cpp = (CountryCodePicker) findViewById(R.id.CountryCode);


        statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                statee = value.toString().trim();
                if (statee.equals("Maharashtra")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Maharashtra) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Registeration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }
                if (statee.equals("Gujarat")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Gujarat) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Registeration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                cityy = value.toString().trim();
                if (cityy.equals("Mumbai")) {
                    ArrayList<String> listt = new ArrayList<>();
                    for (String text : Mumbai) {
                        listt.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Registeration.this, android.R.layout.simple_spinner_item, listt);
                    Suburban.setAdapter(arrayAdapter);
                }

                if (cityy.equals("Pune")) {
                    ArrayList<String> listt = new ArrayList<>();
                    for (String text : Pune) {
                        listt.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Registeration.this, android.R.layout.simple_spinner_item, listt);
                    Suburban.setAdapter(arrayAdapter);
                }

                if (cityy.equals("Aurangabad")) {
                    ArrayList<String> listt = new ArrayList<>();
                    for (String text : Aurangabad) {
                        listt.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Registeration.this, android.R.layout.simple_spinner_item, listt);
                    Suburban.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        databaseReference = firebaseDatabase.getInstance().getReference("Customer");
        FAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                emailid = Email.getEditText().getText().toString().trim();
                mobile = mobileno.getEditText().getText().toString().trim();
                password = Pass.getEditText().getText().toString().trim();
                confirmpassword = cfpass.getEditText().getText().toString().trim();
                Area = area.getEditText().getText().toString().trim();


                if (isValid()) {

                    final ProgressDialog mDialog = new ProgressDialog(Registeration.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registering please wait...");
                    mDialog.show();

                    FAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                                final HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("Role", role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        HashMap<String, String> hashMappp = new HashMap<>();
                                        hashMappp.put("Area", Area);
                                        hashMappp.put("City", cityy);
                                        hashMappp.put("ConfirmPassword", confirmpassword);
                                        hashMappp.put("EmailID", emailid);
                                        hashMappp.put("Fname", fname);
                                        hashMappp.put("Local Address", String.valueOf(localaddress));
                                        hashMappp.put("Lname", lname);
                                        hashMappp.put("Mobile", mobile);
                                        hashMappp.put("Password", password);
                                        hashMappp.put("State", statee);
                                        hashMappp.put("Suburban", suburban);
                                        firebaseDatabase.getInstance().getReference("Chef")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMappp).addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mDialog.dismiss();

                                                FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Registeration.this);
                                                            builder.setMessage("Registered Successfully,Please Verify your Email");
                                                            builder.setCancelable(false);
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                    dialog.dismiss();

                                                                    String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                                                    Intent b = new Intent(Registeration.this, VerifyPhone.class);
                                                                    b.putExtra("phonenumber", phonenumber);
                                                                    startActivity(b);

                                                                }
                                                            });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();

                                                        } else {
                                                            mDialog.dismiss();
                                                            ReusableCodeForAll.ShowAlert(Registeration.this, "Error", task.getException().getMessage());

                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });


                            } else {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(Registeration.this, "Error", task.getException().getMessage());
                            }

                        }
                    });

                }
            }

            private boolean isValid() {
                return false;
            }
        });
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    }
}