package tank.viraj.database_benchmark.squeakyfinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import co.touchlab.squeaky.dao.Dao;
import co.touchlab.squeaky.table.TableUtils;
import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

/**
 * Created by kgalligan, 10/12/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class SqueakyFinalExecutor implements BenchmarkExecutable {
    private DataBaseHelper mHelper;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        DataBaseHelper.init(context, useInMemoryDb);
        mHelper = DataBaseHelper.getInstance();
    }

    @Override
    public long createDbStructure() throws SQLException {
        long start = System.nanoTime();
        TableUtils.createTables(mHelper.getWrappedDatabase(), Message.class);
        return (System.nanoTime() - start);
    }

    @Override
    public long writeWholeData() throws SQLException {
        List<Message> messages = new LinkedList<>();
        for (int i = 0; i < NUM_MESSAGE_INSERTS; i++) {
            Message newMessage = new Message(i, Data.longData, Data.doubleData, Data.stringData);
            messages.add(newMessage);
        }

        long start = System.nanoTime();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.beginTransaction();

        try {
            Dao messageDao = mHelper.getDao(Message.class);
            for (Message message : messages) {
                messageDao.create(message);
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
            List<Message> messages = mHelper.getDao(Message.class).queryForEq("intField", i).list();
            if (messages != null) {
                if (messages.size() > 0) {
                    Message message = messages.get(0);
                    int intField = message.intField;
                    long longField = message.longField;
                    double doubleField = message.doubleField;
                    String stringField = message.stringField;
                }
            }
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readBatchData() throws SQLException {
        /* TODO: find a way to query gt */
        return (-1);
    }

    @Override
    public long readWholeData() throws SQLException {
        long start = System.nanoTime();

        List<Message> messages = mHelper.getDao(Message.class).queryForAll().list();
        for (Message message : messages) {
            int intField = message.intField;
            long longField = message.longField;
            double doubleField = message.doubleField;
            String stringField = message.stringField;
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        TableUtils.dropTables(mHelper.getWrappedDatabase(), true, Message.class);
        return System.nanoTime() - start;
    }

    @Override
    public String getOrmName() {
        return "SqueakyFinal";
    }
}
