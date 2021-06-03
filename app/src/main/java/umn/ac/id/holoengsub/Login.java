package umn.ac.id.holoengsub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{
    EditText email,password;
    TextView clickhere_up;
    Button signin;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(user);
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
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed: " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        user = mAuth.getCurrentUser();
        /*-Check if user is already logged in or not-*/
        if (user != null) {
            /*-If user's email is verified then access login -*/
            if(user.isEmailVerified()){
                Toast.makeText(Login.this, "Login Success.",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this,Player.class));
            }
            else {
                Toast.makeText(Login.this, "Your Email is not verified.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
}





