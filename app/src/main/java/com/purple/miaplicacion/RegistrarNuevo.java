package com.purple.miaplicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegistrarNuevo extends AppCompatActivity {

    private static final String TAG = " ";
    private FirebaseAuth mAuth;

    Button mInicioSesion, mRegistrar;
    EditText mCorreo, mClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_nuevo);
        mAuth = FirebaseAuth.getInstance();


        mCorreo = (EditText) findViewById(R.id.txt_correo);
        mClave = (EditText) findViewById(R.id.txt_password);
        mRegistrar =(Button) findViewById(R.id.Button_RegistrarNuevo);
        mInicioSesion =(Button) findViewById(R.id.Button_loginNuevo);

        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email,password;
                email = mCorreo.getText().toString();
                password = mClave.getText().toString();

                //Patron











                //Cierran validacion correcta e incorrecta


                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegistrarNuevo.this, "El campo correo No puede estar vacio.",
                            Toast.LENGTH_SHORT).show();
                return;}


                if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegistrarNuevo.this, "El campo password No puede estar vacio.",
                            Toast.LENGTH_SHORT).show();
                    return;}

                if (password.length() <= 5){
                    Toast.makeText(RegistrarNuevo.this, "El Campo Password NO debe ser menor a 5.",
                            Toast.LENGTH_SHORT).show();
                    return;}



                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrarNuevo.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent  intent = new Intent(RegistrarNuevo.this, SesionOn.class);
                                    startActivity(intent);
                                    Toast.makeText(RegistrarNuevo.this, "Inicio de Sesion Correcto.",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegistrarNuevo.this, "Error al inciar sesion.",
                                            Toast.LENGTH_SHORT).show();

                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegistrarNuevo.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        });
        mInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarNuevo.this,MainActivity.class));

            }
        });





    }
    private void updateUI(FirebaseUser user) {

        if (user != null){
            Intent intent = new Intent(RegistrarNuevo.this, SesionOn.class);
            startActivity(intent);


            finish();


        }



    }
}
