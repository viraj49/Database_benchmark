package tank.viraj.database_benchmark.requery;

import android.content.Context;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;
import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class RequeryExecutor implements BenchmarkExecutable {
    private DataBaseHelper mHelper;
    private Context context;
    private boolean useInMemoryDb;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        this.context = context;
        this.useInMemoryDb = useInMemoryDb;
    }

    @Override
    public long createDbStructure() throws SQLException {
        long start = System.nanoTime();

        DataBaseHelper.init(context, useInMemoryDb);
        mHelper = DataBaseHelper.getInstance();
        mHelper.createTables(mHelper.getWritableDatabase());

        return (System.nanoTime() - start);
    }

    @Override
    public long writeWholeData() throws SQLException {
        final List<MessageEntity> messages = new LinkedList<>();
        for (int i = 0; i < NUM_MESSAGE_INSERTS; i++) {
            MessageEntity newMessage = new MessageEntity();
            newMessage.intField = i;
            newMessage.longField = Data.longData;
            newMessage.doubleField = Data.doubleData;
            newMessage.stringField = Data.stringData;

            messages.add(newMessage);
        }

        long start = System.nanoTime();

        final EntityDataStore<Persistable> userStore = new EntityDataStore<>(mHelper.getConfiguration());
        userStore.runInTransaction(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                userStore.insert(messages);
                return null;
            }
        });

        return (System.nanoTime() - start);
    }

    @Override
    public long writeSingleData() throws SQLException {
        final List<MessageEntity> messages = new LinkedList<>();
        for (int i = NUM_MESSAGE_INSERTS; i < (NUM_MESSAGE_INSERTS * 2); i++) {
            MessageEntity newMessage = new MessageEntity();
            newMessage.intField = i;
            newMessage.longField = Data.longData;
            newMessage.doubleField = Data.doubleData;
            newMessage.stringField = Data.stringData;

            messages.add(newMessage);
        }

        long start = System.nanoTime();

        final EntityDataStore<Persistable> userStore = new EntityDataStore<>(mHelper.getConfiguration());
        userStore.runInTransaction(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                for (Message message : messages) {
                    userStore.insert(message);
                }
                return null;
            }
        });

        return (System.nanoTime() - start);
    }

    @Override
    public long updateData() throws SQLException {
        final List<MessageEntity> messages = new LinkedList<>();
        for (int i = NUM_MESSAGE_INSERTS; i < (NUM_MESSAGE_INSERTS * 2); i++) {
            MessageEntity newMessage = new MessageEntity();
            newMessage.intField = i;
            newMessage.longField = Data.longData + 1;
            newMessage.doubleField = Data.doubleData + 1;
            newMessage.stringField = Data.stringData + "update";

            messages.add(newMessage);
        }

        long start = System.nanoTime();

        final EntityDataStore<Persistable> userStore = new EntityDataStore<>(mHelper.getConfiguration());
        userStore.runInTransaction(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                userStore.update(messages);
                return null;
            }
        });

        return (System.nanoTime() - start);

    }

    @Override
    public long readSingleData() throws SQLException {
        long start = System.nanoTime();

        final EntityDataStore<Persistable> messageStore = new EntityDataStore<>(mHelper.getConfiguration());
        for (int i = 0; i < NUM_MESSAGE_QUERY; i++) {
            Message message = messageStore.select(Message.class).where(MessageEntity.INT_FIELD.eq(i)).get().first();
            int intField = message.intField;
            long longField = message.longField;
            double doubleField = message.doubleField;
            String stringField = message.stringField;
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readBatchData() throws SQLException {
        long start = System.nanoTime();

        final EntityDataStore<Persistable> userStore = new EntityDataStore<>(mHelper.getConfiguration());
        List<Message> messages = userStore.select(Message.class).where(MessageEntity.INT_FIELD.greaterThan(NUM_BATCH_QUERY)).get().toList();
        for (Message message : messages) {
            int intField = message.intField;
            long longField = message.longField;
            double doubleField = message.doubleField;
            String stringField = message.stringField;
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readWholeData() throws SQLException {
        long start = System.nanoTime();

        final EntityDataStore<Persistable> userStore = new EntityDataStore<>(mHelper.getConfiguration());
        List<Message> messages = userStore.select(Message.class).get().toList();
        for (Message message : messages) {
            int intField = message.intField;
            long longField = message.longField;
            double doubleField = message.doubleField;
            String stringField = message.stringField;
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long countData() throws SQLException {
        long start = System.nanoTime();

        final EntityDataStore<Persistable> userStore = new EntityDataStore<>(mHelper.getConfiguration());
        int count = userStore.select(Message.class).get().toList().size();

        return (System.nanoTime() - start);
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();

        mHelper.dropTables();

        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "Requery";
    }
}
