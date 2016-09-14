package tank.viraj.database_benchmark.dbflow;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

/**
 * Created by kgalligan, 10/12/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class DBFlowExecutor implements BenchmarkExecutable {
    private Context applicationContext;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        applicationContext = context.getApplicationContext();
    }

    @Override
    public long createDbStructure() throws SQLException {
        long start = System.nanoTime();

        FlowManager.init(new FlowConfig.Builder(applicationContext)
                .openDatabasesOnInit(true)
                .build());

        return System.nanoTime() - start;
    }

    @Override
    public long writeWholeData() throws SQLException {
        final List<Message> messages = new LinkedList<>();
        for (int i = 0; i < NUM_MESSAGE_INSERTS; i++) {
            Message newMessage = new Message();
            newMessage.intField = i;
            newMessage.longField = Data.longData;
            newMessage.doubleField = Data.doubleData;
            newMessage.stringField = Data.stringData;

            messages.add(newMessage);
        }

        long start = System.nanoTime();

        FlowManager.getDatabase(DatabaseModule.NAME).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for (Message message : messages) {
                    message.save();
                }
            }
        });

        return System.nanoTime() - start;
    }

    @Override
    public long writeSingleData() throws SQLException {
        final List<Message> messages = new LinkedList<>();
        for (int i = NUM_MESSAGE_INSERTS; i < (NUM_MESSAGE_INSERTS * 2); i++) {
            Message newMessage = new Message();
            newMessage.intField = i;
            newMessage.longField = Data.longData;
            newMessage.doubleField = Data.doubleData;
            newMessage.stringField = Data.stringData;

            messages.add(newMessage);
        }

        long start = System.nanoTime();

        FlowManager.getDatabase(DatabaseModule.NAME).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for (Message message : messages) {
                    message.save();
                }
            }
        });

        return System.nanoTime() - start;
    }

    @Override
    public long updateData() throws SQLException {
        final List<Message> messages = new LinkedList<>();
        for (int i = 0; i < (NUM_MESSAGE_INSERTS * 2); i++) {
            Message newMessage = new Message();
            newMessage.intField = i;
            newMessage.longField = Data.longData + 1;
            newMessage.doubleField = Data.doubleData + 1;
            newMessage.stringField = Data.stringData + "update";

            messages.add(newMessage);
        }

        long start = System.nanoTime();

        FlowManager.getDatabase(DatabaseModule.NAME).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for (Message message : messages) {
                    message.update();
                }
            }
        });

        return System.nanoTime() - start;
    }

    @Override
    public long readSingleData() throws SQLException {
        long start = System.nanoTime();
        for (int i = 0; i < NUM_MESSAGE_QUERY; i++) {
            Message message = new Select().from(Message.class).where(Message_Table.intField.is(i)).querySingle();
            assert message != null;
            int intField = message.intField;
            long longField = message.longField;
            double doubleField = message.doubleField;
            String stringField = message.stringField;
        }
        return System.nanoTime() - start;
    }

    @Override
    public long readBatchData() throws SQLException {
        long start = System.nanoTime();
        List<Message> messages = new Select().from(Message.class).where(Message_Table.intField.greaterThan(NUM_BATCH_QUERY)).queryList();
        for (Message message : messages) {
            int intField = message.intField;
            long longField = message.longField;
            double doubleField = message.doubleField;
            String stringField = message.stringField;
        }
        return System.nanoTime() - start;
    }

    @Override
    public long readWholeData() throws SQLException {
        long start = System.nanoTime();
        List<Message> messages = new Select().from(Message.class).queryList();
        for (Message message : messages) {
            int intField = message.intField;
            long longField = message.longField;
            double doubleField = message.doubleField;
            String stringField = message.stringField;
        }
        return System.nanoTime() - start;
    }

    @Override
    public long countData() throws SQLException {
        long start = System.nanoTime();

        int count = new Select().from(Message.class).queryList().size();

        return System.nanoTime() - start;
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        Delete.table(Message.class);
        return System.nanoTime() - start;
    }

    @Override
    public String getOrmName() {
        return "DBFlow";
    }
}
