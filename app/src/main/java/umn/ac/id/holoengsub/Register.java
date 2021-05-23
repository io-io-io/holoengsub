package umn.ac.id.holoengsub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText fullname, email, password, confirm_password, phonenumber;
    private Button signup;
    private TextView clickhere_in;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(this);
        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        clickhere_in = (TextView) findViewById(R.id.clickhere_in);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clickhere_in:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.signup:
                registeruser();
                break;
        }
    }

    private void registeruser() {
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String conpass = confirm_password.getText().toString().trim();
        String name = fullname.getText().toString().trim();
        String phone = phonenumber.getText().toString().trim();

        if(name.isEmpty()){
            fullname.setError("Please fill your name");
            fullname.requestFocus();
            return;
        }

        if(mail.isEmpty()){
            email.setError("Please fill your email");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Please use a valid email");
            email.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            phonenumber.setError("Please fill your phone number");
            phonenumber.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            password.setError("Please fill your password");
            password.requestFocus();
            return;
        }

        if(!conpass.equals(pass)) {
            confirm_password.setError("Password does not match");
            confirm_password.requestFocus();
        }

        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, mail, phone);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Register.this, "Sign up successful! ", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(Register.this, "Sign up failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(Register.this, "Sign up failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });


    }
}