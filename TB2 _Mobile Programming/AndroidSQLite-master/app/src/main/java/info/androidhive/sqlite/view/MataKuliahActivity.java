package info.androidhive.sqlite.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.MataKuliah;

public class MataKuliahActivity extends AppCompatActivity implements MataKuliahAdapter.MataKuliahAdapterListener {
    private MataKuliahAdapter mAdapter;
    private List<MataKuliah> mataKuliahList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private TextView textUserCount, textMataKuliahCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mata_kuliah);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Data Mata Kuliah");

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view_mata_kuliah);

        textUserCount = findViewById(R.id.text_user_count);
        textMataKuliahCount = findViewById(R.id.text_mata_kuliah_count);

        db = new DatabaseHelper(this);

        // Mendapatkan jumlah user
        int userCount = db.getUsersCount();
        textUserCount.setText(String.format("Jumlah Mahasiswa: %d", userCount));

        // Mendapatkan jumlah mata kuliah
        int mataKuliahCount = db.getMataKuliahCount();
        textMataKuliahCount.setText(String.format("Jumlah Mata Kuliah: %d", mataKuliahCount));

        mataKuliahList.addAll(db.getAllMataKuliah());

        FloatingActionButton fab = findViewById(R.id.fab_mata_kuliah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMataKuliahDialog(false, null, -1);
            }
        });

        mAdapter = new MataKuliahAdapter(this, mataKuliahList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    //Buat nambahin counter
    @Override
    protected void onResume() {
        super.onResume();

        // Update counts when the activity is resumed (e.g., after adding or deleting data)
        int userCount = db.getUsersCount();
        textUserCount.setText(String.format("Jumlah User: %d", userCount));

        int mataKuliahCount = db.getMataKuliahCount();
        textMataKuliahCount.setText(String.format("Jumlah Mata Kuliah: %d", mataKuliahCount));
    }

    @Override
    public void onEditButtonClicked(MataKuliah mataKuliah, int position) {
        showMataKuliahDialog(true, mataKuliah, position);
    }

    @Override
    public void onDeleteButtonClicked(final MataKuliah mataKuliah, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Mata Kuliah");
        builder.setMessage("Are you sure you want to delete this mata kuliah?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteMataKuliah(mataKuliah);
                mataKuliahList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }



    private void showMataKuliahDialog(final boolean shouldUpdate, final MataKuliah mataKuliah, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.mata_kuliah_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MataKuliahActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNama = view.findViewById(R.id.nama_mata_kuliah);
        final EditText inputKode = view.findViewById(R.id.kode_mata_kuliah);
        final EditText inputSks = view.findViewById(R.id.sks_mata_kuliah);

        if (shouldUpdate && mataKuliah != null) {
            inputNama.setText(mataKuliah.getNamaMataKuliah());
            inputKode.setText(mataKuliah.getKodeMataKuliah());
            inputSks.setText(String.valueOf(mataKuliah.getSksMataKuliah()));
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNama.getText().toString()) || TextUtils.isEmpty(inputKode.getText().toString()) || TextUtils.isEmpty(inputSks.getText().toString())) {
                    Toast.makeText(MataKuliahActivity.this, "Enter all details!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && mataKuliah != null) {
                    // update mata kuliah by it's id
                    updateMataKuliah(inputNama.getText().toString(), inputKode.getText().toString(), Integer.parseInt(inputSks.getText().toString()), position);
                } else {
                    // create new mata kuliah
                    createMataKuliah(inputNama.getText().toString(), inputKode.getText().toString(), Integer.parseInt(inputSks.getText().toString()));
                }
            }
        });
    }

    private void createMataKuliah(String nama, String kode, int sks) {
        long id = db.insertMataKuliah(nama, kode, sks);

        MataKuliah mataKuliah = db.getMataKuliah(id);

        if (mataKuliah != null) {
            mataKuliahList.add(0, mataKuliah);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateMataKuliah(String nama, String kode, int sks, int position) {
        MataKuliah mataKuliah = mataKuliahList.get(position);
        mataKuliah.setNamaMataKuliah(nama);
        mataKuliah.setKodeMataKuliah(kode);
        mataKuliah.setSksMataKuliah(sks);

        db.updateMataKuliah(mataKuliah);

        mataKuliahList.set(position, mataKuliah);
        mAdapter.notifyItemChanged(position);
    }
}
