package umn.ac.id.holoengsub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    Button signout,change_password,update_password;;
    EditText new_password;
    TextView welcome;
    ImageView user_profile;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private CountDownTimer timer;
    private boolean session_out = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        welcome = findViewById(R.id.welcome);
        signout = findViewById(R.id.signout);
        change_password = (Button)findViewById(R.id.change_password);
        update_password = (Button)findViewById(R.id.update_password);
        new_password = (EditText) findViewById(R.id.new_password);
        user_profile = findViewById(R.id.user_profile);

        signout.setOnClickListener(this);
        change_password.setOnClickListener(this);
        update_password.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user!=null){
            String name = user.getDisplayName();
            Uri image_uri = user.getPhotoUrl();
            welcome.setText("Welcome, "+name);
            user_profile.setImageURI(image_uri);
        }


        /*-auto logout on user's inactivity-*/
        timer = new CountDownTimer(300000,1000) { //set session timeout interval here
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                session_out = true;
            }
        };

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signout){
            mAuth.signOut();
            Toast.makeText(Profile.this,"Logged out!",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Profile.this,MainActivity.class));
        }
        else if(v.getId() == R.id.change_password){
            new_password.setVisibility(View.VISIBLE);
            update_password.setVisibility(View.VISIBLE);

        }
        else if(v.getId() == R.id.update_password){
            ChangePasswordRequest(new_password.getText().toString());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //Start timer on inactivity
        Log.i("Main","Timer started!");
        timer.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //if user do some activity then cancel timer
        if(timer!=null){
            Log.i("Main","Timer stopped!");
            timer.cancel();
        }
        //if user comes back after session time out then redirect to login page
        if(session_out){
            mAuth.signOut();
            Toast.makeText(Profile.this,"Session Timed Out!!.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Profile.this,MainActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //on closing application signout user
        mAuth.signOut();
    }
    private void ChangePasswordRequest(String new_password) {
        if(new_password.equals("")){
            Toast.makeText(Profile.this, "Enter Password!! ", Toast.LENGTH_LONG).show();
        }
        else {
            FirebaseUser user = mAuth.getCurrentUser();
            user.updatePassword(new_password)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Profile.this, "User Password Updated.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Profile.this, "For Securtiy resons you have to re-login first.\nThen try to update password.", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }

    }



}
