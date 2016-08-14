package tank.viraj.database_benchmark.requery;

import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
@Entity
public class Message implements Persistable {
    @Key
    public int intField;
    public long longField;
    public double doubleField;
    public String stringField;
}