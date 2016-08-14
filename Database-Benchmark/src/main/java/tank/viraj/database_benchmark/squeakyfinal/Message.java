package tank.viraj.database_benchmark.squeakyfinal;

import co.touchlab.squeaky.field.DatabaseField;
import co.touchlab.squeaky.table.DatabaseTable;

@DatabaseTable
public class Message {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(canBeNull = false)
    public int intField;

    @DatabaseField(canBeNull = false)
    public long longField;

    @DatabaseField(canBeNull = false)
    public double doubleField;

    @DatabaseField(canBeNull = false)
    public String stringField;

    public Message() {
    }

    public Message(int intField, long longField, double doubleField, String stringField) {
        this.intField = intField;
        this.longField = longField;
        this.doubleField = doubleField;
        this.stringField = stringField;
    }
}
