package info.androidhive.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.database.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2; // Increment version for schema changes

    // Database Name
    private static final String DATABASE_NAME = "user_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create users table
        db.execSQL(User.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);

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
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_NAMA, User.COLUMN_NOMOR_REGISTRASI, User.COLUMN_EMAIL, User.COLUMN_NOMOR_TELEPON},
                User.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(
                cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_NAMA)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_NOMOR_REGISTRASI)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_NOMOR_TELEPON)));

        cursor.close();

        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + User.TABLE_NAME + " ORDER BY " +
                User.COLUMN_NAMA + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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

        db.close();

        return users;
    }

    // Get users count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + User.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

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

        return db.update(User.TABLE_NAME, values, User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Delete user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(User.TABLE_NAME, User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
}
