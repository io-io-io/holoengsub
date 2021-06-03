package umn.ac.id.holoengsub;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;



public class Register extends AppCompatActivity implements View.OnClickListener{
    EditText register_email,register_password,register_name;
    TextView clickhere_in;
    Button signup;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    ImageView user_profile;
    Uri image_uri;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_email = findViewById(R.id.email);
        register_password = findViewById(R.id.password);
        register_name = findViewById(R.id.fullname);
        user_profile = findViewById(R.id.add_pic);
        signup = findViewById(R.id.signup);
        clickhere_in = findViewById(R.id.clickhere_in);
        clickhere_in.setOnClickListener(this);
        signup.setOnClickListener(this);
        user_profile.setOnClickListener(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.clickhere_in){
            startActivity(new Intent(Register.this,Login.class));
        }
        else if(v.getId() == R.id.signup){
            signUpUser(register_email.getText().toString(),register_password.getText().toString());
        }
        else if(v.getId() == R.id.add_pic){
            SelectProfilePic();
        }

    }


    /*-------- Below Code is for selecting image from galary or camera -----------*/

    private void SelectProfilePic() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")){
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String [] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,1000);
                    }
                    else {
                        openCamera();
                    }
                }
                else if (options[item].equals("Choose from Gallery")){

                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }
            }
        });
        builder.show();

    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //Camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(takePictureIntent, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                //permisiion from pop up was denied.
                Toast.makeText(Register.this, "Permission Denied...", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    user_profile.setImageURI(image_uri);
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    user_profile.setImageURI(image_uri);
                    break;
            }
        }
    }



    /*- successful sign up process -*/

    private void signUpUser(String email, String password) {
        if(register_email.equals("")){
            Toast.makeText(Register.this, "Please enter your email!",
                    Toast.LENGTH_SHORT).show();
        }
        else if(register_password.equals("")){
            Toast.makeText(Register.this, "Please enter your password!!",
                    Toast.LENGTH_SHORT).show();
        }
        else if((register_password.getText().toString()).equals("")){
            Toast.makeText(Register.this, "Please enter your name!!",
                    Toast.LENGTH_SHORT).show();
        }
        else if(image_uri == null){
            Toast.makeText(Register.this, "Please upload a picture!!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                userProfile();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Sign up failed: " + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /*-saving image and user name in Firebase Database-*/
    private void userProfile() {
        user = mAuth.getCurrentUser();
        if(user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(register_name.getText().toString())
                    .setPhotoUri(image_uri).build();
            user.updateProfile(profileUpdates).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    verifyEmailRequest();
                }
            });
        }
    }

    /*- send verification email-*/
    private void verifyEmailRequest() {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"Email verification sent on\n"+register_email.getText().toString(),Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            startActivity(new Intent(Register.this,Player.class));
                        }
                        else {
                            Toast.makeText(Register.this,"Sign up Success But Failed to send verification email.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        user = mAuth.getCurrentUser();
        /*-Check if user is already logged in or not-*/
        if (user != null) {
                startActivity(new Intent(Register.this,Player.class));

            } else {
            Toast.makeText(Register.this, "Please register ",
                    Toast.LENGTH_SHORT).show();
        }

    }



}

