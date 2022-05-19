package es.uca.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class NuevaReserva extends AppCompatActivity {

    String txtSpinnerSalas;
    String txtSpinnerAdultos;
    String txtSpinnerNinios;

    //Crea un objeto de la clase reserva
    Reserva reserva = new Reserva();


    DatePickerDialog datePickerDialogE;
    DatePickerDialog datePickerDialogS;
    TextInputEditText btnFechaEntrada;
    TextInputEditText btnFechaSalida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_reserva);

        //Llamamos a la función initDatePickerE para manejar el campo de selección de la fecha de entrada
        initDatePickerE();
        btnFechaEntrada = findViewById(R.id.btnFechaEntrada);
        //Asignamos al campo fecha de entrada la fecha actual
        btnFechaEntrada.setText(getTodaysDate());

        //Llamamos a la función initDatePickerS para manejar el campo de selección de la fecha de salida
        initDatePickerS();
        btnFechaSalida = findViewById(R.id.btnFechaSalida);
        //Asignamos al campo fecha de salida la fecha del día siguiente al actual
        btnFechaSalida.setText(getTomorrowsDate());

        //Tomamos las opciones del Spinner del array de String definido en strings.xml
        Resources res = getResources();
        String[] s = res.getStringArray(R.array.tiposHabitacion);

        //Los pasamos al Spinner mediante un ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tiposHabitacion, android.R.layout.simple_dropdown_item_1line);
        AutoCompleteTextView actvSalas = findViewById(R.id.itemSpinnerSalas);
        actvSalas.setAdapter(adapter);

        //Creamos un listener del evento OnItemSelected para el spinner
        actvSalas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Si selecciona alguna mostramos en una notificación Toast cuál ha seleccionado
                Toast.makeText(NuevaReserva.this, s[i]+ " seleccionada!", Toast.LENGTH_SHORT).show();
                txtSpinnerSalas = (String) adapterView.getItemAtPosition(i);
            }
        });

        //Tomamos las opciones del Spinner del array de String definido en strings.xml
        Resources res2 = getResources();
        String[] s2 = res2.getStringArray(R.array.numAdultos);

        //Los pasamos al Spinner mediante un ArrayAdapter
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.numAdultos, android.R.layout.simple_dropdown_item_1line);
        AutoCompleteTextView actvAdultos = findViewById(R.id.itemSpinnerAdultos);
        actvAdultos.setAdapter(adapter2);

        //Creamos un listener del evento OnItemSelected para el spinner
        actvAdultos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Si selecciona alguna mostramos en una notificación Toast cuál ha seleccionado

                Toast.makeText(NuevaReserva.this, "El número de adultos seleccionado es " + s2[i], Toast.LENGTH_SHORT).show();
                txtSpinnerAdultos = (String) adapterView.getItemAtPosition(i);

            }
        });

        //Tomamos las opciones del Spinner del array de String definido en strings.xml
        Resources res3 = getResources();
        String[] s3 = res3.getStringArray(R.array.numNiños);

        //Los pasamos al Spinner mediante un ArrayAdapter
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.numNiños, android.R.layout.simple_dropdown_item_1line);
        AutoCompleteTextView actvNinios = findViewById(R.id.itemSpinnerNinios);
        actvNinios.setAdapter(adapter3);

        //Creamos un listener del evento OnItemSelected para el spinner
        actvNinios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Si selecciona alguna mostramos en una notificación Toast cuál ha seleccionado

                txtSpinnerNinios = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(NuevaReserva.this, "El número de niños seleccionado es " + s3[i], Toast.LENGTH_SHORT).show();

            }
        });

    }

    //Establece los atributos de la reserva a partir de los campos introducidos por el usuario
    //Invoca también a la tarea asíncrona postReserva
    public void crearReserva(View view) {
        //Referenciamos todos los TextInputEditText donde se introducirán los campos de la reserva
        TextInputEditText tvNombre = findViewById(R.id.EditTextNombre);
        TextInputEditText tvEmail = findViewById(R.id.EditTextEmail);
        TextInputEditText tvTlf = findViewById(R.id.EditTextTlf);
        TextInputEditText tvDni = findViewById(R.id.EditTextDni);
        TextInputEditText tvComentario = findViewById(R.id.EditTextComentario);

        //Si la validación del formulario es correcta
        if (validacionForm()) {
            //Se establecen los atributos de la reserva a partir de de los EditText,los Spinner y los selectores de fecha
            reserva.setTipoHabitacion(txtSpinnerSalas);
            reserva.setFechaEntrada(btnFechaEntrada.getText().toString());
            reserva.setFechaSalida(btnFechaSalida.getText().toString());
            reserva.setNumAdultos(Integer.valueOf(txtSpinnerAdultos));
            reserva.setNumNiños(Integer.valueOf(txtSpinnerNinios));
            reserva.setNombre(tvNombre.getText().toString());
            reserva.setEmail(tvEmail.getText().toString());
            reserva.setTelefono(Integer.valueOf(tvTlf.getText().toString()));
            reserva.setDni(tvDni.getText().toString());
            reserva.setComentario(tvComentario.getText().toString());

            //Invocamos a la tarea asíncrona postReserva
            NuevaReserva.postReserva myInvokeTask = new NuevaReserva.postReserva();
            myInvokeTask.execute();
        }
    }

    //Función que valida todos los campos del formulario, mostrando si no los errores correspondientes
    public boolean validacionForm() {
        boolean valido = true;

        //Referenciamos todos los TextInputLayout del formulario
        TextInputLayout tilNombre = findViewById(R.id.LayoutNombre);
        TextInputLayout tilEmail = findViewById(R.id.LayoutEmail);
        TextInputLayout tilDni = findViewById(R.id.LayoutDni);
        TextInputLayout tilTlf = findViewById(R.id.LayoutTlf);
        TextInputLayout tilSalas = findViewById(R.id.spinnerSalas);
        TextInputLayout tilNumAdultos = findViewById(R.id.spinnerAdultos);
        TextInputLayout tilNumNinios = findViewById(R.id.spinnerNinios);
        TextInputLayout tilfEntrada = findViewById(R.id.LayoutfE);
        TextInputLayout tilfSalida = findViewById(R.id.LayoutfS);

        //Referenciamos los TextInputEditText
        TextInputEditText tvNombre = findViewById(R.id.EditTextNombre);
        TextInputEditText tvEmail = findViewById(R.id.EditTextEmail);
        TextInputEditText tvDni = findViewById(R.id.EditTextDni);
        TextInputEditText tvTlf = findViewById(R.id.EditTextTlf);

        //Extraemos de cada campo del formulario lo introducido por el usuario
        String nombre= tvNombre.getText().toString();
        String email = tvEmail.getText().toString();
        String dni = tvDni.getText().toString();
        String tlf = tvTlf.getText().toString();
        String fEntrada = btnFechaEntrada.getText().toString();
        String fSalida = btnFechaSalida.getText().toString();

        //Validamos el nombre, comprobando que está compuesto solo por letras y espacios y tiene menos de 30 caracteres
        Pattern patron = Pattern.compile("^[A-Za-zñÑ-áéíóúÁÉÍÓÚü ]+$");
        if (!(patron.matcher(nombre).matches()) || nombre.length() > 30 || nombre.length() == 0) {
            valido = false;
            tilNombre.setError("Nombre inválido");
        }
        else {
            tilNombre.setError(null);
        }

        //Validamos el email, comprobando que tiene el formato correcto
        patron = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
        if (!(patron.matcher(email).matches()) || email.length() > 30 || email.length() == 0) {
            valido = false;
            tilEmail.setError("Email inválido");
        }
        else {
            tilEmail.setError(null);
        }

        //Comprobamos que el DNI introducido es válido
        patron = Pattern.compile("^\\d{8}[A-Z]$");
        char[] letras = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E', 'T'};
        if (!(patron.matcher(dni).matches()) || dni.charAt(8) != letras[Integer.parseInt(dni.substring(0,8)) % 23]) {
            tilDni.setError("Dni inválido");
            valido = false;
        }
        else {
            tilDni.setError(null);
        }

        //Comprobamos que el teléfono es válido, compuesto por 9 dígitos
        patron = Pattern.compile("^\\d{9}$");
        if (!(patron.matcher(tlf).matches())) {
            valido = false;
            tilTlf.setError("Teléfono inválido");
        }
        else {
            tilTlf.setError(null);
        }

        //Comprobamos que se ha seleccionado un tipo de habitación
        if (txtSpinnerSalas == null) {
            valido = false;
            tilSalas.setError("Debes seleccionar un tipo de habitación");
        }
        else {
            tilSalas.setError(null);
        }

        //Comprobamos que se ha seleccionado un número de niños
        if (txtSpinnerNinios == null) {
            valido = false;
            tilNumNinios.setError("Debes seleccionar un número de niños");
        }
        else {
            tilNumNinios.setError(null);
        }

        //Comprobamos que se ha seleccionado un número de adultos
        //Además, comprobamos que si se había seleccionado una habitación individual, el número de adultos es 1
        if (txtSpinnerAdultos == null) {
            valido = false;
            tilNumAdultos.setError("Debes seleccionar un número de adultos");
        }
        else if (txtSpinnerSalas != null) {
            if ((txtSpinnerSalas.equals("Habitación simple") && Integer.parseInt(txtSpinnerAdultos) > 1) ||
                    (txtSpinnerSalas.equals("Suite simple") && Integer.parseInt(txtSpinnerAdultos) > 1)) {
                valido = false;
                tilNumAdultos.setError("No puedes reservar una habitación o suite simple para más de un adulto");
            }
        }
        else {
            tilNumAdultos.setError(null);
        }

        //Comprobamos que las fechas de entrada y salida son válidas
        SimpleDateFormat dateFormat = new SimpleDateFormat ("dd / MM / yyyy");
        try {
            Date datefEntrada = dateFormat.parse(fEntrada);
            Date datefSalida = dateFormat.parse(fSalida);
            Date datefHoy = dateFormat.parse(getTodaysDate());
            Date datefMannana = dateFormat.parse(getTomorrowsDate());

            if (datefHoy.after(datefEntrada)) {
                valido = false;
                tilfEntrada.setError("La fecha de entrada seleccionada no es válida");
            }
            else if (datefSalida.after(datefMannana) && !datefEntrada.before(datefSalida)) {
                valido = false;
                tilfEntrada.setError("La fecha de entrada debe ser anterior a la de salida");
            }
            else {
                tilfEntrada.setError(null);
            }

            if (datefMannana.after(datefSalida)) {
                valido = false;
                tilfSalida.setError("La fecha de salida seleccionada no es válida");
            }
            else if (datefEntrada.after(datefHoy) && !datefEntrada.before(datefSalida)) {
                valido = false;
                tilfSalida.setError("La fecha de salida debe ser posterior a la de entrada");
            }
            else {
                tilfSalida.setError(null);
            }

        } catch (ParseException e) {
            Log.d("funcionaParse", e.toString());
        }

        Log.d("validoform", String.valueOf(valido));
        return valido;
    }

    public class postReserva extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonResult = new JSONObject();
            try {
                URL urlToRequest = new URL("https://gadeshotel.herokuapp.com/reservas/");
                //Abrimos la conexión con nuestra API
                HttpURLConnection urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                //Establecemos la petición a la API como POST
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Accept-Encoding","gzip");
                urlConnection.setRequestProperty("Content-Type","application/json");

                OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);

                //Creamos un JSONObject
                JSONObject jsonObject = new JSONObject();

                //Introducimos en el JSONObject las claves y valores a partir de los atributos de la reserva antes creada
                jsonObject.put("tipoHabitacion", reserva.getTipoHabitacion());
                jsonObject.put("FechaEntrada", reserva.getFechaEntrada());
                jsonObject.put("FechaSalida", reserva.getFechaSalida());
                jsonObject.put("NumAdultos", reserva.getNumAdultos());
                jsonObject.put("NumNiños", reserva.getNumNiños());
                jsonObject.put("Nombre", reserva.getNombre());
                jsonObject.put("Email", reserva.getEmail());
                jsonObject.put("DNI", reserva.getDni());
                jsonObject.put("Telefono", reserva.getTelefono());
                jsonObject.put("Comentario", reserva.getComentario());


                //Introduce en el flujo de salida el JSONObject creado
                osw.write(jsonObject.toString());
                osw.flush();
                osw.close();

                BufferedReader in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder html = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    html.append(inputLine);
                }
                in.close();
            } catch (Exception e) {
                jsonResult = null;
                e.printStackTrace();
            }
            //Devuelve la respuesta
            return jsonResult;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (result != null) {
                //Mostramos una notificación Toast indicando que hemos creado la reserva
                Toast.makeText(NuevaReserva.this,"La reserva ha sido creada correctamente",Toast.LENGTH_LONG).show();

                //Volvemos a la actividad Ver Reservas
                Intent i = new Intent(NuevaReserva.this, VerReservas.class);
                startActivity(i);
            }
            else {
                //Mostramos una notificación Toast indicando que ha ocurrido un error
                Toast.makeText(NuevaReserva.this,"Ha ocurrido un error y no se ha creado la reserva. Inténtelo de nuevo.",Toast.LENGTH_LONG).show();
            }
        }
    }

    //Devuelve el día actual
    private String getTodaysDate() {

        //Obtenemos un objeto de la clase Calendar
        Calendar cal = Calendar.getInstance();

        //Tomamos el año del calendario
        int year = cal.get(Calendar.YEAR);

        //Tomamos el mes del calendario y le sumamos uno ya que en la clase Calendar enero es el mes 0 y diciembre el 11
        int month = cal.get(Calendar.MONTH);
        month = month + 1;

        //Tomamos el día del mes del calendario
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //Devuelve el resultado de la función makeDateString, que formatea la fecha obtenida
        return makeDateString(day,month,year);

    }

    //Devuelve el día siguiente al actual
    private String getTomorrowsDate() {

        //Obtenemos un objeto de la clase Calendar
        Calendar cal = Calendar.getInstance();

        //Tomamos el año del calendario
        int year = cal.get(Calendar.YEAR);

        //Tomamos el mes del calendario y le sumamos uno ya que en la clase Calendar enero es el mes 0 y diciembre el 11
        int month = cal.get(Calendar.MONTH);
        month = month + 1;

        //Tomamos el día del mes del calendario y le sumamos uno ya que nos interesa el día siguiente al actual
        int day = cal.get(Calendar.DAY_OF_MONTH);
        day = day + 1;

        //Devuelve el resultado de la función makeDateString, que formatea la fecha obtenida
        return makeDateString(day,month,year);

    }

    //Maneja la selección de la fecha de entrada
    private void initDatePickerE() {

        //Creamos un listener que controlará el evento Date Set (seleccionar una fecha)
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override

            //Si se ha seleccionado una fecha
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Le sumamos uno al mes ya que en la clase Calendar enero es el mes 0 y diciembre el 11
                month = month + 1;

                //Formatea la fecha de entrada, y muestra en el campo la fecha elegida.
                //Además, establecemos la fecha de salida en el día siguiente
                String date = makeDateString(day, month, year);
                String dateS = makeDateString(day+1,month,year);
                btnFechaEntrada.setText(date);
                btnFechaSalida.setText(dateS);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //Creamos un selector de fecha de entrada al que le pasamos el listener creado y los campos año, mes y día
        datePickerDialogE = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    //Da a una fecha el formato que queremos mostrar en el campo del formulario
    private String makeDateString(int day, int month, int year)  {
        return day + " / " + month + " / " + year;
    }

    //Abre el selector de fecha de entrada al clickear en el campo
    public void openDatePickerE(View view) {
        datePickerDialogE.show();
    }

    //Maneja la selección de la fecha de salida
    private void initDatePickerS() {

        //Creamos un listener que controlará el evento Date Set (seleccionar una fecha)
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //Le sumamos uno al mes ya que en la clase Calendar enero es el mes 0 y diciembre el 11
                month = month + 1;

                //Formatea la fecha de salida, y muestra en el campo la fecha elegida.
                String date = makeDateString(day, month, year);
                btnFechaSalida.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //Creamos un selector de fecha de salida al que le pasamos el listener creado y los campos año, mes y día
        datePickerDialogS = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    //Abre el selector de fecha de salida al clickear en el campo
    public void openDatePickerS(View view) {
        datePickerDialogS.show();
    }
}