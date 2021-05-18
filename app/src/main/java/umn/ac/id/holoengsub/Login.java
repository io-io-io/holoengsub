package umn.ac.id.holoengsub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email1, password1;
    Button signin;
    TextView clickhere_up;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email1 = findViewById(R.id.email1);
        password1 = findViewById(R.id.password1);
        fAuth = FirebaseAuth.getInstance();
        signin = findViewById(R.id.signin);
        clickhere_up = findViewById(R.id.clickhere_up);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email1.getText().toString().trim();
                String pass = password1.getText().toString().trim();

                //check ada yang kosong atau ga
                if(TextUtils.isEmpty(mail)){
                    email1.setError("Please fill your email");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password1.setError("Please fill your password");
                    return;
                }

                fAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent (getApplicationContext(), Profile.class));
                        }else{
                            Toast.makeText(Login.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        clickhere_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });



    }


}