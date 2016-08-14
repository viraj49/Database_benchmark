package tank.viraj.database_benchmark.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by kgalligan, 10/12/15.
 * Edited by Viraj Tank, 10/08/16
 */
@Database(name = DatabaseModule.NAME, version = DatabaseModule.VERSION)
public class DatabaseModule {
    static final String NAME = "dbflow";
    static final int VERSION = 1;
}