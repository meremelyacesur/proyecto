package com.example.proyecto_login;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class dieta extends AppCompatActivity {

    EditText etNombreM, etKcal100gM, etGramosConsM, etDateM, etKcalQuemM, etKcalAcumuladoM;
    public static EditText etKcalLimiteM;
    Button btCrearM, btConsultarM, btEliminarM, btRegistrarM, btApuntarM, btActualizarM, btEliminarUltimoM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta);

        // Declaración de las variables
        etNombreM = findViewById(R.id.etNombreM);
        etKcal100gM = findViewById(R.id.etKcal100gM);
        etGramosConsM = findViewById(R.id.etGramosConsM);
        etDateM = findViewById(R.id.etDateM);
        etKcalQuemM = findViewById(R.id.etKcalQuemM);
        etKcalLimiteM = findViewById(R.id.etKcalLimiteM);
        etKcalAcumuladoM = findViewById(R.id.etKcalAcumuladoM);
        btCrearM = (Button) findViewById(R.id.btCrearM);
        btConsultarM = (Button) findViewById(R.id.btConsultarM);
        btEliminarM = (Button) findViewById(R.id.btEliminarM);
        btRegistrarM = (Button) findViewById(R.id.btRegistrarM);
        btApuntarM = (Button) findViewById(R.id.btApuntarM);
        btActualizarM = (Button) findViewById(R.id.btActualizarM);
        btEliminarUltimoM = (Button) findViewById(R.id.btEliminarUltimoM);

        // Establece el formato de la fecha y lo almacena en etDateM
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fechahoy = sdf.format(new Date());
        etDateM.setText(fechahoy);

        // Recibe datos de "activity_gym resultado" y almacena en etKcalLimiteM
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        etKcalLimiteM.setText(prefe.getString("mail",""));

        // Método recoge los datos introducidos en los editText de nombre y calorias y los guarda en la base de datos
        btCrearM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(dieta.this, "bbdd", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                String nombre = etNombreM.getText().toString().trim();
                String kcal = etKcal100gM.getText().toString().trim();
                ContentValues registro = new ContentValues();
                // Verificación de campos no vacíos
                if(nombre.isEmpty()){
                    etNombreM.setError("Introduzca un nombre");
                    etNombreM.requestFocus();
                    return;
                }
                if(kcal.isEmpty()){
                    etKcal100gM.setError("Introduzca el número de calorías");
                    etKcal100gM.requestFocus();
                    return;
                } // Recoge la información de los campos y la guarda en la base de datos
                else{
                    registro.put("nombre", nombre);
                    registro.put("kcal", kcal);
                    bd.insert("productos", null, registro);
                    bd.close();
                    etNombreM.setText("");
                    etKcal100gM.setText("");
                    Toast.makeText(dieta.this, "Los datos introducidos se han guardado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Método que comprueba la existencia del articulo almacenado en la base de datos mediante su nombre (etNombreM) y lo elimina en caso de su existencia
        btEliminarM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(dieta.this, "bbdd", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                String nombre = etNombreM.getText().toString();
                int cant = bd.delete("productos", "nombre='" + nombre +"'", null);
                bd.close();
                if(cant == 1){
                    Toast.makeText(dieta.this, "Se han eliminado los datos", Toast.LENGTH_SHORT).show();
                    etNombreM.setText("");
                    etKcal100gM.setText("");
                }else{
                    Toast.makeText(dieta.this, "No existen datos para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Método que muestra el articulo almacenado en la base de datos mediante la busqueda por nombre (etNombreM)
        btConsultarM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(dieta.this, "bbdd", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                String nombre = etNombreM.getText().toString().trim();
                Cursor fila = bd.rawQuery("SELECT kcal FROM productos WHERE nombre='" + nombre +"'", null);
                if (fila.moveToFirst()){
                    etKcal100gM.setText(fila.getString(0));
                } else
                    Toast.makeText(dieta.this,"No existen datos almacenados", Toast.LENGTH_SHORT).show();
                bd.close();
            }
        });

        /* Método que realiza el cálculo de las calorias consumidas
           (multiliplicando las Kcal/100g por la cantidad consumida en gramos)
           se puede utilizar conjunatamente con el método btConsultarM para mayor comodidad
        */
        btRegistrarM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(dieta.this, "bbdd", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();

                String datosCalorias = etKcal100gM.getText().toString().trim();
                String datosGramos = etGramosConsM.getText().toString().trim();
                // Verificación de campos no vacíos
                if(datosCalorias.isEmpty()){
                    etKcal100gM.setError("Introduzca un valor");
                    etKcal100gM.requestFocus();
                    return;
                }
                if(datosGramos.isEmpty()){
                    etGramosConsM.setError("Introduzca un valor");
                    etGramosConsM.requestFocus();
                    return;
                }
                int gramos;
                int calorias;
                int operacion;
                String cuentoCalorias;
                calorias = Integer.parseInt(datosCalorias);
                gramos = Integer.parseInt(datosGramos);
                operacion = calorias * gramos / 100;
                cuentoCalorias = String.valueOf(operacion);
                String nombre = etNombreM.getText().toString();
                String kcalcontado = cuentoCalorias;
                String fecha = etDateM.getText().toString();
                ContentValues registro = new ContentValues();
                registro.put("nombre", nombre);
                registro.put("kcalcontado", kcalcontado);
                registro.put("fecha", fecha);
                bd.insert("registro_consumo", null, registro);
                // Consulta sql contra la base de datos que muestra dentro de (etKcalAcumuladoM) la suma total de los registros calóricos (kcalcontado)
                Cursor fila = bd.rawQuery("SELECT SUM(kcalcontado) FROM registro_consumo WHERE fecha='" + fecha +"'", null);
                if (fila.moveToFirst()){
                    etKcalAcumuladoM.setText(fila.getString(0));
                }
                bd.close();
            }
        });

        // Registra un numero negativo de calorias (etKcalQuemM), lo almacena en la base de datos y muestra el total (etKcalAcumuladoM)
        btApuntarM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(dieta.this, "bbdd", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                String kcalcontado = etKcalQuemM.getText().toString();
                String fecha = etDateM.getText().toString();
                // Verificación de campos no vacíos
                if(kcalcontado.isEmpty()){
                    etKcalQuemM.setError("Introduzca un valor");
                    etKcalQuemM.requestFocus();
                    return;
                }
                ContentValues registro = new ContentValues();
                registro.put("kcalcontado", '-' + kcalcontado);
                registro.put("fecha", fecha);
                bd.insert("registro_consumo", null, registro);
                // Consulta sql contra la base de datos que muestra dentro de (etKcalAcumuladoM) la suma total de los registros calóricos (kcalcontado)
                Cursor fila = bd.rawQuery("SELECT SUM(kcalcontado) FROM registro_consumo WHERE fecha='" + fecha +"'", null);
                if (fila.moveToFirst()){
                    etKcalAcumuladoM.setText(fila.getString(0));
                }
                bd.close();
            }
        });

        /* Método que muestra el total de las Kcal acumuladas,
        además compara el resultado con el valor del campo Limite Kcal (etKcalLimiteM) que proviene de Activity.gym         
        */
        btActualizarM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(dieta.this, "bbdd", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                String fecha = etDateM.getText().toString();
                Cursor fila = bd.rawQuery("SELECT SUM(kcalcontado) FROM registro_consumo WHERE fecha='" + fecha +"'", null);
                if (fila.moveToFirst()){
                    etKcalAcumuladoM.setText(fila.getString(0));
                } else
                    Toast.makeText(dieta.this,"No existen datos almacenados", Toast.LENGTH_SHORT).show();
                bd.close();
                // Comparación de valores para hallar el mayor
                String sa = etKcalAcumuladoM.getText().toString();
                String sb = etKcalLimiteM.getText().toString();
                // Verificación de valores no vacíos
                if(sa.isEmpty()){
                    //etKcalAcumuladoM.setError("No existen datos almacenados para la fecha elegida");
                    //etKcalAcumuladoM.requestFocus();
                    Toast.makeText(dieta.this, "No existen datos almacenados para la fecha elegida", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sb.isEmpty()){
                    etKcalLimiteM.setError("No existen datos almacenados");
                    etKcalLimiteM.requestFocus();
                    return;
                }
                // Muestra un mensaje emergente dependiendo del resultado de la comparación
                int a = Integer.parseInt(sa);
                int b = Integer.parseInt(sb);
                if (a > b) {
                    Toast.makeText(dieta.this, "Has superado el consumo de calorias", Toast.LENGTH_SHORT).show();
                }
                if (b >= a) {
                    Toast.makeText(dieta.this, "Sigue así!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Método encargado de eliminar el ÚLTIMO registro almacenado en la base de datos
        btEliminarUltimoM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(dieta.this, "bbdd", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                // Sentencia sql que busca el mayor de los id y procede a su borrado
                int cant = bd.delete("registro_consumo", "ID=(SELECT MAX(ID) FROM registro_consumo)", null);
                String fecha = etDateM.getText().toString();
                Cursor fila = bd.rawQuery("SELECT SUM(kcalcontado) FROM registro_consumo WHERE fecha='" + fecha +"'", null);
                if (fila.moveToFirst()){
                    etKcalAcumuladoM.setText(fila.getString(0));
                }
                bd.close();
                if (cant == 1)
                    Toast.makeText(dieta.this, "Se han eliminado los datos",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(dieta.this, "No existen datos para eliminar",Toast.LENGTH_SHORT).show();
            }
        });


    }
}