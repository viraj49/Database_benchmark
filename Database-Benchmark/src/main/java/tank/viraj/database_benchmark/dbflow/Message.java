package tank.viraj.database_benchmark.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.IndexGroup;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by kgalligan, 10/12/15.
 * Edited by Viraj Tank, 10/08/16.
 */
@Table(database = DatabaseModule.class, indexGroups = {
        @IndexGroup(number = 1, name = "index")},
        orderedCursorLookUp = true)
public class Message extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public int intField;

    @Column
    public long longField;

    @Column
    public double doubleField;

    @Column
    public String stringField;
}
