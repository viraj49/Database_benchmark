package tank.viraj.database_benchmark.requery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import io.requery.android.sqlite.DatabaseSource;
import io.requery.sql.SchemaModifier;

public class DataBaseHelper extends DatabaseSource {
    private static int DB_VERSION = 1;
    private static DataBaseHelper sInstance;

    public static void init(Context context, boolean useInMemoryDb) {
        sInstance = new DataBaseHelper(context, useInMemoryDb);
    }

    public static DataBaseHelper getInstance() {
        if (sInstance == null) {
            throw new InstantiationError();
        }
        return sInstance;
    }

    private DataBaseHelper(Context context, boolean useInMemoryDb) {
        super(context, Models.DEFAULT, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void createTables(SQLiteDatabase db) {
        super.onCreate(db);
    }

    void dropTables() {
        new SchemaModifier(getConfiguration()).dropTables();
    }
}