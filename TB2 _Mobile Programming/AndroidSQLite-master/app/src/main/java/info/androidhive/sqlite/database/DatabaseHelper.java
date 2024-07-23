package info.androidhive.sqlite.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.database.model.MataKuliah;
import info.androidhive.sqlite.database.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "user_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MataKuliah.CREATE_TABLE);
        db.execSQL(User.CREATE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MataKuliah.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // Insert new user
    public long insertUser(String nama, String nomorRegistrasi, String email, String nomorTelepon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_NAMA, nama);
        values.put(User.COLUMN_NOMOR_REGISTRASI, nomorRegistrasi);
        values.put(User.COLUMN_EMAIL, email);
        values.put(User.COLUMN_NOMOR_TELEPON, nomorTelepon);

        long id = db.insert(User.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // Get single user by id
    @SuppressLint("Range")
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_NAMA, User.COLUMN_NOMOR_REGISTRASI, User.COLUMN_EMAIL, User.COLUMN_NOMOR_TELEPON},
                User.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User(
                        cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(User.COLUMN_NAMA)),
                        cursor.getString(cursor.getColumnIndex(User.COLUMN_NOMOR_REGISTRASI)),
                        cursor.getString(cursor.getColumnIndex(User.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(User.COLUMN_NOMOR_TELEPON)));
            }
            cursor.close();
        }
        db.close();
        return user;
    }

    // Get all users
    @SuppressLint("Range")
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + User.TABLE_NAME + " ORDER BY " +
                User.COLUMN_NAMA + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)));
                    user.setNama(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAMA)));
                    user.setNomorRegistrasi(cursor.getString(cursor.getColumnIndex(User.COLUMN_NOMOR_REGISTRASI)));
                    user.setEmail(cursor.getString(cursor.getColumnIndex(User.COLUMN_EMAIL)));
                    user.setNomorTelepon(cursor.getString(cursor.getColumnIndex(User.COLUMN_NOMOR_TELEPON)));

                    users.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        return users;
    }

    // Get users count
    public int getUsersCount() {
        String countQuery = "SELECT * FROM " + User.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    // Update user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_NAMA, user.getNama());
        values.put(User.COLUMN_NOMOR_REGISTRASI, user.getNomorRegistrasi());
        values.put(User.COLUMN_EMAIL, user.getEmail());
        values.put(User.COLUMN_NOMOR_TELEPON, user.getNomorTelepon());

        int rowsAffected = db.update(User.TABLE_NAME, values, User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});

        db.close();
        return rowsAffected;
    }

    // Delete user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(User.TABLE_NAME, User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // Insert a new mata kuliah
    public long insertMataKuliah(String namaMataKuliah, String kodeMataKuliah, int sksMataKuliah) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MataKuliah.COLUMN_NAMA_MATA_KULIAH, namaMataKuliah);
        values.put(MataKuliah.COLUMN_KODE_MATA_KULIAH, kodeMataKuliah);
        values.put(MataKuliah.COLUMN_SKS_MATA_KULIAH, sksMataKuliah);

        long id = db.insert(MataKuliah.TABLE_NAME, null, values);
        db.close();

        return id;
    }

    // Get single mata kuliah by id
    @SuppressLint("Range")
    public MataKuliah getMataKuliah(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        MataKuliah mataKuliah = null;
        Cursor cursor = db.query(MataKuliah.TABLE_NAME,
                new String[]{MataKuliah.COLUMN_ID, MataKuliah.COLUMN_NAMA_MATA_KULIAH, MataKuliah.COLUMN_KODE_MATA_KULIAH, MataKuliah.COLUMN_SKS_MATA_KULIAH},
                MataKuliah.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mataKuliah = new MataKuliah(
                        cursor.getInt(cursor.getColumnIndex(MataKuliah.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(MataKuliah.COLUMN_NAMA_MATA_KULIAH)),
                        cursor.getString(cursor.getColumnIndex(MataKuliah.COLUMN_KODE_MATA_KULIAH)),
                        cursor.getInt(cursor.getColumnIndex(MataKuliah.COLUMN_SKS_MATA_KULIAH)));
            }
            cursor.close();
        }
        db.close();
        return mataKuliah;
    }

    // Get all mata kuliah
    @SuppressLint("Range")
    public List<MataKuliah> getAllMataKuliah() {
        List<MataKuliah> mataKuliahList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + MataKuliah.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    MataKuliah mataKuliah = new MataKuliah();
                    mataKuliah.setId(cursor.getInt(cursor.getColumnIndex(MataKuliah.COLUMN_ID)));
                    mataKuliah.setNamaMataKuliah(cursor.getString(cursor.getColumnIndex(MataKuliah.COLUMN_NAMA_MATA_KULIAH)));
                    mataKuliah.setKodeMataKuliah(cursor.getString(cursor.getColumnIndex(MataKuliah.COLUMN_KODE_MATA_KULIAH)));
                    mataKuliah.setSksMataKuliah(cursor.getInt(cursor.getColumnIndex(MataKuliah.COLUMN_SKS_MATA_KULIAH)));

                    mataKuliahList.add(mataKuliah);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        return mataKuliahList;
    }

    // Get count of all mata kuliah
    public int getMataKuliahCount() {
        String countQuery = "SELECT * FROM " + MataKuliah.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    // Update mata kuliah
    public int updateMataKuliah(MataKuliah mataKuliah) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MataKuliah.COLUMN_NAMA_MATA_KULIAH, mataKuliah.getNamaMataKuliah());
        values.put(MataKuliah.COLUMN_KODE_MATA_KULIAH, mataKuliah.getKodeMataKuliah());
        values.put(MataKuliah.COLUMN_SKS_MATA_KULIAH, mataKuliah.getSksMataKuliah());

        int rowsAffected = db.update(MataKuliah.TABLE_NAME, values, MataKuliah.COLUMN_ID + " = ?",
                new String[]{String.valueOf(mataKuliah.getId())});

        db.close();
        return rowsAffected;
    }

    // Delete mata kuliah
    public void deleteMataKuliah(MataKuliah mataKuliah) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MataKuliah.TABLE_NAME, MataKuliah.COLUMN_ID + " = ?",
                new String[]{String.valueOf(mataKuliah.getId())});
        db.close();
    }

    // Delete all users
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(User.TABLE_NAME, null, null);
        db.close();
    }
}
