package tank.viraj.database_benchmark.cupboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;
import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class CupboardExecutor implements BenchmarkExecutable {
    private DataBaseHelper mHelper;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        mHelper = new DataBaseHelper(context, useInMemoryDb);
    }

    @Override
    public long createDbStructure() throws SQLException {
        long start = System.nanoTime();
        cupboard().withDatabase(mHelper.getWritableDatabase()).createTables();
        return System.nanoTime() - start;
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
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            cupboard().withDatabase(mHelper.getWritableDatabase()).put(messages);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long writeSingleData() throws SQLException {
        List<Message> messages = new LinkedList<>();
        for (int i = NUM_MESSAGE_INSERTS; (i < NUM_MESSAGE_INSERTS * 2); i++) {
            Message newMessage = new Message();
            newMessage.intField = i;
            newMessage.longField = Data.longData;
            newMessage.doubleField = Data.doubleData;
            newMessage.stringField = Data.stringData;

            messages.add(newMessage);
        }

        long start = System.nanoTime();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            for (Message message : messages) {
                cupboard().withDatabase(mHelper.getWritableDatabase()).put(message);
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
        for (int i = 0; (i < NUM_MESSAGE_INSERTS * 2); i++) {
            Message newMessage = new Message();
            newMessage.intField = i;
            newMessage.longField = Data.longData + 1;
            newMessage.doubleField = Data.doubleData + 1;
            newMessage.stringField = Data.stringData + "update";

            messages.add(newMessage);
        }

        long start = System.nanoTime();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            for (Message message : messages) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", message._id);
                contentValues.put("intFiled", message.intField);
                contentValues.put("longFiled", message.longField);
                contentValues.put("doubleFiled", message.doubleField);
                contentValues.put("stringFiled", message.stringField);
                cupboard().withDatabase(mHelper.getWritableDatabase()).update(Message.class, contentValues);
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
            Message message = cupboard().withDatabase(mHelper.getWritableDatabase())
                    .query(Message.class).withSelection("intField = " + i).get();

            long intField = message.intField;
            long longField = message.longField;
            double doubleField = message.doubleField;
            String stringField = message.stringField;
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readBatchData() throws SQLException {
        long start = System.nanoTime();

        List<Message> messages = cupboard().withDatabase(mHelper.getWritableDatabase())
                .query(Message.class).withSelection("intField > " + (NUM_BATCH_QUERY)).list();

        for (Message message : messages) {
            long intField = message.intField;
            long longField = message.longField;
            double doubleField = message.doubleField;
            String stringField = message.stringField;
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readWholeData() throws SQLException {
        long start = System.nanoTime();

        Cursor cursor = cupboard().withDatabase(mHelper.getWritableDatabase()).query(Message.class).getCursor();
        QueryResultIterable<Message> iterate = null;
        try {
            iterate = cupboard().withCursor(cursor).iterate(Message.class);
            for (Message message : iterate) {
                long intField = message.intField;
                long longField = message.longField;
                double doubleField = message.doubleField;
                String stringField = message.stringField;
            }
        } finally {
            if (iterate != null) {
                iterate.close();
                cursor.close();
            }
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long countData() throws SQLException {
        long start = System.nanoTime();

        Cursor cursor = cupboard().withDatabase(mHelper.getWritableDatabase()).query(Message.class).getCursor();
        try {
            int count = cupboard().withCursor(cursor).list(Message.class).size();
        } finally {
            cursor.close();
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        cupboard().withDatabase(mHelper.getWritableDatabase()).dropAllTables();
        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "Cupboard";
    }
}
