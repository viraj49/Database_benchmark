package tank.viraj.database_benchmark.squeaky;

import co.touchlab.squeaky.field.DatabaseField;
import co.touchlab.squeaky.table.DatabaseTable;

@DatabaseTable
public class Message {
    public static final String NAME_BASE = "aabbddccssddfdsdfs_";

    @DatabaseField(generatedId = true, canBeNull = true, columnName = NAME_BASE + "id")
    public long id;

    @DatabaseField(columnName = NAME_BASE + "intField")
    public int intField;

    @DatabaseField(columnName = NAME_BASE + "longField")
    public long longField;

    @DatabaseField(columnName = NAME_BASE + "doubleField")
    public double doubleField;

    @DatabaseField(columnName = NAME_BASE + "stringField")
    public String stringField;
}