package com.example.proyecto_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Definición de las variables
        logout = (Button) findViewById(R.id.logout);

        // Método on click para cerrar sessión y volver a MainActivity
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        // Definición de las variables utilizadas en Firebase para registar al usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView etSaludo = (TextView) findViewById(R.id.etSaludo);

        // Método que recoge el valor del campo "nombre" del usuario para mostrar un saludo personalizado
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile !=null){
                    String fullname = userProfile.fullName;
                    etSaludo.setText("¡Hola " + fullname + "!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Algo no ha ido bien", Toast.LENGTH_LONG).show();
            }
        });

    }

    // Tres métodos on click para abrir activity correspondiente
    public void gototmb(View view) {
        Intent i = new Intent(this, tmb.class);
        startActivity(i);
    }

    public void gotogym(View view) {
        Intent i = new Intent(this, gym.class);
        startActivity(i);
    }

    public void menudb(View view) {
        Intent i = new Intent(this, dieta.class);
        startActivity(i);
    }

}