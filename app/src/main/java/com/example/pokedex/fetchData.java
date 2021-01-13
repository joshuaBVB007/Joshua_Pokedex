package com.example.pokedex;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

//import com.ahmadrosid.svgloader.SvgLoader;
//import com.squareup.picasso.Picasso;

import com.ahmadrosid.svgloader.SvgLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static com.example.pokedex.MainActivity.long_itud;
import static com.example.pokedex.MainActivity.type_variable;

public class fetchData extends AsyncTask<Void, Void, Void> {
    //en data almacena los datos del los pokemon name,id,etc
    protected String data = "";
    //result es el string donde se escribe la informacion del JSON ver linea 108
    protected String results = "";
    //decision es un entero que decide si hago una consulta a pokemon o types
    public int decision;
    //strTypes almacena los datos de los tipos osea las imagenes
    protected static ArrayList<String> strTypes; // Create an ArrayList object
    //poksearch es lo que yo introduzco al clicar el button search
    protected String pokSearch;

    String img="";
    public fetchData(String pokSearch,int decision) {
        this.pokSearch = pokSearch;
        this.decision=decision;
        strTypes = new ArrayList<String>();
    }

    @Override
    protected Void doInBackground(Void... voids) {//ESTE METODO SE HACE EN SEGUNDO PLANO
        try {
            //Make API connection
            URL url=null;
            if(decision==0){
                url = new URL("https://pokeapi.co/api/v2/" + pokSearch);
                Log.i("ruta pokemon", "vas por tuta pokemon" + pokSearch);
            }else if(decision==1){
                url = new URL("https://pokeapi.co/api/v2/" + pokSearch);
                Log.i("ruta type", "vas por ruta types" + pokSearch);
            }


            //creamos la coneccion con internet
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            // Read API results
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sBuilder = new StringBuilder();


            // Build JSON String
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            inputStream.close();

            //al parecer nos escribe tot el contenido que hemos iterado
            data = sBuilder.toString();



        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){

        img = "";

        if(decision==0){
            Log.i("hola", "estamos en decision 0 ");
            try {
                // en data esta la informacion de cada pokemon
                JSONObject jObject = new JSONObject(data);

                // Get JSON name, height, weight,id,base,weight
                results += "Name: " + jObject.getString("name").toUpperCase() + "\n" +
                        "Height: " + jObject.getString("height") + "\n" +
                        "ID: " + jObject.getString("id") + "\n" +
                        "Base: " + jObject.getString("base_experience") + "\n" +
                        "Weight: " + jObject.getString("weight");


                // Get img SVG
                JSONObject sprites = new JSONObject(jObject.getString("sprites"));
                JSONObject other = new JSONObject(sprites.getString("other"));
                JSONObject dream_world = new JSONObject(other.getString("dream_world"));
                img  = dream_world.getString("front_default");

                // Get type/types
                JSONArray types = new JSONArray(jObject.getString("types"));
                for(int i=0; i<types.length(); i++){
                    JSONObject type = new JSONObject(types.getString(i));
                    JSONObject type2  = new JSONObject(type.getString("type"));
                    strTypes.add(type2.getString("name"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            // Set info
            MainActivity.txtDisplay.setText(this.results);

            mostraImagen(decision);



        }else if(decision==1){

            Log.i("hola", "estamos en decision 1 ");
            try {

               JSONObject jObject = new JSONObject(data);

                //Con estas lineas de codigo se hace la sustraccion de datos de la red
               JSONArray pokemons_es = new JSONArray(jObject.getString("pokemon"));
                for(int i=0; i<pokemons_es.length(); i++){
                    JSONObject type = new JSONObject(pokemons_es.getString(i));
                    JSONObject type2  = new JSONObject(type.getString("pokemon"));
                    strTypes.add(type2.getString("name"));

                }
                //Aca leemos todos los pokemons consultados
                long_itud=strTypes.size();
                Log.i("elemento:"+type_variable, ""+strTypes.get(type_variable));
                results+=String.valueOf(strTypes.get(type_variable));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            MainActivity.txtDisplay.setText(results);

            mostraImagen(decision);

        }
    }

    public void mostraImagen(int decision){
        if(decision==0){
            // Set main img
            SvgLoader.pluck()
                    .with(MainActivity.act)
                    .load(img, MainActivity.imgPok);

            // Set img types
            for(int i=0; i<strTypes.size(); i++){
                MainActivity.imgType[i].setImageResource(MainActivity.act.getResources().getIdentifier(strTypes.get(i), "drawable", MainActivity.act.getPackageName()));
            }
        }else if(decision==1){
            SvgLoader.pluck()
                    .with(MainActivity.act)
                    .load(img, MainActivity.imgPok);

            MainActivity.imgType[0].setImageResource(MainActivity.act.getResources().getIdentifier(strTypes.get(0),"drawable",MainActivity.act.getPackageName()));
            MainActivity.imgType[1].setImageResource(MainActivity.act.getResources().getIdentifier(strTypes.get(1),"drawable",MainActivity.act.getPackageName()));
        }
    }

}
