package tank.viraj.database_benchmark.sugarorm;

import com.orm.Database;
import com.orm.SugarApp;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class SugarWeirdAppExtensionBecauseBadDesign extends SugarApp {
    public Database sneakyBreakEncapsulationBecause() {
        return getDatabase();
    }
}