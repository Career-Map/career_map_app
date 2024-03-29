package com.example.career_map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.career_map.Utils.Constant;
import com.example.career_map.databinding.ActivitySignupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private  ActivitySignupBinding activitySignUpBinding;
    private int flag = 0;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseDB;


    private int temp_flag = 0;
    private String btn_clicked = "";

    private DocumentSnapshot documentSnapshot;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;

    //    Facebook
  //  private CallbackManager mCallbackManager;
    private static String TAG = "fbdebug";

    private Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Disable nightmode
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        activitySignUpBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

//       Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();

        constant = new Constant();


//        For fb
        //mCallbackManager = CallbackManager.Factory.create();

        activitySignUpBinding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUp();
            }
        });

        activitySignUpBinding.signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignIn();
            }
        });


        //        Google Sign in
        activitySignUpBinding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_clicked = "google";
            }
        });



        /*LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: ");
                Log.d("fblog", "onSuccess: " + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: ");
                Log.d("fblog", "onError: " + error);
            }
        });*/


//        Text Change Listeners
        textChangedListener(activitySignUpBinding.nameET, activitySignUpBinding.nameTIL);
        textChangedListener(activitySignUpBinding.numberET, activitySignUpBinding.numberTIL);
        textChangedListener(activitySignUpBinding.emailET, activitySignUpBinding.emailTIL);
        textChangedListener(activitySignUpBinding.passwordET, activitySignUpBinding.passwordTIL);



    }


    private void googleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void googleSignInIntent() {
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, RC_SIGN_IN);
    }

   /* private void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookToken: ");
        Log.d("fblog", "handleFacebookToken: " + accessToken);

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("fblog", "SignInWithCredential: successful");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    loginWithCredential(user);
                } else {
                    Toast.makeText(getApplicationContext(), "SignInWithCredential: failed\n" + task.getResult(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Result from intent from googleSignIn
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
//                Google Sign In successful
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Log.d("googlesignin: ", "firebaseAuthWithGoogle: " + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d("googlesignin: ", "Google Sign In failed\n" + e.getMessage());
                ;
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Sign in success
                            Log.d("googlesignin", "signInWithCredential: success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            loginWithCredential(user);
                        } else {
                            Toast.makeText(getApplicationContext(), "SignInWithCredential: failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private void loginWithCredential(FirebaseUser user) {
        String TAG = "cred";

        Log.d(TAG, "loginWithCredential: Name: " + user.getDisplayName());
        Log.d(TAG, "loginWithCredential: Email: " + user.getEmail());
        Log.d(TAG, "loginWithCredential: Phone: " + user.getPhoneNumber());


//        To add user to database if not done
        DocumentReference documentReference = firebaseDB.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid());

        Map<String, Object> userInfo = new HashMap<>();

//        To check if user already exists
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
//                        User exists with this account
                        Toast.makeText(SignUpActivity.this, "User already exists with this account, Please go to Log in page", Toast.LENGTH_SHORT).show();
                    } else
                    {
//                        Create account
                        userInfo.put(constant.getUserNameField(), user.getDisplayName());
                        userInfo.put(constant.getUserPhoneField(), user.getPhoneNumber());
                        userInfo.put(constant.getUserEmailField(), user.getEmail());
                        userInfo.put(constant.getUserPhotoField(), user.getPhotoUrl().toString());
                        userInfo.put(constant.getUserPhotoField(), null);
                        userInfo.put(constant.getUserBioField(), null);
                        userInfo.put(constant.getUserGraduate(), null);
                        userInfo.put(constant.getUserIdField(), firebaseAuth.getUid());


//

                        documentReference.set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                        Update connection to null
                                CollectionReference connectionRef = firebaseDB.collection(constant.getChatConnections());
                                connectionRef.document(firebaseAuth.getUid()).set(new ChatConnectionModel(null, null));
                            }
                        });


                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }
        });

    }

    private void onSignUp() {
//        Check whether any field is left blank
        flag = 0;
        checkEmptyField(activitySignUpBinding.nameET, activitySignUpBinding.nameTIL);
        checkEmptyField(activitySignUpBinding.numberET, activitySignUpBinding.numberTIL);
        checkEmptyField(activitySignUpBinding.emailET, activitySignUpBinding.emailTIL);
        checkEmptyField(activitySignUpBinding.passwordET, activitySignUpBinding.passwordTIL);

        //check for email format
        checkemailformat(activitySignUpBinding.emailET, activitySignUpBinding.emailTIL);

        //check phone number
        checkphonenumber(activitySignUpBinding.numberET, activitySignUpBinding.numberTIL);


        String NAME_ET=activitySignUpBinding.nameET.getText().toString()
                ,NUMBER_ET=activitySignUpBinding.numberET.getText().toString(),EMAIL_ET=activitySignUpBinding.emailET.getText().toString(),
                PASSWORD_ET=activitySignUpBinding.passwordET.getText().toString();


        if (flag == 6) {
//            We can now register the user
            activitySignUpBinding.signUpProgressLayout.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(
                    activitySignUpBinding.emailET.getText().toString(),
                    activitySignUpBinding.passwordET.getText().toString()
            ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
//                    Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                    assert firebaseUser != null;
                    firebaseUser.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "email sent");
                                        Toast.makeText(SignUpActivity.this, "We have sent an email to your registered "+EMAIL_ET+" for confirmation. After receiving email, follow the link provided" +
                                                "to complete email verification.", Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                    }
                                    else
                                    {
                                        Log.d(TAG, "error in email sent");
                                    }
                                }
                            });

//                    Now to store data into the database
                    DocumentReference documentReference = firebaseDB.collection("Users")
                            .document(firebaseAuth.getCurrentUser().getUid());

                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put(constant.getUserNameField(), NAME_ET);
                    userInfo.put(constant.getUserPhoneField(), NUMBER_ET);
                    userInfo.put(constant.getUserEmailField(), EMAIL_ET);
                    userInfo.put(constant.getUserPhotoField(), null);
                    userInfo.put(constant.getUserBioField(), null);
                    userInfo.put(constant.getUserGraduate(), null);
                    userInfo.put(constant.getUserIdField(), firebaseAuth.getUid());



                    documentReference.set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                         Update connection to null
                            CollectionReference connectionRef = firebaseDB.collection(constant.getChatConnections());
                            connectionRef.document(firebaseAuth.getUid()).set(new ChatConnectionModel(null, null));
                        }
                    });


                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                    activitySignUpBinding.signUpProgressLayout.setVisibility(View.GONE);
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(SignUpActivity.this, "Account creation failed:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                activitySignUpBinding.signUpProgressLayout.setVisibility(View.GONE);
            });
        }
    }

    private void onSignIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void textChangedListener(TextInputEditText ET, TextInputLayout TIL) {
        ET.addTextChangedListener(new addOnTextChangeListener(this, ET, TIL));
    }

    private void checkEmptyField(EditText et, TextInputLayout til) {
        if (et.getText().toString().isEmpty()) {
            til.setError("Field cannot be blank");
            til.setErrorEnabled(true);
            flag--;
        } else {
            flag++;
        }
    }



    private void checkemailformat(EditText et, TextInputLayout til)
    {
        if(Pattern.compile("gmail\\.com$").matcher(et.getText().toString()).find())
        {
            flag++;
        }
        else if(Pattern.compile("hotmail\\.com$").matcher(et.getText().toString()).find())
        {
            flag++;
        }
        else if(Pattern.compile("yahoo\\.com$").matcher(et.getText().toString()).find())
        {
            flag++;
        }
        else if(Pattern.compile("kiit\\.ac\\.in$").matcher(et.getText().toString()).find())
        {
            flag++;
        }
        else
        {
            til.setError("Please enter correct format");
            til.setErrorEnabled(true);
        }
    }

    private void checkphonenumber(EditText et, TextInputLayout til)
    {
        if(Pattern.compile("^[1-9][0-9]{9}$").matcher(et.getText().toString()).find())
        {
            flag++;
        }
        else
        {
            til.setError("Please correct the phone number");
            til.setErrorEnabled(true);
        }
    }



}
