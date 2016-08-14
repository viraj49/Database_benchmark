package tank.viraj.database_benchmark.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.sqliteoptimized.OptimizedSQLiteExecutor;
import tank.viraj.database_benchmark.util.Data;

/**
 * An unoptimized set of SQLite operations for reading and writing the test database objects.
 * <p>
 * See {@link OptimizedSQLiteExecutor} for optimized
 * versions of these SQLite operations.
 */
public class SQLiteExecutor implements BenchmarkExecutable {
    private DataBaseHelper mHelper;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        mHelper = new DataBaseHelper(context, useInMemoryDb);
    }

    @Override
    public long createDbStructure() throws SQLException {
        long start = System.nanoTime();
        Message.createTable(mHelper);
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

        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            for (Message message : messages) {
                db.insert(Message.TABLE_NAME, null, message.prepareForInsert());
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

        Cursor cursor = null;
        Message newMessage;
        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();

            for (int i = 0; i < NUM_MESSAGE_QUERY; i++) {
                newMessage = new Message();

                cursor = db.query(Message.TABLE_NAME, Message.PROJECTION, Message.INT_FIELD + "=" + i, null, null, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    newMessage.setIntField(cursor.getInt(cursor.getColumnIndex(Message.INT_FIELD)));
                    newMessage.setLongField(cursor.getLong(cursor.getColumnIndex(Message.LONG_FIELD)));
                    newMessage.setDoubleField(cursor.getDouble(cursor.getColumnIndex(Message.DOUBLE_FIELD)));
                    newMessage.setStringField(cursor.getString(cursor.getColumnIndex(Message.STRING_FIELD)));
                }

                int intField = newMessage.getIntField();
                long longField = newMessage.getLongField();
                double doubleField = newMessage.getDoubleField();
                String stringField = newMessage.getStringField();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long readBatchData() throws SQLException {
        long start = System.nanoTime();

        Cursor cursor = null;
        List<Message> messages;
        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            messages = new LinkedList<>();
            cursor = db.query(Message.TABLE_NAME, Message.PROJECTION, Message.INT_FIELD + ">" + (NUM_BATCH_QUERY), null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                Message newMessage = new Message();
                newMessage.setIntField(cursor.getInt(cursor.getColumnIndex(Message.INT_FIELD)));
                newMessage.setLongField(cursor.getLong(cursor.getColumnIndex(Message.LONG_FIELD)));
                newMessage.setDoubleField(cursor.getDouble(cursor.getColumnIndex(Message.DOUBLE_FIELD)));
                newMessage.setStringField(cursor.getString(cursor.getColumnIndex(Message.STRING_FIELD)));

                messages.add(newMessage);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

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

        Cursor cursor = null;
        List<Message> messages;
        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            messages = new LinkedList<>();
            cursor = db.query(Message.TABLE_NAME, Message.PROJECTION, null, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                Message newMessage = new Message();
                newMessage.setIntField(cursor.getInt(cursor.getColumnIndex(Message.INT_FIELD)));
                newMessage.setLongField(cursor.getLong(cursor.getColumnIndex(Message.LONG_FIELD)));
                newMessage.setDoubleField(cursor.getDouble(cursor.getColumnIndex(Message.DOUBLE_FIELD)));
                newMessage.setStringField(cursor.getString(cursor.getColumnIndex(Message.STRING_FIELD)));

                messages.add(newMessage);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

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
        Message.dropTable(mHelper);
        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "SQLite";
    }
}