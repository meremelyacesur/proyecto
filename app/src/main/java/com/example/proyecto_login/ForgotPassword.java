package com.example.proyecto_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText etEmail;
    private Button btResetPassword;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        // Declaración de las variables
        etEmail = (EditText) findViewById(R.id.etEmail);
        btResetPassword = (Button) findViewById(R.id.btResetPassword);
        // Instancia de la base de datos
        auth = FirebaseAuth.getInstance();
        // Método on click asociado al botón de Recuperar contraseña que invoca el método resetPassword
        btResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    // Método resetPassword encargado de comprobar que los campos tengan la información y el formato adecuados, para hacer uso de la instancia (auth) contra la base de datos
    private void resetPassword(){
        String email = etEmail.getText().toString().trim();
        // Verificación de los campos
        if(email.isEmpty()){
            etEmail.setError("Campo obligatorio");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Introduzca un correo electrónico válido");
            etEmail.requestFocus();
            return;
        }

        // Método de la instancia de Firebase que envia un correo para restabalcer la contraseña
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Compruebe su correo electrónico y siga las instrucciones", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPassword.this, "Algo no ha ido bien, vuelva a intentarlo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}