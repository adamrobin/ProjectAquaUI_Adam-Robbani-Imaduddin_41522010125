package info.androidhive.sqlite.database.model;

public class User {
    public static final String TABLE_NAME = "users";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_NOMOR_REGISTRASI = "nomor_registrasi";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NOMOR_TELEPON = "nomor_telepon";

    private int id;
    private String nama;
    private String nomorRegistrasi;
    private String email;
    private String nomorTelepon;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAMA + " TEXT NOT NULL,"
                    + COLUMN_NOMOR_REGISTRASI + " TEXT UNIQUE NOT NULL,"
                    + COLUMN_EMAIL + " TEXT NOT NULL,"
                    + COLUMN_NOMOR_TELEPON + " TEXT NOT NULL"
                    + ")";

    public User() {
    }

    public User(int id, String nama, String nomorRegistrasi, String email, String nomorTelepon) {
        this.id = id;
        this.nama = nama;
        this.nomorRegistrasi = nomorRegistrasi;
        this.email = email;
        this.nomorTelepon = nomorTelepon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomorRegistrasi() {
        return nomorRegistrasi;
    }

    public void setNomorRegistrasi(String nomorRegistrasi) {
        this.nomorRegistrasi = nomorRegistrasi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }
}
