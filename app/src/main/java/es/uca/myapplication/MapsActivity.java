package es.uca.myapplication;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import es.uca.myapplication.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //inicializamos las variables
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //enlace externo a otra pagina
        TextView text1 = findViewById(R.id.link1);
        TextView text2 = findViewById(R.id.link2);
        TextView text3 = findViewById(R.id.link3);
        text1.setMovementMethod(LinkMovementMethod.getInstance());
        text2.setMovementMethod(LinkMovementMethod.getInstance());
        text3.setMovementMethod(LinkMovementMethod.getInstance());

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //creacion de una marca
        LatLng hotel = new LatLng(36.53080466401778, -6.304734884686058);
        mMap.addMarker(new MarkerOptions().position(hotel).title("Hotel Gades"));

        //configuracion del mapa
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hotel));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
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

    //Redireccion a la actividad reservas
    public void ClickReservas(View view) {
        MainActivity.redirectActivity(this, VerReservas.class);
    }

    //recarga el layout actual
    public void ClickLocalizacion(View view) {
        recreate();
    }

    @Override
    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }

}