package tank.viraj.database_benchmark.sqlite;

import android.content.ContentValues;
import android.provider.BaseColumns;

public class Message {
    static final String TABLE_NAME = "message";
    static final String INT_FIELD = "int_filed";
    static final String LONG_FIELD = "long_filed";
    static final String DOUBLE_FIELD = "double_filed";
    static final String STRING_FIELD = "string_filed";

    public int intField;
    public long longField;
    public double doubleField;
    public String stringField;

    static final String[] PROJECTION = new String[]{INT_FIELD, LONG_FIELD, DOUBLE_FIELD, STRING_FIELD};

    static final String CreateTable =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY ," +
                    INT_FIELD + " INTEGER, " +
                    LONG_FIELD + " REAL, " +
                    DOUBLE_FIELD + " REAL, " +
                    STRING_FIELD + " TEXT );";

    static final String DropTable = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

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