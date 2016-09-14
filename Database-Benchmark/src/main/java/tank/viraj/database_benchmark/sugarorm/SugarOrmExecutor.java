package tank.viraj.database_benchmark.sugarorm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class SugarOrmExecutor implements BenchmarkExecutable {
    SugarWeirdAppExtensionBecauseBadDesign sugarApp;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        sugarApp = (SugarWeirdAppExtensionBecauseBadDesign) context.getApplicationContext();
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
            newMessage.intField = i;
            newMessage.longField = Data.longData;
            newMessage.doubleField = Data.doubleData;
            newMessage.stringField = Data.stringData;

            messages.add(newMessage);
        }

        long start = System.nanoTime();
        SQLiteDatabase db = sugarApp.sneakyBreakEncapsulationBecause().getDB();
        db.beginTransaction();
        try {
            for (Message message : messages) {
                message.save();
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return (System.nanoTime() - start);
    }

    @Override
    public long writeSingleData() throws SQLException {
        return 0;
    }

    @Override
    public long updateData() throws SQLException {
        return 0;
    }

    @Override
    public long readSingleData() throws SQLException {
        //long start = System.nanoTime();


//        for (long i = 0; i < NUM_MESSAGE_QUERY; i++) {
//            List<Message> messages = Message.findWithQuery(Message.class, String.valueOf(from(Message.class).where(Condition.prop("intField").eq(i))), null);
//            if (messages != null) {
//                if (messages.size() > 0) {
//                    int intField = messages.get(0).intField;
//                    long longField = messages.get(0).longField;
//                    double doubleField = messages.get(0).doubleField;
//                    String stringField = messages.get(0).stringField;
//                }
//            }
//        }

        //return (System.nanoTime() - start);

        /* TODO - find a way to query */
        return (-1);
    }

    @Override
    public long readBatchData() throws SQLException {
//        long start = System.nanoTime();
//        SQLiteDatabase db = sugarApp.sneakyBreakEncapsulationBecause().getDB();
//        db.beginTransaction();
//        try {
//            List<Message> messages = Message.findWithQuery(Message.class, String.valueOf(from(Message.class).where(Condition.prop("intField").gt(NUM_BATCH_QUERY), null).list()));
//            Log.e("VIRAJ", getOrmName() + messages.size());
//            for (Message message : messages) {
//                int intField = message.intField;
//                long longField = message.longField;
//                double doubleField = message.doubleField;
//                String stringField = message.stringField;
//            }
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("VIRAJ", "e", e);
//        } finally {
//            db.endTransaction();
//        }
//
//        return (System.nanoTime() - start);

        /* TODO - find a way to query */
        return (-1);
    }

    @Override
    public long readWholeData() throws SQLException {
//        long start = System.nanoTime();
//
//        List<Message> messages = Message.listAll(Message.class);
//        for (Message message : messages) {
//            int intField = message.intField;
//            long longField = message.longField;
//            double doubleField = message.doubleField;
//            String stringField = message.stringField;
//        }
//
//        return (System.nanoTime() - start);
        /* TODO - find a way to query */
        return (-1);
    }

    @Override
    public long countData() throws SQLException {
        return 0;
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        Message.deleteAll(Message.class);
        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "SugarOrm";
    }
}