package tank.viraj.database_benchmark.sqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Message {
    public static final String TABLE_NAME = "message";
    public static final String INT_FIELD = "int_filed";
    public static final String LONG_FIELD = "long_filed";
    public static final String DOUBLE_FIELD = "double_filed";
    public static final String STRING_FIELD = "string_filed";

    public int intField;
    public long longField;
    public double doubleField;
    public String stringField;

    public static final String[] PROJECTION = new String[]{INT_FIELD, LONG_FIELD, DOUBLE_FIELD, STRING_FIELD};

    public static void createTable(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("CREATE TABLE '" + TABLE_NAME +
                "' ('" + BaseColumns._ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '" +
                INT_FIELD + "' INTEGER, '" +
                LONG_FIELD + "' REAL, '" +
                DOUBLE_FIELD + "' REAL, '" +
                STRING_FIELD + "' TEXT );");
    }

    public static void dropTable(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("DROP TABLE '" + TABLE_NAME + "';");
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public void setLongField(long longField) {
        this.longField = longField;
    }

    public void setDoubleField(double doubleField) {
        this.doubleField = doubleField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public int getIntField() {
        return intField;
    }

    public long getLongField() {
        return longField;
    }

    public double getDoubleField() {
        return doubleField;
    }

    public String getStringField() {
        return stringField;
    }

    ContentValues prepareForInsert() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(INT_FIELD, intField);
        contentValues.put(LONG_FIELD, longField);
        contentValues.put(DOUBLE_FIELD, doubleField);
        contentValues.put(STRING_FIELD, stringField);
        return contentValues;
    }
}