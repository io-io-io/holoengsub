package umn.ac.id.holoengsub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{
    EditText email,password;
    TextView clickhere_up;
    Button signin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);
        signin = findViewById(R.id.signin);
        clickhere_up = findViewById(R.id.clickhere_up);
        signin.setOnClickListener(this);
        clickhere_up.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.clickhere_up){
            startActivity(new Intent(Login.this,Register.class));
        }
        else if(v.getId() == R.id.signin){
            loginUser(email.getText().toString(),password.getText().toString());
        }


    }

    /*-successful login process-*/
    private void loginUser(String email, String password) {
        if(email.equals("")){
            Toast.makeText(Login.this, "Enter Email!!",
                    Toast.LENGTH_SHORT).show();
        }
        else if(password.equals("")){
            Toast.makeText(Login.this, "Enter Password!!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Authentication failed: " + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            updateUI();
                        }
                    });
        }
    }

    private void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();
        /*-------- Check if user is already logged in or not--------*/
        if (user != null) {
            /*------------ If user's email is verified then access login -----------*/
            if(user.isEmailVerified()){
                Toast.makeText(Login.this, "Login Success.",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this,Profile.class));
            }
            else {
                Toast.makeText(Login.this, "Your Email is not verified.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
}





    /**EditText email1, password1;
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


}**/