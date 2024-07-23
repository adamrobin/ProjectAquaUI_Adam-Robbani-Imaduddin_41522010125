package info.androidhive.sqlite.database.model;

public class MataKuliah {
    public static final String TABLE_NAME = "mata_kuliah";


    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA_MATA_KULIAH = "nama_mata_kuliah";
    public static final String COLUMN_KODE_MATA_KULIAH = "kode_mata_kuliah";
    public static final String COLUMN_SKS_MATA_KULIAH = "sks_mata_kuliah";


    private int id;
    private String namaMataKuliah;
    private String kodeMataKuliah;
    private int sksMataKuliah;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAMA_MATA_KULIAH + " TEXT,"
                    + COLUMN_KODE_MATA_KULIAH + " TEXT,"
                    + COLUMN_SKS_MATA_KULIAH + " INTEGER"
                    + ")";

    public MataKuliah() {
    }

    public MataKuliah(int id, String namaMataKuliah, String kodeMataKuliah, int sksMataKuliah) {
        this.id = id;
        this.namaMataKuliah = namaMataKuliah;
        this.kodeMataKuliah = kodeMataKuliah;
        this.sksMataKuliah = sksMataKuliah;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaMataKuliah() {
        return namaMataKuliah;
    }

    public void setNamaMataKuliah(String namaMataKuliah) {
        this.namaMataKuliah = namaMataKuliah;
    }

    public String getKodeMataKuliah() {
        return kodeMataKuliah;
    }

    public void setKodeMataKuliah(String kodeMataKuliah) {
        this.kodeMataKuliah = kodeMataKuliah;
    }

    public int getSksMataKuliah() {
        return sksMataKuliah;
    }

    public void setSksMataKuliah(int sksMataKuliah) {
        this.sksMataKuliah = sksMataKuliah;
    }
}
