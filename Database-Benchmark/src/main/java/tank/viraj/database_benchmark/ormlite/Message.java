package tank.viraj.database_benchmark.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

@DatabaseTable(tableName = "message")
public class Message {
    @DatabaseField(id = true)
    private int intField;

    @DatabaseField
    private long longField;

    @DatabaseField
    private double doubleField;

    @DatabaseField
    private String stringField;

    private static Dao<Message, Integer> sDao;

    public static Dao<Message, Integer> getDao() throws SQLException {
        if (sDao == null) {
            sDao = DataBaseHelper.getInstance().getDao(Message.class);
        }
        return sDao;
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
}