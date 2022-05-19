package es.uca.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class VerReservas extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reservas);

        //Referenciamos al RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        //Mejoramos rendimiento con esta configuración
        mRecyclerView.setHasFixedSize(true);

        //Creamos un LinearLayoutManager para gestionar el item.xml creado antes
        mLayoutManager = new LinearLayoutManager(this);
        //Lo asociamos al RecyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);

        drawerLayout = findViewById(R.id.drawer_layout);

        //Invocamos a la tarea asíncrona getAll
        getAll myInvokeTask = new getAll();
        myInvokeTask.execute();

    }

    //Iniciamos la actividad Nueva Reserva al pulsar en el botón correspondiente
    public void nuevaReserva(View view) {
        Intent i = new Intent(this, NuevaReserva.class);
        startActivity(i);
    }

    //Tarea asíncrona que hará una petición GET a nuestra API y mostrará las reservas obtenidas dentro de nuestro RecyclerView
    private class getAll extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            String text;
            HttpURLConnection urlConnection = null;
            try {
                URL urlToRequest = new URL("https://gadeshotel.herokuapp.com/reservas");
                //Realizamos la petición (GET por defecto) a nuestra API
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                //Tomamos los resultados obtenidos en un flujo de entrada
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //Formateamos los resultados obtenidos
                text = new Scanner(in).useDelimiter("\\A").next();
            } catch (Exception e) {
                return e.toString();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            //Devolvemos los resultados obtenidos
            return text;
        }

        @Override
        protected void onPostExecute(String results) {
            //Si existen reservas
            if (results != null) {
                Log.d("data",results);
                //Creamos un ArrayList de Reservas
                ArrayList<Reserva> reservas = new ArrayList<>();
                try {
                    //Creamos un array de objectos JSON a partir de los resultados obtenidos
                    JSONArray jsonArray = new JSONArray(results);

                    //Para cada posición del JSONArray
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Extraemos el objeto JSON almacenado en la posición i del JSONArray
                        JSONObject oneObject = jsonArray.getJSONObject(i);
                        //Tomamos el valor asociado a la clave "tipoHabitacion"
                        String sala = oneObject.getString("tipoHabitacion");
                        //Tomamos el valor asociado a la clave "Nombre"
                        String nombre = oneObject.getString("Nombre");
                        //Tomamos el valor asociado a la clave "_id"
                        String id = oneObject.getString("_id");
                        //Añadimos la reserva a nuestro ArrayList
                        reservas.add(new Reserva(sala,nombre,id));
                    }
                    //Creamos un adapter pasándole todas nuestras reservas
                    mAdapter = new ReservaAdapter(reservas);
                    //Asociamos el adaptador al RecyclerView
                    mRecyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //abrir layout de drawer
    public void ClickMenu(View view) {
        MainActivity.openDrawer(drawerLayout);
    }

    //cerrar layout de drawer
    public void ClickLogo(View view) {
        MainActivity.closeDrawer(drawerLayout);
    }

    //redireccion al MainActivity
    public void ClickHome(View view) {
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    //redireccion a la actividad Salas
    public void ClickSalas(View view) {
        MainActivity.redirectActivity(this, Salas.class);
    }

    //recarga el layout actual
    public void ClickReservas(View view) {
        recreate();
    }

    //Redireccion a la actividad reservas
    public void ClickLocalizacion(View view) {
        MainActivity.redirectActivity(this, MapsActivity.class);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }
}