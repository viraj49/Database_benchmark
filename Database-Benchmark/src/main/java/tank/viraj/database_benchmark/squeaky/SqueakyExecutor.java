package tank.viraj.database_benchmark.squeaky;

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
public class SqueakyExecutor implements BenchmarkExecutable {
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
            Message newMessage = new Message();
            newMessage.intField = i;
            newMessage.longField = Data.longData;
            newMessage.doubleField = Data.doubleData;
            newMessage.stringField = Data.stringData;

            messages.add(newMessage);
        }

        long start = System.nanoTime();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            Dao messageDao = mHelper.getDao(Message.class);
            for (Message message : messages) {
                messageDao.create(messages);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long writeSingleData() throws SQLException {
        List<Message> messages = new LinkedList<>();
        for (int i = NUM_MESSAGE_INSERTS; i < (NUM_MESSAGE_INSERTS * 2); i++) {
            Message newMessage = new Message();
            newMessage.intField = i;
            newMessage.longField = Data.longData;
            newMessage.doubleField = Data.doubleData;
            newMessage.stringField = Data.stringData;

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
    public long updateData() throws SQLException {
        List<Message> messages = new LinkedList<>();
        for (int i = 0; i < (NUM_MESSAGE_INSERTS * 2); i++) {
            Message newMessage = new Message();
            newMessage.intField = i;
            newMessage.longField = Data.longData + 1;
            newMessage.doubleField = Data.doubleData + 1;
            newMessage.stringField = Data.stringData + "update";

            messages.add(newMessage);
        }

        long start = System.nanoTime();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            Dao messageDao = mHelper.getDao(Message.class);
            for (Message message : messages) {
                messageDao.update(message);
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
    public long countData() throws SQLException {
        long start = System.nanoTime();

        int count = mHelper.getDao(Message.class).queryForAll().list().size();

        return (System.nanoTime() - start);
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        TableUtils.dropTables(mHelper.getWrappedDatabase(), true, Message.class);
        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "Squeaky";
    }
}
