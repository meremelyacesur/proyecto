package com.example.proyecto_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private Button registerUser;
    private EditText etNombreR, etEdadR, etPasswordR, etEmailR, etAlturaR, etPesoR;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // Declaración de las variables
        mAuth = FirebaseAuth.getInstance();
        etNombreR = (EditText) findViewById(R.id.etNombreR);
        etEdadR = (EditText) findViewById(R.id.etEdadR);
        etPasswordR = (EditText) findViewById(R.id.etPasswordR);
        etEmailR = (EditText) findViewById(R.id.etEmailR);
        etAlturaR = (EditText) findViewById(R.id.etAlturaR);
        etPesoR = (EditText) findViewById(R.id.etPesoR);
        registerUser = (Button) findViewById(R.id.btRegisterUser);
        registerUser.setOnClickListener(this);

    }

    // Método on click asociado al botón de "Registrar nuevo usuario"
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btRegisterUser:
                registerUser();
                break;
        }
    }
    // Método de registro de un nuevo usuario
    private void registerUser() {
        String fullName = etNombreR.getText().toString().trim();
        String email = etEmailR.getText().toString().trim();
        String password = etPasswordR.getText().toString().trim();
        String age = etEdadR.getText().toString().trim();
        String altura = etAlturaR.getText().toString().trim();
        String peso = etPesoR.getText().toString().trim();
        // Verificación de campos vacíos
        if (email.isEmpty()) {
            etEmailR.setError("Campo obligatorio");
            etEmailR.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailR.setError("Introduzca un correo electrónico válido");
            etEmailR.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPasswordR.setError("Campo obligatorio");
            etPasswordR.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPasswordR.setError("La contraseña ha de ser mínimo de seis caracteres");
            etPasswordR.requestFocus();
            return;
        }
        if (fullName.isEmpty()) {
            etNombreR.setError("Campo obligatorio");
            etNombreR.requestFocus();
            return;
        }
        /* Omitir la obligación de los campos Edad, Altura, Peso.
        if(age.isEmpty()){
            etEdadR.setError("Age is requierd");
            etEdadR.requestFocus();
            return;
        }
        if(altura.isEmpty()){
            etAlturaR.setError("Altura!");
            etAlturaR.requestFocus();
            return;
        }
        if(peso.isEmpty()){
            etPesoR.setError("Peso!");
            etPesoR.requestFocus();
            return;
        }
        */
        // Método de la instancia de Firebase que recoge los datos de los campos y los almacena en la base de datos
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullName, age, email, altura, peso);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "Usuario registrado con éxito!", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(RegisterUser.this, MainActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(RegisterUser.this, "Fallo al registrar el usuario", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this, "Ya existe un usuario con ese correo electrónico", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}