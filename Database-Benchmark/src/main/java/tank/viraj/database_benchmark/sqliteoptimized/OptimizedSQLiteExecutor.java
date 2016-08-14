package tank.viraj.database_benchmark.sqliteoptimized;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.sqlite.Message;
import tank.viraj.database_benchmark.util.Data;

/**
 * This executor takes the basic idea of the standard Android sqlLite query helper, but adds a few optimizations.
 * <p>
 * Select optimization
 * A Cursor can only access columns by their respective position in the result
 * the standard way of reading the fields value is cursor.getString(cursor.getColumnIndex("field_name"))
 * this actually loops through all column names until it finds one that matches and then return that index.
 * <p>
 * To decrease unnecessary lookup, we do this once before we read any of the rows, and remembers the position
 * in local variables.
 * <p>
 * Insert optimizations
 * The database.Insert function does not cache the insert query. Instead it re-creates the full statement
 * for each time its called including adding all values to the statement
 * What we do here instead is to create the SQL-statement manually and let the database driver compile it
 * for us. This creates a re-usable and very fast executable statement for us.
 * <p>
 * For each row we just need to insert the values into the statement via the bind function and then
 * execute it.
 * <p>
 * Everything should of course still be encapsulated by a transaction, otherwise you will get a huge overhead
 * per row.
 */
public class OptimizedSQLiteExecutor implements BenchmarkExecutable {
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
        List<OptimizedMessage> messages = new LinkedList<>();
        for (int i = 0; i < NUM_MESSAGE_INSERTS; i++) {
            OptimizedMessage newMessage = new OptimizedMessage();
            newMessage.setIntField(i);
            newMessage.setLongField(Data.longData);
            newMessage.setDoubleField(Data.doubleData);
            newMessage.setStringField(Data.stringData);

            messages.add(newMessage);
        }

        long start = System.nanoTime();
        SQLiteDatabase db = mHelper.getWritableDatabase();

        SQLiteStatement insertMessage = db.compileStatement(String
                .format("Insert into %s (%s, %s, %s, %s) values (?,?,?,?)",
                        OptimizedMessage.TABLE_NAME,
                        OptimizedMessage.INT_FIELD,
                        OptimizedMessage.LONG_FIELD,
                        OptimizedMessage.DOUBLE_FIELD,
                        OptimizedMessage.STRING_FIELD));

        try {
            db.beginTransaction();

            for (OptimizedMessage message : messages) {
                message.prepareForInsert(insertMessage);
                insertMessage.execute();
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
        List<OptimizedMessage> messages;

        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            messages = new LinkedList<>();
            cursor = db.query(OptimizedMessage.TABLE_NAME, OptimizedMessage.PROJECTION, null, null, null, null, null);

            if (cursor != null) {
                int intFieldId = cursor.getColumnIndex(OptimizedMessage.INT_FIELD);
                int longFieldId = cursor.getColumnIndex(OptimizedMessage.LONG_FIELD);
                int doubleFieldId = cursor.getColumnIndex(OptimizedMessage.DOUBLE_FIELD);
                int stringFieldId = cursor.getColumnIndex(OptimizedMessage.STRING_FIELD);

                while (cursor.moveToNext()) {
                    OptimizedMessage newMessage = new OptimizedMessage();

                    newMessage.setIntField(cursor.getInt(intFieldId));
                    newMessage.setLongField(cursor.getLong(longFieldId));
                    newMessage.setDoubleField(cursor.getDouble(doubleFieldId));
                    newMessage.setStringField(cursor.getString(stringFieldId));

                    messages.add(newMessage);
                }
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

        return System.nanoTime() - start;
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();

        Message.dropTable(mHelper);

        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "SQLite_OPTIMIZED";
    }
}