package es.uca.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class InfoReserva extends AppCompatActivity implements View.OnClickListener {

    String idReserva;
    Button btnEliminarReserva;
    Button btnEditarReserva;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_reserva);

        //Establecemos un listener del evento onClick en el botón para eliminar una reserva
        btnEliminarReserva = (Button) findViewById(R.id.btnEliminarReserva);
        btnEliminarReserva.setOnClickListener(this);

        //Establecemos un listener del evento onClick en el botón para editar una reserva
        btnEditarReserva = (Button) findViewById(R.id.btnEditarReserva);
        btnEditarReserva.setOnClickListener(this);

        //Tomamos la id que se ha pasado como parámetro al iniciar esta actividad
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idReserva =  extras.getString("idReserva");
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        //Invocamos a la tarea asíncrona getOne
        InfoReserva.getOne myInvokeTask = new InfoReserva.getOne();
        myInvokeTask.execute();
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

    //redireccion a la actividad de salas
    public void ClickSalas(View view) {
        MainActivity.redirectActivity(this, Salas.class);
    }

    //Redireccion a la actividad reservas
    public void ClickReservas(View view) {
        MainActivity.redirectActivity(this, VerReservas.class);
    }

    //redireccion a la actividad localizacion
    public void ClickLocalizacion(View view) {
        MainActivity.redirectActivity(this,MapsActivity.class);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Si el botón en el que hemos hecho click es el de eliminar la reserva, invocamos a la tarea asíncrona deleteOne
            case R.id.btnEliminarReserva:
                InfoReserva.deleteOne myInvokeTask = new InfoReserva.deleteOne();
                myInvokeTask.execute();
                break;
            //Si el botón en el que hemos hecho click es el de editar la reserva, iniciamos la actividad Editar Reserva,
            //pasando la id como parámetro.
            case R.id.btnEditarReserva:
                Intent intent = new Intent(this,EditarReserva.class);
                intent.putExtra("idReserva", idReserva);
                startActivity(intent);
                break;
        }

    }

    //Tarea asíncrona que hará una petición GET a nuestra API y mostrará los detalles
    //de la reserva que tiene la id recibida como parámetro
    private class getOne extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            String text = null;
            HttpURLConnection urlConnection = null;
            try {
                URL urlToRequest = new URL("https://gadeshotel.herokuapp.com/reservas/" + idReserva);
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
            //Si existe la reserva
            if (results != null) {
                Log.d("dataReserva",results.toString());
                //Referenciamos todos los TextView donde se mostrarán los campos de la reserva
                TextView textId = (TextView) findViewById(R.id.textId);
                TextView textTSala = (TextView) findViewById(R.id.textTSala);
                TextView textFEntrada = (TextView) findViewById(R.id.textFEntrada);
                TextView textFSalida = (TextView) findViewById(R.id.textFSalida);
                TextView textNumAdultos = (TextView) findViewById(R.id.textNumAdultos);
                TextView textNumNiños = (TextView) findViewById(R.id.textNumNiños);
                TextView textNombreSala = (TextView) findViewById(R.id.textNombreSala);
                TextView textEmail = (TextView) findViewById(R.id.textEmail);
                TextView textDNI = (TextView) findViewById(R.id.textDNI);
                TextView textTlf = (TextView) findViewById(R.id.textTlf);
                TextView textComentario = (TextView) findViewById(R.id.textComentario);

                try {
                    //Creamos un array de objectos JSON a partir de los resultados obtenidos
                    JSONArray jsonArray = new JSONArray(results);
                    //Tomamos el JSONObject de la primera posición del array
                    JSONObject oneObject = jsonArray.getJSONObject(0);

                    //Tomamos los valores de todos los campos de la reserva a partir de las claves a las que están asociados
                    String idReserva = oneObject.getString("_id");
                    String sala = oneObject.getString("tipoHabitacion");
                    String fEntrada = oneObject.getString("FechaEntrada");
                    String fSalida = oneObject.getString("FechaSalida");
                    String numAdultos = oneObject.getString("NumAdultos");
                    String numNiños = oneObject.getString("NumNiños");
                    String nombre = oneObject.getString("Nombre");
                    String email = oneObject.getString("Email");
                    String dni = oneObject.getString("DNI");
                    String tlf = oneObject.getString("Telefono");
                    String comentario = oneObject.getString("Comentario");

                    //Mostramos los valores obtenidos en los TextView correspondientes
                    textId.setText(idReserva);
                    textTSala.setText(sala);
                    textFEntrada.setText(fEntrada);
                    textFSalida.setText(fSalida);
                    textNumAdultos.setText(numAdultos);
                    textNumNiños.setText(numNiños);
                    textNombreSala.setText(nombre);
                    textEmail.setText(email);
                    textDNI.setText(dni);
                    textTlf.setText(tlf);
                    textComentario.setText(comentario);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //Tarea asíncrona que hará una petición DELETE a nuestra API y borrará la reserva que tiene la id recibida como parámetro
    public class deleteOne extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonObject = new JSONObject();
            try {
                URL urlToRequest = new URL("https://gadeshotel.herokuapp.com/reservas/" + idReserva);
                //Abrimos la conexión con nuestra API
                HttpURLConnection urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                //Establecemos la petición a la API como DELETE
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Accept-Encoding","gzip");
                urlConnection.setRequestProperty("Content-Type","application/json");
                Log.d("dataCode","Response code: " + urlConnection.getResponseCode());

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuilder responseText = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    System.out.println("LINE: "+line);
                    responseText.append(line);
                }
                br.close();
                urlConnection.disconnect();
            } catch (Exception e) {
                jsonObject = null;
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {
                //Mostramos una notificación Snackbar indicando que hemos eliminado la reserva
                Snackbar.make(
                        findViewById(R.id.msj),
                        "La reserva ha sido eliminada correctamente",
                        BaseTransientBottomBar.LENGTH_LONG
                ).show();

                //Si no pulsamos en el botón de la notificación, tras 3 segundos volvemos a la actividad VerReservas igualmente
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        Intent i = new Intent(InfoReserva.this, VerReservas.class);
                        startActivity(i);
                    }
                }, 2000);
            }
            else {
                //Mostramos una notificación Snackbar indicando que ha ocurrido un error
                Snackbar.make(
                        findViewById(R.id.msj),
                        "Ha ocurrido un error y no se ha eliminado la reserva. Inténtelo de nuevo.",
                        BaseTransientBottomBar.LENGTH_SHORT
                ).show();
            }
        }
    }
}