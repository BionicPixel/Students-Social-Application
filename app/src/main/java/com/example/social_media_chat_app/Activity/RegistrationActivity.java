package com.example.social_media_chat_app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.social_media_chat_app.ModelClass.Users;
import com.example.social_media_chat_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView txt_signin, btn_SignUp;
    CircleImageView profile_image;
    EditText reg_name, reg_email, reg_password, reg_cPassword;
    Spinner reg_branch, reg_year;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        final Spinner b_spinner = findViewById(R.id.reg_branch);
        final Spinner y_spinner = findViewById(R.id.reg_year);

        b_spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        y_spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        List<String> b = new ArrayList<String>();
        b.add("AIML");
        b.add("COMPS");
        b.add("IT");
        b.add("MECH");

        ArrayAdapter<String> b_dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, b);
        b_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b_spinner.setAdapter(b_dataAdapter);

        List<String>y = new ArrayList<String>();
        y.add("FE");
        y.add("SE");
        y.add("TE");
        y.add("BE");

        ArrayAdapter<String> y_dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, y);
        y_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        y_spinner.setAdapter(y_dataAdapter);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        txt_signin=findViewById(R.id.txt_signin);
        profile_image=findViewById(R.id.profile_image);
        reg_email=findViewById(R.id.reg_email);
        reg_name=findViewById(R.id.reg_name);
        reg_branch=findViewById(R.id.reg_branch);
        reg_year=findViewById(R.id.reg_year);
        reg_password=findViewById(R.id.reg_pass);
        reg_cPassword=findViewById(R.id.reg_cPass);
        btn_SignUp=findViewById(R.id.btn_SignUp);

       // getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primary_purple));
        }

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name=reg_name.getText().toString();
                String branch=String.valueOf(b_spinner.getSelectedItem());
                String year=String.valueOf(y_spinner.getSelectedItem());
                String email=reg_email.getText().toString();
                String password=reg_password.getText().toString();
                String cPassword=reg_cPassword.getText().toString();
                String status="Hey there, I'm using this Application";

                Intent b_intent= new Intent(RegistrationActivity.this,Branches.class);
                b_intent.putExtra("data",String.valueOf(b_spinner.getSelectedItem()));
                startActivity(b_intent);
                Intent y_intent= new Intent(RegistrationActivity.this,Branches.class);
                y_intent.putExtra("data",String.valueOf(y_spinner.getSelectedItem()));
                startActivity(y_intent);

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cPassword) || TextUtils.isEmpty(branch) || TextUtils.isEmpty(branch)){
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                }else if (!email.matches(emailPattern)){
                    progressDialog.dismiss();
                    reg_email.setError("Please Enter Valid Email");
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();

                }else if (!password.equals(cPassword)){
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
                }else if(password.length()<6){
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Enter Atleast 6 digits Password", Toast.LENGTH_SHORT).show();
                }else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                DatabaseReference reference= database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());
                                if(imageUri!=null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI=uri.toString();
                                                        Users users=new Users(auth.getUid(), name, email, imageURI, status, branch, year);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    progressDialog.dismiss();
                                                                   startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                                                }else{
                                                                    Toast.makeText(RegistrationActivity.this, "Error in creating a new user", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });

                                                    }
                                                });
                                            }

                                        }
                                    });
                                }else{
                                    String status="Hey there, I'm using this Application";
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/social-media-chat-app-b02f3.appspot.com/o/profile_image.png?alt=media&token=cb783691-031f-458d-930c-e13d9522477e";
                                    Users users=new Users(auth.getUid(), name, email, imageURI, status, branch, year);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                            }else{
                                                Toast.makeText(RegistrationActivity.this, "Error in creating a new user", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }

                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }



            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10){
            if(data!=null){
                imageUri=data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
