package com.example.proyecto_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvRegitroMain, tvPasswordLostMain;
    private EditText etPasswordMain, etEmailMain;
    private Button btLoginMain;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declaración de las variables
        btLoginMain=(Button) findViewById(R.id.btLoginMain);
        etEmailMain = (EditText) findViewById(R.id.etEmailR);
        etPasswordMain = (EditText) findViewById(R.id.etPasswordR);
        tvRegitroMain = (TextView) findViewById(R.id.tvRegitroMain);
        tvPasswordLostMain = (TextView) findViewById(R.id.tvPasswordLostMain);
        btLoginMain.setOnClickListener(this);
        tvRegitroMain.setOnClickListener(this);
        tvPasswordLostMain.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }
    // Método on click con switch para diferentes elementos
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvRegitroMain:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.btLoginMain:
                UserLogin();
                break;
            case R.id.tvPasswordLostMain:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }
    // Método encargado de comprobar los datos introducidos y dar acceso a la aplicación si encuentra un usuario registrado en la base de datos
    private void UserLogin() {
        String email = etEmailMain.getText().toString().trim();
        String password = etPasswordMain.getText().toString().trim();
        // Verificación de los campos vacíos
        if(email.isEmpty()){
            etEmailMain.setError("Campo obligatorio");
            etEmailMain.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailMain.setError("Introduzca un correo electrónico válido");
            etEmailMain.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPasswordMain.setError("Campo obligatorio");
            etPasswordMain.requestFocus();
            return;
        }
        if(password.length() < 6){
            etPasswordMain.setError("La contraseña ha de ser mínimo de seis caracteres");
            etPasswordMain.requestFocus();
            return;
        }
        /* Método de la instancia de Firebase que comprueba la existencia de un usuario
        registrado en la base de datos, comprobando el correo electrónico y la contraseña.
        */
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Compruebe su correo electrónico y siga las instrucciones", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Compruebe los datos introducidos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}