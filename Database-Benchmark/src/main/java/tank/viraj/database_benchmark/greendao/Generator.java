package tank.viraj.database_benchmark.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * Created by kgalligan, 10/19/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class Generator {
    // MESSAGE TABLE
    private static final String MESSAGE_ENTITY = "Message";

    private static final String INT_FIELD = "int_field";
    private static final String LONG_FIELD = "long_field";
    private static final String DOUBLE_FIELD = "double_field";
    private static final String STRING_FIELD = "string_field";

    public static void main(String[] args) {
        int DB_VERSION = 1;
        String PACKAGE = "com.littleinc.orm_benchmark.greendao";
        Schema schema = new Schema(DB_VERSION, PACKAGE);

        Entity message = schema.addEntity(MESSAGE_ENTITY);
        message.addIdProperty().autoincrement();
        message.addIntProperty(INT_FIELD);
        message.addLongProperty(LONG_FIELD);
        message.addDoubleProperty(DOUBLE_FIELD);
        message.addStringProperty(STRING_FIELD);

        try {
            new DaoGenerator().generateAll(schema, "ORM-Benchmark/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
