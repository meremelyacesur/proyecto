package com.example.proyecto_login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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

public class tmb extends AppCompatActivity {

    EditText etPesoT, etAlturaT, etEdadT;
    Button btCalcularT, btCargarDatosT;
    ImageButton ibInfoT;
    TextView tvResultadoT;
    RadioButton rbHombreT, rbMujerT, rbAdelgazarT, rbMantenerT, rbEngordarT, rbBajoT, rbMedioT, rbAltoT;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmb);

        // Declaración de las variables
        etPesoT = (EditText) findViewById(R.id.etPesoT);
        etAlturaT = (EditText) findViewById(R.id.etAlturaT);
        etEdadT = (EditText) findViewById(R.id.etEdadT);
        tvResultadoT = (TextView) findViewById(R.id.tvResultadoT);
        btCalcularT = (Button) findViewById(R.id.btCalcularT);
        ibInfoT = (ImageButton) findViewById(R.id.ibInfoT);
        btCargarDatosT = (Button) findViewById(R.id.btCargarDatosT);
        rbHombreT = (RadioButton) findViewById(R.id.rbHombreT);
        rbMujerT = (RadioButton) findViewById(R.id.rbMujerT);
        rbAdelgazarT = (RadioButton) findViewById(R.id.rbAdelgazarT);
        rbMantenerT = (RadioButton) findViewById(R.id.rbMantenerT);
        rbEngordarT = (RadioButton) findViewById(R.id.rbEngordarT);
        rbBajoT = (RadioButton) findViewById(R.id.rbBajoT);
        rbMedioT = (RadioButton) findViewById(R.id.rbMedioT);
        rbAltoT = (RadioButton) findViewById(R.id.rbAltoT);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        // Con ese metodo se cargan los datos del usuario introduciodos durante el registro en los campos de la calculadora (activity.tmb)
        btCargarDatosT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if(userProfile !=null){
                            String age = userProfile.age;
                            String altura = userProfile.altura;
                            String peso = userProfile.peso;
                            etAlturaT.setText(altura);
                            etPesoT.setText(peso);
                            etEdadT.setText(age);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(tmb.this, "Algo no fue bien", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Método listener asociado al botón calcular
        btCalcularT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Recoge los datos introducidos en los String de EditText y los transforma
                en Double, variable count1 realiza la operación matematica,
                variable countResultado transforma el resultado de la operación en String,
                el cual es mostrado dentro del TextView (tvResultadoT)
                 */
                // Declaración variables
                String datosAltura = etAlturaT.getText().toString();
                String datosEdad = etEdadT.getText().toString();
                String datosPeso = etPesoT.getText().toString();
                double altura;
                double edad;
                double peso;

                // Verificación de los campos vacíos
                if (datosPeso.isEmpty()) {
                    etPesoT.setError("Debe introducir un número para el peso");
                    etPesoT.requestFocus();
                    return;
                } else {
                    peso = Double.parseDouble(datosPeso);
                }

                if (datosAltura.isEmpty()) {
                    etAlturaT.setError("Debe introducir un número para la altura");
                    etAlturaT.requestFocus();
                    return;
                } else {
                    altura = Double.parseDouble(datosAltura);
                }

                if (datosEdad.isEmpty()) {
                    etEdadT.setError("Debe introducir un número para la edad");
                    etEdadT.requestFocus();
                    return;
                } else {
                    edad = Double.parseDouble(datosEdad);
                }

                // Declaración de las variables para la realización de la fórmula, dependiendo de las condiciones
                double countNivelActividad;
                double countHombre;
                double countMujer;
                String countResultado;
                int countResultadoNumEntero;
                double count1 = peso * 9.99 + altura * 6.25 + edad * 4.92;

                if (rbHombreT.isChecked() && rbBajoT.isChecked() && rbAdelgazarT.isChecked()) {
                    countHombre = count1 + 5;
                    countNivelActividad = countHombre * 1.2 * 0.8;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbHombreT.isChecked() && rbMedioT.isChecked() && rbAdelgazarT.isChecked()) {
                    countHombre = count1 + 5;
                    countNivelActividad = countHombre * 1.5 * 0.8;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbHombreT.isChecked() && rbAltoT.isChecked() && rbAdelgazarT.isChecked()) {
                    countHombre = count1 + 5;
                    countNivelActividad = countHombre * 1.75 * 0.8;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }

                if (rbMujerT.isChecked() && rbBajoT.isChecked() && rbAdelgazarT.isChecked()) {
                    countMujer = count1 - 161;
                    countNivelActividad = countMujer * 1.2 * 0.8;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbMujerT.isChecked() && rbMedioT.isChecked() && rbAdelgazarT.isChecked()) {
                    countMujer = count1 - 161;
                    countNivelActividad = countMujer * 1.5 * 0.8;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbMujerT.isChecked() && rbAltoT.isChecked() && rbAdelgazarT.isChecked()) {
                    countMujer = count1 - 161;
                    countNivelActividad = countMujer * 1.75 * 0.8;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }

                ///////////////////////////////////////////////////////////////////////

                if (rbHombreT.isChecked() && rbBajoT.isChecked() && rbMantenerT.isChecked()) {
                    countHombre = count1 + 5;
                    countNivelActividad = countHombre * 1.2;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbHombreT.isChecked() && rbMedioT.isChecked() && rbMantenerT.isChecked()) {
                    countHombre = count1 + 5;
                    countNivelActividad = countHombre * 1.5;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbHombreT.isChecked() && rbAltoT.isChecked() && rbMantenerT.isChecked()) {
                    countHombre = count1 + 5;
                    countNivelActividad = countHombre * 1.75;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }

                if (rbMujerT.isChecked() && rbBajoT.isChecked() && rbMantenerT.isChecked()) {
                    countMujer = count1 - 161;
                    countNivelActividad = countMujer * 1.2;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbMujerT.isChecked() && rbMedioT.isChecked() && rbMantenerT.isChecked()) {
                    countMujer = count1 - 161;
                    countNivelActividad = countMujer * 1.5;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbMujerT.isChecked() && rbAltoT.isChecked() && rbMantenerT.isChecked()) {
                    countMujer = count1 - 161;
                    countNivelActividad = countMujer * 1.75;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }

                ///////////////////////////////////////////////////////////////////////////////////

                if (rbHombreT.isChecked() && rbBajoT.isChecked() && rbEngordarT.isChecked()) {
                    countHombre = count1 + 5;
                    countNivelActividad = countHombre * 1.2 * 1.2;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbHombreT.isChecked() && rbMedioT.isChecked() && rbEngordarT.isChecked()) {
                    countHombre = count1 + 5;
                    countNivelActividad = countHombre * 1.5 * 1.2;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbHombreT.isChecked() && rbAltoT.isChecked() && rbEngordarT.isChecked()) {
                    countHombre = count1 + 5;
                    countNivelActividad = countHombre * 1.75 * 1.2;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }

                if (rbMujerT.isChecked() && rbBajoT.isChecked() && rbEngordarT.isChecked()) {
                    countMujer = count1 - 161;
                    countNivelActividad = countMujer * 1.2 * 1.2;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbMujerT.isChecked() && rbMedioT.isChecked() && rbEngordarT.isChecked()) {
                    countMujer = count1 - 161;
                    countNivelActividad = countMujer * 1.5 * 1.2;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }
                if (rbMujerT.isChecked() && rbAltoT.isChecked() && rbEngordarT.isChecked()) {
                    countMujer = count1 - 161;
                    countNivelActividad = countMujer * 1.75 * 1.2;
                    countResultadoNumEntero = (int) Math.round(countNivelActividad);
                    countResultado = String.valueOf(countResultadoNumEntero);
                    tvResultadoT.setText(countResultado);
                }

                /* La clase SharedPrefernces recoge los datos de EditText (tvResultadoT),
                   los almacena de modo permanente en la variable "mail"
                */
                SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferencias.edit();
                editor.putString("mail", tvResultadoT.getText().toString());
                editor.commit();
            }
        });

        // Método on click listener asociado al botón ibInfoT, muestra un mensaje emergente al recibir un click
        ibInfoT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(tmb.this, "Nivel de actividad: \nBAJO - estilo de vida sedentario \nMEDIO - hasta 5 entrenamientos semanales \nALTO - alto esfuerzo fisico diario", Toast.LENGTH_LONG).show();
            }
        });

        // RadioGroup (rgGeneroT), recoge los RadioButton y comprueba que solo haya una solo selección activa
        rbHombreT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbHombreT.isChecked()) {
                    rbMujerT.setChecked(false);
                }
            }
        });

        rbMujerT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbMujerT.isChecked()) {
                    rbHombreT.setChecked(false);
                }
            }
        });

        // RadioGroup (rgPesoT), recoge los RadioButton y comprueba que solo haya una solo selección activa
        rbAdelgazarT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbAdelgazarT.isChecked()){
                    rbMantenerT.setChecked(false);
                    rbEngordarT.setChecked(false);
                }
            }
        });

        rbMantenerT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbMantenerT.isChecked()){
                    rbAdelgazarT.setChecked(false);
                    rbEngordarT.setChecked(false);
                }
            }
        });

        rbEngordarT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbEngordarT.isChecked()){
                    rbAdelgazarT.setChecked(false);
                    rbMantenerT.setChecked(false);
                }
            }
        });

        // RadioGroup (rgNivelT), recoge los RadioButton y comprueba que solo haya una solo selección activa
        rbBajoT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbBajoT.isChecked()){
                    rbMedioT.setChecked(false);
                    rbAltoT.setChecked(false);
                }
            }
        });

        rbMedioT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbMedioT.isChecked()){
                    rbBajoT.setChecked(false);
                    rbAltoT.setChecked(false);
                }
            }
        });

        rbAltoT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbAltoT.isChecked()){
                    rbMedioT.setChecked(false);
                    rbBajoT.setChecked(false);
                }
            }
        });

    }

}