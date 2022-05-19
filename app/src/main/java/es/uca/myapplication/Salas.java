package es.uca.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Salas extends AppCompatActivity {

    //inicializamos las variables
    private URL url = null;
    private Button btnDownload;
    private String filepath = "https://files.fm/down.php?i=qzmzcf9bd";
    private String filename;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salas);

        try {
            initViews();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        setListeners();
        drawerLayout = findViewById(R.id.drawer_layout);
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

    //recarga el layout actual
    public void ClickSalas(View view) {
        recreate();
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

    //asignamos el nombre del archivo al filename
    private void initViews() throws MalformedURLException {
        btnDownload = findViewById(R.id.btnDownload);

        url = new URL(filepath);
        filename = url.getPath();
        filename = filename.substring(filename.lastIndexOf('/') + 1);

    }

    //listener para el boton de descargar
    private void setListeners() {
        btnDownload.setOnClickListener(v -> {
            //descarga del archivo, asignacion de caracteristicas y guardado en el sistema
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url + ""));
            request.setTitle(filename);
            request.setMimeType("application/pdf");
            request.setAllowedOverMetered(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
        });
    }

}