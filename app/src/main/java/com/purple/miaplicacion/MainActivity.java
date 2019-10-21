package com.purple.miaplicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = " ";
    private FirebaseAuth mAuth;
    Button mInicioSesion, mRegistrar;
    EditText mCorreo, mClave;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;

    private FirebaseAnalytics mFirebaseAnalytics;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mCorreo = (EditText) findViewById(R.id.txt_correo);
        mClave = (EditText) findViewById(R.id.txt_password);
        mRegistrar =(Button) findViewById(R.id.button2);
        mInicioSesion =(Button) findViewById(R.id.Button_loginNuevo);

        mAuth = FirebaseAuth.getInstance();

        //Firebase Auten por google





        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

                mGoogleSignInClient = GoogleSignIn.getClient(this , gso);



                signInButton = (SignInButton) findViewById(R.id.sign_in_button);
                signInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, 101);




                    }
                });








        //Cuando ya llenaron los datos

        mInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password;
                email = mCorreo.getText().toString();
                password = mClave.getText().toString();




                if (TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "El campo correo No puede estar vacio.",
                            Toast.LENGTH_SHORT).show();
                    return;}


                if (TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "El campo password No puede estar vacio.",
                            Toast.LENGTH_SHORT).show();
                    return;}

                if (password.length() <= 5){
                    Toast.makeText(MainActivity.this, "El Campo Password NO debe ser menor a 5.",
                            Toast.LENGTH_SHORT).show();
                    return;}
                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "Yes sir");
                                    Intent  intent = new Intent(MainActivity.this, SesionOn.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "Inicio de Sesion Exitoso",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                    Toast.makeText(MainActivity.this, "Error al Iniciar Sesion",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });


            }
        });

        //Escuchador
        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegistrarNuevo.class));
            }
        });




    }
    private void signIn() {





    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Intent  intent = new Intent(MainActivity.this, SesionOn.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Inicio de sesion exitoso",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Intent  intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "No se pudo",
                                    Toast.LENGTH_SHORT).show();

                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }


    private void updateUI(FirebaseUser user) {

        if (user != null){
            Intent  intent = new Intent(MainActivity.this, SesionOn.class);
            startActivity(intent);


            finish();


        }



    }
}
