package com.simona.firebasetestecheckbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptorMotivareAbsente extends  RecyclerView.Adapter<AdaptorMotivareAbsente.ClasaViewHolderAbsente> {

    Context context;
    ArrayList<Absente> sirAbsente;
    InterfataMotivareAbsente interfataMotivareAbsente;

    public AdaptorMotivareAbsente(Context context, ArrayList<Absente> sirAbsente, InterfataMotivareAbsente interfataMotivareAbsente) {
        this.context = context;
        this.sirAbsente = sirAbsente;
        this.interfataMotivareAbsente = interfataMotivareAbsente;
    }

    public class ClasaViewHolderAbsente extends RecyclerView.ViewHolder{

        TextView numeTV, idTV, materieTV;
        CheckBox checkBox;
        public ClasaViewHolderAbsente(@NonNull View itemView) {
            super(itemView);
            numeTV = itemView.findViewById(R.id.numeTextView);
            idTV = itemView.findViewById(R.id.idAbsentaTextView);
            materieTV = itemView.findViewById(R.id.materieTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }


    @NonNull
    @Override
    public ClasaViewHolderAbsente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rand_absenta, parent, false);
        return new ClasaViewHolderAbsente(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClasaViewHolderAbsente holder, int position) {
        Absente absentaCurenta = sirAbsente.get(position);
        holder.numeTV.setText(absentaCurenta.getNumele());
        holder.idTV.setText(absentaCurenta.getIdulAbse());
        holder.materieTV.setText(absentaCurenta.getMateriaAbs());

        if (absentaCurenta.getStatus().equals("MOTI")){
            holder.checkBox.setChecked(true);
            holder.checkBox.setText("motivata");
        } else {
            holder.checkBox.setChecked(false);
            holder.checkBox.setText("NEmoti");
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    interfataMotivareAbsente.motiveazaAbsenta(position);
                } else {
                    interfataMotivareAbsente.neMotiveazaAbsenta(position);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sirAbsente.size();
    }

}
