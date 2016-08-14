package tank.viraj.database_benchmark.squidb;

import android.content.Context;

import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Query;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class SquidbExecutor implements BenchmarkExecutable {
    private MyDatabase myDatabase;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        myDatabase = new MyDatabase(context);
    }

    @Override
    public long createDbStructure() throws SQLException {
        return 0;
    }

    @Override
    public long writeWholeData() throws SQLException {
        List<Message> messages = new LinkedList<>();
        for (int i = 0; i < NUM_MESSAGE_INSERTS; i++) {
            Message newMessage = new Message();
            newMessage.setIntField(i);
            newMessage.setLongField(Data.longData);
            newMessage.setDoubleField(Data.doubleData);
            newMessage.setStringField(Data.stringData);

            messages.add(newMessage);
        }

        long start = System.nanoTime();
        myDatabase.beginTransaction();

        try {
            for (Message message : messages) {
                myDatabase.persist(message);
            }
            myDatabase.setTransactionSuccessful();
        } finally {
            myDatabase.endTransaction();
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readSingleData() throws SQLException {
        long start = System.nanoTime();

        for (int i = 0; i < NUM_MESSAGE_QUERY; i++) {
            Message message = myDatabase.fetchByQuery(Message.class, Query.select().from(Message.TABLE).where(Message.INT_FIELD.eq(i)));
            int intField = message.getIntField();
            long longField = message.getLongField();
            double doubleField = message.getDoubleField();
            String stringField = message.getStringField();
        }

        return System.nanoTime() - start;
    }

    @Override
    public long readBatchData() throws SQLException {
        long start = System.nanoTime();
        int count = 0;
        SquidCursor<Message> query = myDatabase.query(Message.class, Query.select().from(Message.TABLE).where(Message.INT_FIELD.gt(NUM_BATCH_QUERY)));
        Message message = new Message();
        while (query.moveToNext()) {
            message.readPropertiesFromCursor(query);

            int intField = message.getIntField();
            long longField = message.getLongField();
            double doubleField = message.getDoubleField();
            String stringField = message.getStringField();
            count++;
        }

        return System.nanoTime() - start;
    }

    @Override
    public long readWholeData() throws SQLException {
        long start = System.nanoTime();

        SquidCursor<Message> query = myDatabase.query(Message.class, Query.select());
        Message message = new Message();
        while (query.moveToNext()) {
            message.readPropertiesFromCursor(query);

            int intField = message.getIntField();
            long longField = message.getLongField();
            double doubleField = message.getDoubleField();
            String stringField = message.getStringField();
        }

        return System.nanoTime() - start;
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        myDatabase.deleteAll(Message.class);
        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "Squidb";
    }
}
