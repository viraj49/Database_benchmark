package tank.viraj.database_benchmark.squidb;

import com.yahoo.squidb.annotations.PrimaryKey;
import com.yahoo.squidb.annotations.TableModelSpec;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
@TableModelSpec(className = "Message", tableName = "message")
class MessageSpec {
    @PrimaryKey
    public long id;

    public int intField;
    public long longField;
    public double doubleField;
    public String stringField;
}