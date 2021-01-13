package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    public static Activity act;
    public int decision=0;
    //contiene la informacion del pokemon
    public static TextView txtDisplay;
    //contiene la imagen del pokemon
    public static ImageView imgPok;
    //contiene la longitud de los pokemon consultados en type/fire
    public static int long_itud;
    //iterador de los tipos especificos
    public static int type_variable=0;

    public String newPokSearch;
    //contiene las imagenes de los tipos
    public static ImageView [] imgType;
    public int click=1;
    Button PokemonSiguiente;
    Button PokemonAnterior;
    Button PokemonInferior;
    Button PokemonSuperior;
    //iterados botonnes derecha e izquierda
    public int iterador;
    private View v;
    private String[] type_especific;

    public Button jona;
    public MediaPlayer jonathan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;

        type_especific =getResources().getStringArray(R.array.tipos_especificos);
        imgType = new ImageView[2];

        //es el textview que contiene la informacion del pokemon nombre,id etc
        txtDisplay = findViewById(R.id.txtDisplay);

        //es una imagen del pokemon en nuestra pantalla
        imgPok = findViewById(R.id.imgPok);

        //imagenes que contienen la imagenes de los tipos del pokemon
        imgType[0] = findViewById(R.id.imgType0);
        imgType[1] = findViewById(R.id.imgType1);



        //de entrada los botones de arriba y abajo estan desactivados.

        iterador=1;
        String pokSearch = String.valueOf("pokemon/"+iterador);
        fetchData process = new fetchData(pokSearch,decision);
        process.execute();


        /*Buscador boton amarillo*/
       ImageButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTxtSearch();
            }
        });
        //Boton Azul del main
        ImageButton btnTypes = findViewById(R.id.btnTypes);
        //Boton de la cruz derecho
        PokemonSiguiente=findViewById(R.id.btnRight);
        PokemonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iterador>=1118){
                    iterador=1118;
                    String pokSearch = String.valueOf("pokemon/"+iterador);
                    fetchData process = new fetchData(pokSearch,decision);
                    process.execute();
                }else{
                    iterador++;
                    String pokSearch = String.valueOf("pokemon/"+iterador);
                    fetchData process = new fetchData(pokSearch,decision);
                    process.execute();
                }
            }
        });
        //Boton de la cruz izquierda
        PokemonAnterior=findViewById(R.id.btnLeft);
        PokemonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iterador<=1){//VALIDA QUE NO SE VAYA A CERO
                    iterador=1;
                    String pokSearch = String.valueOf("pokemon/"+iterador);
                    fetchData process = new fetchData(pokSearch,decision);
                    process.execute();
                }else{
                    iterador--;
                    String pokSearch = String.valueOf("pokemon/"+iterador);
                    fetchData process = new fetchData(pokSearch,decision);
                    process.execute();
                }
            }
        });
        //Boton de la cruz arriba
        PokemonSuperior=findViewById(R.id.btnUp);
        PokemonSuperior.setEnabled(false);
        PokemonSuperior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type_variable>=long_itud){
                    type_variable=long_itud;
                    fetchData process = new fetchData("type/"+newPokSearch,decision);
                    process.execute();
                }else{
                    type_variable++;
                    fetchData process = new fetchData("type/"+newPokSearch,decision);
                    process.execute();
                }
            }
        });
        //Boton de la cruz abajo
        PokemonInferior=findViewById(R.id.btnDown);
        PokemonInferior.setEnabled(false);
        PokemonInferior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type_variable<=1){
                    type_variable=1;
                    fetchData process = new fetchData("type/"+newPokSearch,decision);
                    process.execute();
                }else{
                    type_variable--;
                    fetchData process = new fetchData("type/"+newPokSearch,decision);
                    process.execute();
                }
            }
        });
        jona=findViewById(R.id.btn_musica);
        jona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jonathan!=null){
                        jonathan.release();
                }
                jonathan=MediaPlayer.create(getApplicationContext(),R.raw.poke);

                if (click==3){
                    jonathan.start();
                    click=2;
                }else if (click==1){
                    jonathan.seekTo(0);
                    jonathan.start();
                    click++;
                }else if(click==2){
                    jonathan.pause();
                    click++;
                }
            }
        });


    }//FINAL METODO ONCREATE
    //METODO QUE SE ACTICA AL CLICAR EL BOTON AZUL REDONDO
    public void crearDialogo(View V){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("ELIGE UN TIPO");
        builder.setItems(R.array.tipos_especificos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                type_variable=1;
                Toast.makeText(MainActivity.this,"BOTONES UP/DOWN ACTIVADOS RIGHT/LEFT DESACTIVADOS",Toast.LENGTH_LONG).show();
                PokemonAnterior.setEnabled(false);
                PokemonSiguiente.setEnabled(false);
                decision=1;
                newPokSearch = type_especific[i];
                Toast.makeText(MainActivity.this,"SE ACTIVARAN DE NUEVO AL CLICAR EL BOTON AMARILLO",Toast.LENGTH_LONG).show();
                fetchData process1 = new fetchData("type/"+newPokSearch,decision);
                process1.execute();
                PokemonInferior.setEnabled(true);
                PokemonSuperior.setEnabled(true);
            }
        });
        Dialog dialog=builder.create();
        dialog.show();
    }
    public void showTxtSearch(){//METODO QUE ACTIVA EL BOTON AMARILLO
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("INGRESA ID POKEMON\nEJEMPLO 5 CHARMANDER");

        final EditText input = new EditText(this);
        input.setHint("Pokemon");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                PokemonSuperior.setEnabled(false);
                PokemonInferior.setEnabled(false);
                decision=0;
                String pokSearch = input.getText().toString();
                fetchData process = new fetchData("pokemon/"+pokSearch,decision);
                iterador=Integer.parseInt(pokSearch);
                process.execute();
                PokemonAnterior.setEnabled(true);
                PokemonSiguiente.setEnabled(true);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }


}




