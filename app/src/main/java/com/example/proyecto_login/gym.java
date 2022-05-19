package com.example.proyecto_login;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class gym extends AppCompatActivity {

    CalendarView calendarViewGym = null;
    EditText etDateGym, etDiarioGym;
    Button btGuardarGym, btRecuperarGym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        // Declaración de las variables
        calendarViewGym = (CalendarView) findViewById(R.id.calendarViewGym);
        etDateGym = (EditText) findViewById(R.id.etDateGym);
        etDiarioGym = (EditText) findViewById(R.id.etDiarioGym);
        btGuardarGym = (Button) findViewById(R.id.btCalcularT);
        btRecuperarGym = (Button) findViewById(R.id.btRecuperarGym);

        // Método on click encargado de crear un archivo con el nombre de la fecha elegida (etDateGym) y guardar la informacón recogida de EditText etDiarioGym dentro de ese archivo
        btGuardarGym.setOnClickListener(view -> {
            String nomarchivo = etDateGym.getText().toString().replace('/', '-');
            try {
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(nomarchivo, Activity.MODE_PRIVATE));
                archivo.write(etDiarioGym.getText().toString());
                archivo.flush();
                archivo.close();
            } catch (IOException e) {
            }
            Toast.makeText(this, "Los datos han sido guardados", Toast.LENGTH_SHORT).show();
            //etDateGym.setText("");
            etDiarioGym.setText("");
        });

        // Método on click encargado de buscar un archivo con el nombre de la fecha elegida (etDateGym) y recuperar la informacón almacenada en dicho archivo
        btRecuperarGym.setOnClickListener(v -> {
            String nomarchivo = etDateGym.getText().toString();
            nomarchivo = nomarchivo.replace('/', '-');
            boolean enco = false;
            String[] archivos = fileList();
            for (int f = 0; f < archivos.length; f++)
                if (nomarchivo.equals(archivos[f]))
                    enco = true;
            if (enco == true) {
                try {
                    InputStreamReader archivo = new InputStreamReader(openFileInput(nomarchivo));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String todo = "";
                    while (linea != null) {
                        todo = todo + linea + "\n";
                        linea = br.readLine();
                    }
                    br.close();
                    archivo.close();
                    etDiarioGym.setText(todo);
                } catch (IOException e) {
                }
            } else {
                Toast.makeText(this, "No hay datos guardados para la fecha seleccionada", Toast.LENGTH_SHORT).show();
                etDiarioGym.setText("");
            }
        });

        // Método que convierte la fecha elegida en el calendario en un String que se almacena dentro de etDateGym
        calendarViewGym.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int año, int mes, int dia) {
                String date = dia + "/" + (mes + 1) + "/" + año;
                etDateGym.setText(date);
            }
        });
    }
}