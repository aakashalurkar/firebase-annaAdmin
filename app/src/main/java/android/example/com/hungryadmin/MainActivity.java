package android.example.com.hungryadmin;

import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    //MainActivity

    private TextView welcome1;
    private TextView welcome2;
    private TextView line;
    private TextView eilisys;
    private LinearLayout ll;

    String email;
    String password;

    EditText emailInput;
    EditText password_input;

    Button authenticate;
    Button login;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //RegisterAdmin

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;


    private Button register;

    EditText getName;
    EditText getNumber;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcome1 = (TextView) findViewById(R.id.welcome1);
        welcome2 = (TextView) findViewById(R.id.welcome2);
        line = (TextView) findViewById(R.id.line);
        eilisys = (TextView) findViewById(R.id.eilisys);
        emailInput = (EditText) findViewById(R.id.userEmailInputActivityMain);
        password_input = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
//        passwordInput = (EditText) findViewById(R.id.userPasswordInputActivityMain);
        authenticate = (Button) findViewById(R.id.authenticate);
        ll = (LinearLayout) findViewById(R.id.linearLayout);

        login = (Button) findViewById(R.id.login);

        //RegisterAdmin
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        register = (Button) findViewById(R.id.userSubmit);
        getName = (EditText) findViewById(R.id.name);
        getNumber = (EditText) findViewById(R.id.mobile);

        final FirebaseUser user = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
//                    FirebaseUser user=mAuth.getCurrentUser();
//                    Toast.makeText(MainActivity.this, "USER HAS LOGGED IN", Toast.LENGTH_SHORT).show();
                }
            }
        };

        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        if(emailInput.getText().toString().trim().equals("")||password_input.getText().toString().trim().equals(""))
        {
                Toast.makeText(MainActivity.this, "Please enter the fields", Toast.LENGTH_SHORT).show();
                Intent intent=getIntent();
                finish();
                startActivity(intent);
}
    else {

             CreateAccount();


            authenticate.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSignIn();

            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            Intent onAuthSuccess = new Intent(MainActivity.this,AdminUpdatesOrder.class);
            startActivity(onAuthSuccess);

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void CreateAccount() {
        email = emailInput.getText().toString()+"@eilisys.com";
        final String e = email;
        password = password_input.getText().toString();

        if (emailInput.equals("")) {

            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();

        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String User_id = mAuth.getCurrentUser().getUid();
                                Toast.makeText(MainActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, "Verify the email sent to your inbox", Toast.LENGTH_SHORT).show();
                                emailverify();
                                mAuth.signOut();
                                register.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(getName.getText().toString().trim().equals("")||getNumber.getText().toString().trim().equals("")) {
                                            Toast.makeText(MainActivity.this, "Please enter the field to verify!!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            String name = getName.getText().toString().trim();
                                            String mobile = getNumber.getText().toString().trim();
                                            String userdetails = name + " " + mobile;
                                            String email = e;

                                            AdminStructure adminStructure = new AdminStructure(userdetails,e);

                                            mDatabase.child("ADMIN").child(User_id).setValue(adminStructure).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        Log.v("RegisterUSer", "if task is successful");
                                                        Toast.makeText(MainActivity.this, "Data Stored Successfully", Toast.LENGTH_LONG).show();
                                                        getName.setText("");
                                                        getNumber.setText("");

                                                        Intent intent = new Intent(MainActivity.this, AdminUpdatesOrder.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Log.v("AdminUpdateOrder", "if task is NOT successful");
                                                        Toast.makeText(MainActivity.this, "Data Not Stored", Toast.LENGTH_LONG).show();
                                                        getName.setText("");
                                                        getNumber.setText("");
                                                    }
                                                }
                                            });
                                        }
                                    }

                                });


                            }
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Account Creation Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void emailverify() {

        final FirebaseUser user = mAuth.getInstance().getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    // email sent
                    Toast.makeText(MainActivity.this, "Email sent to   " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    // after email is sent just logout the user and finish this activity
//                                mAuth.signOut();
//                                startActivity(new Intent(MainActivity.this, LoginClass.class));
//                                finish();
                } else {
                    // email not sent, so display message and restart the activity or do whatever you wish to do
                    //restart this activity
                    Toast.makeText(MainActivity.this, "Could not send email please retry", Toast.LENGTH_SHORT).show();
//                    overridePendingTransition(0, 0);
//                    finish();
//                    overridePendingTransition(0, 0);
//                    startActivity(getIntent());
                }
            }
        });


    }

    //    Needed only to sign in when already the account is created
    private void startSignIn() {

        email = emailInput.getText().toString()+"@eilisys.com";
        password = password_input.getText().toString();
        if (email == "" || password == "") {
            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sign in Problem", Toast.LENGTH_SHORT).show();

                    } else {
                        try {

                            if (user.isEmailVerified()) {

                                welcome1.setVisibility(View.GONE);
                                welcome2.setVisibility(View.GONE);
                                line.setVisibility(View.GONE);
                                emailInput.setVisibility(View.GONE);
                                eilisys.setVisibility(View.GONE);
                                password_input.setVisibility(View.GONE);
                                authenticate.setVisibility(View.GONE);
                                login.setVisibility(View.GONE);
                                ll.setVisibility(View.GONE);

                                getName.setVisibility(View.VISIBLE);
                                getNumber.setVisibility(View.VISIBLE);
                                register.setVisibility(View.VISIBLE);

                                Toast.makeText(MainActivity.this, "Success,Email verified", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(MainActivity.this, AdminUpdateOrder.class);
//                                startActivity(intent);

                            } else {
                                Toast.makeText(MainActivity.this, "Email is not verified", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(MainActivity.this, "Null point exception", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
