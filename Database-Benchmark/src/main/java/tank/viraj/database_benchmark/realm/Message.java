package tank.viraj.database_benchmark.realm;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class Message extends RealmObject {
    @PrimaryKey
    private int intField;
    private long longField;
    private double doubleField;
    private String stringField;

    public int getIntField() {
        return intField;
    }

    public void setIntField(int mId) {
        this.intField = mId;
    }

    public long getLongField() {
        return longField;
    }

    public void setLongField(long longField) {
        this.longField = longField;
    }

    public double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(double doubleField) {
        this.doubleField = doubleField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }
}
