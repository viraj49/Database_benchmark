package tank.viraj.database_benchmark.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

public class ORMLiteExecutor implements BenchmarkExecutable {
    private DataBaseHelper mHelper;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        DataBaseHelper.init(context, useInMemoryDb);
        mHelper = DataBaseHelper.getInstance();
    }

    @Override
    public long createDbStructure() throws SQLException {
        long start = System.nanoTime();
        ConnectionSource connectionSource = mHelper.getConnectionSource();
        TableUtils.createTable(connectionSource, Message.class);
        return (System.nanoTime() - start);
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
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            for (Message message : messages) {
                Message.getDao().createOrUpdate(message);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readSingleData() throws SQLException {
        long start = System.nanoTime();

        for (int i = 0; i < NUM_MESSAGE_QUERY; i++) {
            List<Message> messages = mHelper.getDao(Message.class).queryForEq("intField", i);
            if (messages != null) {
                if (messages.size() > 0) {
                    Message message = messages.get(0);
                    int intField = message.getIntField();
                    long longField = message.getLongField();
                    double doubleField = message.getDoubleField();
                    String stringField = message.getStringField();
                }
            }
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readBatchData() throws SQLException {
        long start = System.nanoTime();

        List<Message> messages = mHelper.getDao(Message.class).queryBuilder().where().gt("intField", (NUM_BATCH_QUERY)).query();
        for (Message message : messages) {
            int intField = message.getIntField();
            long longField = message.getLongField();
            double doubleField = message.getDoubleField();
            String stringField = message.getStringField();
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readWholeData() throws SQLException {
        long start = System.nanoTime();
        List<Message> messages = mHelper.getDao(Message.class).queryForAll();
        for (Message message : messages) {
            int intField = message.getIntField();
            long longField = message.getLongField();
            double doubleField = message.getDoubleField();
            String stringField = message.getStringField();
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        ConnectionSource connectionSource = mHelper.getConnectionSource();
        TableUtils.dropTable(connectionSource, Message.class, true);
        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "ORMLite";
    }
}