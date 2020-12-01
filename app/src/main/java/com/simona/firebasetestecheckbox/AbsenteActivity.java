package com.simona.firebasetestecheckbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class AbsenteActivity extends AppCompatActivity implements InterfataMotivareAbsente {

    Button motiveazaTotBtn;
    String clasaLogata = "0A";
    RecyclerView rv;
    AdaptorMotivareAbsente mAdapter;
    ArrayList<Absente> sirAbsente;
    ArrayList<String> sirIduriTotiEleviiDinClasa;
    int contor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absente);

        init();
        incarcaAbsenteleDinFirebase();
        incarcaIduriTotiEleviiDinClasa();
        actiuneBtn();

    }

    private void incarcaAbsenteleDinFirebase() {

        final DatabaseReference incarcaAbsente = FirebaseDatabase.getInstance().getReference(clasaLogata).child("toateAbsentele");
        incarcaAbsente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contor++;
                sirAbsente.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Absente acurent = ds.getValue(Absente.class);
                    sirAbsente.add(acurent);
                    Log.i("incarcarea " + contor, "; " + acurent.getIdulAbse() + "; status= " + acurent.getStatus());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void incarcaIduriTotiEleviiDinClasa() {
        DatabaseReference incarcaIduriTotiElevii = FirebaseDatabase.getInstance().getReference(clasaLogata).child("totiElevii");
        incarcaIduriTotiElevii.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sirIduriTotiEleviiDinClasa.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    sirIduriTotiEleviiDinClasa.add(ds.getKey());
                    Log.i("marimeSirID= ", sirIduriTotiEleviiDinClasa.size() + "");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void actiuneBtn() {
        motiveazaTotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AbsenteActivity.this, "clickat pt moti tot", LENGTH_SHORT).show();
                final DatabaseReference motiveazaTotInTabelGeneral = FirebaseDatabase.getInstance().getReference(clasaLogata).child("toateAbsentele");
                motiveazaTotInTabelGeneral.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Absente aCurenta = ds.getValue(Absente.class);
                            if (aCurenta.getStatus().equals("NE")) {
                                //  Log.i("NEMOT = ", aCurenta.getIdulAbse());
                                String idul = aCurenta.getIdulAbse();
                                Absente amotivata = new Absente(aCurenta.getIdulAbse(), aCurenta.getDataAbs(),
                                        aCurenta.getMateriaAbs(), aCurenta.getIdul(), "MOTI", aCurenta.getNumele());
                                motiveazaTotInTabelGeneral.child(idul).setValue(amotivata);

                                // AM INCERCAT CU ASTA, DAR NU MERGE
                                if (!rv.isComputingLayout()) {
//                                    mAdapter.notifyItemChanged(pozi, absCurenta);
                                    mAdapter.notifyDataSetChanged();

                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                for (int i = 0; i < sirIduriTotiEleviiDinClasa.size(); i++) {
                    String idElevCurent = sirIduriTotiEleviiDinClasa.get(i);
                    final DatabaseReference motiveazaInTabelulElevuluiDedicat = FirebaseDatabase.getInstance().getReference(clasaLogata).child("totiElevii")
                            .child(idElevCurent).child("absentele");
                    motiveazaInTabelulElevuluiDedicat.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.getKey().equals("nimic")) {
                                    continue;
                                } else {
                                    Absente aCu = ds.getValue(Absente.class);
                                    if (aCu.getStatus().equals("MOTI")) {
                                        continue;
                                    } else {
                                        Absente amotivata = new Absente(aCu.getIdulAbse(), aCu.getDataAbs(),
                                                aCu.getMateriaAbs(), aCu.getIdul(), "MOTI", aCu.getNumele());
                                        motiveazaInTabelulElevuluiDedicat.child(aCu.getIdulAbse()).setValue(amotivata);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
    }


    @Override
    public void motiveazaAbsenta(int pozi) {
        Absente absCurenta = sirAbsente.get(pozi);
        //   Log.i("curenta=", absCurenta.getIdulAbse());
        String idElevCurent = absCurenta.getIdul();
        final String idAbsCurnta = absCurenta.getIdulAbse();
        DatabaseReference motiveazaInTabelulCuToateAbsentele = FirebaseDatabase.getInstance().getReference(clasaLogata).child("toateAbsentele").child(idAbsCurnta);
        Absente absMotivata = new Absente(idAbsCurnta, absCurenta.getDataAbs(), absCurenta.getMateriaAbs(), absCurenta.getIdul(),
                "MOTI", absCurenta.getNumele());
        motiveazaInTabelulCuToateAbsentele.setValue(absMotivata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AbsenteActivity.this, "motivat-o in tabel general " + idAbsCurnta, LENGTH_SHORT).show();
            }
        });
        DatabaseReference motiveazaInTabelulElevuluiDedicat = FirebaseDatabase.getInstance().getReference(clasaLogata).child("totiElevii")
                .child(idElevCurent).child("absentele").child(idAbsCurnta);
        motiveazaInTabelulElevuluiDedicat.setValue(absMotivata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AbsenteActivity.this, "motivat-o in tabel elev " + idAbsCurnta, LENGTH_SHORT).show();
            }
        });

        if (!rv.isComputingLayout()) {
            mAdapter.notifyItemChanged(pozi, absCurenta);
        }
    }

    @Override
    public void neMotiveazaAbsenta(int pozi) {
        Absente absCurenta = sirAbsente.get(pozi);
        // Log.i("curenta=", absCurenta.getIdulAbse());
        String idElevCurent = absCurenta.getIdul();
        final String idAbsCurnta = absCurenta.getIdulAbse();
        DatabaseReference neMotiveaza = FirebaseDatabase.getInstance().getReference(clasaLogata).child("toateAbsentele").child(idAbsCurnta);
        Absente absMotivata = new Absente(idAbsCurnta, absCurenta.getDataAbs(), absCurenta.getMateriaAbs(), idElevCurent,
                "NE", absCurenta.getNumele());

        neMotiveaza.setValue(absMotivata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AbsenteActivity.this, "NEmotivat-o in tabel general " + idAbsCurnta, LENGTH_SHORT).show();
            }
        });

        //  //AM ADAUGAT ASTA
        //  absCurenta.setStatus("NE");
        DatabaseReference neMotiveazaInTabelulElevuluiDedicat = FirebaseDatabase.getInstance().getReference(clasaLogata).child("totiElevii")
                .child(idElevCurent).child("absentele").child(idAbsCurnta);
        neMotiveazaInTabelulElevuluiDedicat.setValue(absMotivata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AbsenteActivity.this, "am NEmotivat-o in elev " + idAbsCurnta, LENGTH_SHORT).show();
            }
        });
        //  absCurenta.setMotivata(true);
        // Toast.makeText(DiriginteActivity.this, "am NEmotivat-o " + idAbsCurnta, LENGTH_SHORT).show();
//        mAdapter.notifyItemChanged(pozi, absCurenta);
        if (!rv.isComputingLayout()) {
            mAdapter.notifyItemChanged(pozi, absCurenta);
        }

    }

    private void init() {
        sirAbsente = new ArrayList<>();
        rv = findViewById(R.id.listaAbsenteRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AdaptorMotivareAbsente(this, sirAbsente, this);
        rv.setAdapter(mAdapter);
        motiveazaTotBtn = findViewById(R.id.button2);
        sirIduriTotiEleviiDinClasa = new ArrayList<>();
    }

}