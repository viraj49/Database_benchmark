package tank.viraj.database_benchmark.sqliteoptimized;

import android.database.sqlite.SQLiteStatement;

import tank.viraj.database_benchmark.sqlite.Message;

public class OptimizedMessage extends Message {
    public void prepareForInsert(final SQLiteStatement insertMessage) {
        insertMessage.bindLong(1, intField);
        insertMessage.bindLong(2, longField);
        insertMessage.bindDouble(3, doubleField);
        insertMessage.bindString(4, stringField);
    }
}
