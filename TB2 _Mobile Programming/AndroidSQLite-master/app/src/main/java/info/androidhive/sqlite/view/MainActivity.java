package info.androidhive.sqlite.view;

import android.content.DialogInterface;
import android.content.Intent; // Tambahkan ini
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.User;
import info.androidhive.sqlite.utils.MyDividerItemDecoration;
import info.androidhive.sqlite.utils.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity {
    private UsersAdapter mAdapter;
    private List<User> userList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noUsersView;
    private DatabaseHelper db;
    private ImageButton btnDeleteAll;
    private TextView textUserCount, textMataKuliahCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Data Mahasiswa");

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noUsersView = findViewById(R.id.empty_notes_view);

        textUserCount = findViewById(R.id.text_user_count);
        textMataKuliahCount = findViewById(R.id.text_mata_kuliah_count);

        db = new DatabaseHelper(this);

        // Mendapatkan jumlah user
        int userCount = db.getUsersCount();
        textUserCount.setText(String.format("Jumlah Mahasiswa: %d", userCount));

        // Mendapatkan jumlah mata kuliah
        int mataKuliahCount = db.getMataKuliahCount();
        textMataKuliahCount.setText(String.format("Jumlah Mata Kuliah: %d", mataKuliahCount));


        userList.addAll(db.getAllUsers());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserDialog(false, null, -1);
            }
        });

        mAdapter = new UsersAdapter(this, userList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnUserClickListener(new UsersAdapter.OnUserClickListener() {
            @Override
            public void onEditClick(int position) {
                showUserDialog(true, userList.get(position), position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteUser(position);
            }
        });

        toggleEmptyUsers();

        // Find and set click listener for the delete all button
        //ImageButton btnDeleteAll = findViewById(R.id.btn_delete_all);
        //btnDeleteAll.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View v) {
             //   deleteAllUsers(); // Call deleteAllUsers() method
            //}
        //});

        // Button to open MataKuliahActivity
        ImageButton btnOpenMataKuliah = findViewById(R.id.btn_open_mata_kuliah);
        btnOpenMataKuliah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MataKuliahActivity.class));
            }
        });

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update counts when the activity is resumed (e.g., after adding or deleting data)
        int userCount = db.getUsersCount();
        textUserCount.setText(String.format("Jumlah Mahasiswa: %d", userCount));

        int mataKuliahCount = db.getMataKuliahCount();
        textMataKuliahCount.setText(String.format("Jumlah Mata Kuliah: %d", mataKuliahCount));
    }

    /**
     * Inserting new user in db
     * and refreshing the list
     */
    private void createUser(String nama, String nomorRegistrasi, String email, String nomorTelepon) {
        // inserting user in db and getting
        // newly inserted user id
        long id = db.insertUser(nama, nomorRegistrasi, email, nomorTelepon);

        // get the newly inserted user from db
        User user = db.getUser(id);

        if (user != null) {
            // adding new user to array list at 0 position
            userList.add(0, user);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyUsers();
        }
    }

    // Inside MainActivity
    private void deleteAllUsers() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete all data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteAllUsers(); // Call the method from DatabaseHelper
                userList.clear(); // Clear the list
                mAdapter.notifyDataSetChanged(); // Notify adapter
                toggleEmptyUsers(); // Update UI
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    /**
     * Updating user in db and updating
     * item in the list by its position
     */
    private void updateUser(String nama, String nomorRegistrasi, String email, String nomorTelepon, int position) {
        User user = userList.get(position);
        // updating user details
        user.setNama(nama);
        user.setNomorRegistrasi(nomorRegistrasi);
        user.setEmail(email);
        user.setNomorTelepon(nomorTelepon);

        // updating user in db
        db.updateUser(user);

        // refreshing the list
        userList.set(position, user);
        mAdapter.notifyItemChanged(position);

        toggleEmptyUsers();
    }

    /**
     * Deleting user from SQLite and removing the
     * item from the list by its position
     */
    private void deleteUser(int position) {
        // deleting the user from db
        db.deleteUser(userList.get(position));

        // removing the user from the list
        userList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyUsers();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 1
     */
    private void showActionsDialog(final int position) {
        CharSequence options[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showUserDialog(true, userList.get(position), position);
                } else {
                    deleteUser(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * user details.
     * when shouldUpdate=true, it automatically displays old user details and changes the
     * button text to UPDATE
     */
    private void showUserDialog(final boolean shouldUpdate, final User user, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.user_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNama = view.findViewById(R.id.nama);
        final EditText inputNomorRegistrasi = view.findViewById(R.id.nomor_registrasi);
        final EditText inputEmail = view.findViewById(R.id.email);
        final EditText inputNomorTelepon = view.findViewById(R.id.nomor_telepon);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_user_title) : getString(R.string.lbl_edit_user_title));

        if (shouldUpdate && user != null) {
            inputNama.setText(user.getNama());
            inputNomorRegistrasi.setText(user.getNomorRegistrasi());
            inputEmail.setText(user.getEmail());
            inputNomorTelepon.setText(user.getNomorTelepon());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // This is intentionally left blank
                    }
                })
                .setNegativeButton("Cancel",
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
                // Show toast message when any field is empty
                if (TextUtils.isEmpty(inputNama.getText().toString()) ||
                        TextUtils.isEmpty(inputNomorRegistrasi.getText().toString()) ||
                        TextUtils.isEmpty(inputEmail.getText().toString()) ||
                        TextUtils.isEmpty(inputNomorTelepon.getText().toString())) {
                    Toast.makeText(MainActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // Check if user is updating user
                if (shouldUpdate && user != null) {
                    // Update user by its id
                    updateUser(inputNama.getText().toString(), inputNomorRegistrasi.getText().toString(),
                            inputEmail.getText().toString(), inputNomorTelepon.getText().toString(), position);
                } else {
                    // Create new user
                    createUser(inputNama.getText().toString(), inputNomorRegistrasi.getText().toString(),
                            inputEmail.getText().toString(), inputNomorTelepon.getText().toString());
                }
            }
        });
    }

    /**
     * Toggling list and empty users view
     */
    private void toggleEmptyUsers() {
        if (db.getUsersCount() > 0) {
            noUsersView.setVisibility(View.GONE);
        } else {
            noUsersView.setVisibility(View.VISIBLE);
        }
    }
}
