package tank.viraj.database_benchmark.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

public class SQLiteExecutor implements BenchmarkExecutable {
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
        mHelper = new DataBaseHelper(context, useInMemoryDb);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(Message.CreateTable);
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
    public long writeSingleData() throws SQLException {
        List<Message> messages = new LinkedList<>();
        for (int i = NUM_MESSAGE_INSERTS; i < (NUM_MESSAGE_INSERTS * 2); i++) {
            Message newMessage = new Message();
            newMessage.setIntField(i);
            newMessage.setLongField(Data.longData);
            newMessage.setDoubleField(Data.doubleData);
            newMessage.setStringField(Data.stringData);

            messages.add(newMessage);
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        long start = System.nanoTime();
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
    public long updateData() throws SQLException {
        //List<Message> messages = new LinkedList<>();
//        for (int i = 0; i < NUM_MESSAGE_INSERTS; i++) {
//            Message newMessage = new Message();
//            newMessage.setIntField(i);
//            newMessage.setLongField(Data.longData + 1);
//            newMessage.setDoubleField(Data.doubleData + 1);
//            newMessage.setStringField(Data.stringData + "update");
//
//            messages.add(newMessage);
//        }
//
//        long start = System.nanoTime();
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        try {
//            db.beginTransaction();
//
//            for (Message message : messages) {
//                String[] setArgs = new String[]{Message.LONG_FIELD + " = " + message.getLongField(),
//                        Message.DOUBLE_FIELD + " = " + message.getDoubleField(),
//                        Message.STRING_FIELD + " = " + message.stringField};
//                db.update(Message.TABLE_NAME, Message.INT_FIELD + " = " + message.intField, setArgs);
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//        }

        return (9999999);
    }

    @Override
    public long readSingleData() throws SQLException {
        long start = System.nanoTime();

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = null;
        Message newMessage;
        try {
            for (int i = 0; i < NUM_MESSAGE_QUERY / 4; i++) {
                newMessage = new Message();

                String[] whereArgs = new String[]{"" + i};
                cursor = db.query(Message.TABLE_NAME, Message.PROJECTION, Message.INT_FIELD + " = ?", whereArgs, null, null, null, null);
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

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Message> messages;
        try {
            messages = new LinkedList<>();

            String[] whereArgs = new String[]{"" + (NUM_MESSAGE_INSERTS - 1000)};
            cursor = db.query(Message.TABLE_NAME, Message.PROJECTION, Message.INT_FIELD + " > ?", whereArgs, null, null, null, null);
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

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Message> messages;
        try {
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
    public long countData() throws SQLException {
        long start = System.nanoTime();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        int count = db.query(Message.TABLE_NAME, Message.PROJECTION, null, null, null, null, null).getCount();
        return (System.nanoTime() - start);
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(Message.DropTable);
        db.close();
        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "SQLite";
    }
}