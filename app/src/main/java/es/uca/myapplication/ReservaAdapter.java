package es.uca.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.MyViewHolder> {
    private ArrayList<Reserva> reservas;

    private Context context;

    public ReservaAdapter(ArrayList<Reserva> misReservas) {
        reservas = misReservas;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;

        TextView sala;
        TextView nombre;
        TextView idReserva;
        Button botonInfo;

        //Referenciamos los elementos visuales que tendrá cada item del RecyclerView
        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();
            sala = (TextView) v.findViewById(R.id.sala);
            nombre = (TextView) v.findViewById(R.id.nombre);
            idReserva = (TextView) v.findViewById(R.id.idReserva);
            botonInfo = (Button) v.findViewById(R.id.infoReserva);
        }

        //Establecemos un listener del evento onClick para el botón de cada item
        void setOnClickListeners() {
            botonInfo.setOnClickListener(this);
        }

        //Si hacemos click, iniciaremos la actividad InfoReserva del item correspondiente, pasando la id como parámetro
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.infoReserva) {
                Intent intent = new Intent(context,InfoReserva.class);
                intent.putExtra("idReserva", idReserva.getText());
                context.startActivity(intent);
            }

        }
    }

    //Inflamos el adapter con las vistas de todos los items
    @Override
    public ReservaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder vh = new MyViewHolder((v));
        context = parent.getContext();
        return vh;
    }

    //Establecemos los campos que queremos mostrar de cada item
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.sala.setText(reservas.get(position).getTipoHabitacion());
        holder.nombre.setText(reservas.get(position).getNombre());
        holder.idReserva.setText(reservas.get(position).getId());

        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

}