package umn.ac.id.holoengsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button signin;
    TextView clickhere_up;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        DB = new DBHelper(this);


        clickhere_up = this.findViewById(R.id.clickhere_up);
        clickhere_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        //baru fungsi button, proses signin belum ada
        signin = this.findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("") || pass.equals(""))
                    Toast.makeText(Login.this, "Please fill all of the fields", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if (checkuserpass) {
                        Toast.makeText(Login.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });


    }

    public void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
