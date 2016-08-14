package tank.viraj.database_benchmark.sugarorm;

import com.orm.SugarRecord;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class Message extends SugarRecord {
    public int intField;
    public long longField;
    public double doubleField;
    public String stringField;

    public Message() {
    }
}