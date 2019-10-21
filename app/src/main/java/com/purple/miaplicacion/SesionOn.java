package com.purple.miaplicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SesionOn extends AppCompatActivity {
    private Button button_cerrar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_on);
        button_cerrar =(Button) findViewById(R.id.button_cerrar);
        // Detecta usuario actual
        mAuth = FirebaseAuth.getInstance();


        button_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SesionOn.this, MainActivity.class));

            }
        });
    }
}
