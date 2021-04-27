package umn.ac.id.holoengsub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText fullname, username, password, confirm_password;
    TextView clickhere_in;
    Button signup;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        DB = new DBHelper(this);


        clickhere_in = this.findViewById(R.id.clickhere_in);
        clickhere_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        //signup, belum ada fungsinya, baru intent
        signup = this.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String name = fullname.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String confirm = confirm_password.getText().toString();
                if(name.equals("")||user.equals("")||pass.equals("")||confirm.equals(""))
                    Toast.makeText(Register.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(confirm)){
                        Boolean checkuser = DB.checkusername(user);
                        if(!checkuser){
                            Boolean insert = DB.insertData(name, user, pass);
                            if(insert){
                                Toast.makeText(Register.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent (getApplicationContext(),Profile.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Register.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Register.this, "User already exists! ", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Register.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });


    }

    public void openLogin() {
        Intent intent = new Intent (this, Login.class);
        startActivity(intent);
    }



}
