package umn.ac.id.holoengsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView clickhere_in;
        Button signup;

        clickhere_in = (TextView) this.findViewById(R.id.clickhere_in);
        clickhere_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        //signup, belum ada fungsinya, baru intent
        signup = (Button) this.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });


    }

    public void openLogin() {
        Intent intent = new Intent (this, Login.class);
        startActivity(intent);
    }

    public void openProfile(){
        Intent intent = new Intent (this, Profile.class);
        startActivity(intent);
    }


}
