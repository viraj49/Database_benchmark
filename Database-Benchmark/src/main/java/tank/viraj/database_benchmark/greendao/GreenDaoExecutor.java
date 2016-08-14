package tank.viraj.database_benchmark.greendao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

import java.util.LinkedList;
import java.util.List;

import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

public class GreenDaoExecutor implements BenchmarkExecutable {
    /**
     * The identity scope tracks all entity objects to assure the same object is returned for one specific ID.
     * Of course this comes with some overhead (map with weak references): thus it should only be used for comparing to
     * other ORMs that also have a similar feature (e.g. caches).
     */
    private static final String DB_NAME = "greenDaoDBName";
    private boolean USE_IDENTITY_SCOPE = false;
    private DataBaseHelper mHelper;
    private DaoSession mDaoSession;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        mHelper = new DataBaseHelper(context, (useInMemoryDb ? null : DB_NAME), null);
    }

    @Override
    public long createDbStructure() throws SQLException {
        long start = System.nanoTime();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        if (mDaoSession == null) {
            mDaoSession = new DaoMaster(db).newSession(USE_IDENTITY_SCOPE ? IdentityScopeType.Session : IdentityScopeType.None);
        }

        Database db2 = mHelper.getWritableDb();
        DaoMaster.createAllTables(db2, true);

        return System.nanoTime() - start;
    }

    @Override
    public long writeWholeData() throws SQLException {
        final List<Message> messages = new LinkedList<>();
        for (int i = 0; i < NUM_MESSAGE_INSERTS; i++) {
            Message newMessage = new Message(null);
            newMessage.setInt_field(i);
            newMessage.setLong_field(Data.longData);
            newMessage.setDouble_field(Data.doubleData);
            newMessage.setString_field(Data.stringData);

            messages.add(newMessage);
        }

        long start = System.nanoTime();
        mDaoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                MessageDao messageDao = mDaoSession.getMessageDao();
                messageDao.insertInTx(messages);
            }
        });

        if (USE_IDENTITY_SCOPE) {
            mDaoSession.clear();
        }
        return System.nanoTime() - start;
    }

    @Override
    public long readSingleData() throws SQLException {
        long start = System.nanoTime();

        for (int i = 0; i < NUM_MESSAGE_QUERY; i++) {
            Message message = mDaoSession.getMessageDao().queryBuilder().where(MessageDao.Properties.Int_field.eq(i)).unique();
            int intField = message.getInt_field();
            long longField = message.getLong_field();
            double doubleField = message.getDouble_field();
            String stringField = message.getString_field();
        }

        if (USE_IDENTITY_SCOPE) {
            mDaoSession.clear();
        }

        return System.nanoTime() - start;
    }

    @Override
    public long readBatchData() throws java.sql.SQLException {
        long start = System.nanoTime();

        List<Message> messages = mDaoSession.getMessageDao().queryBuilder().where(MessageDao.Properties.Int_field.gt(NUM_BATCH_QUERY)).list();
        for (Message message : messages) {
            int intField = message.getInt_field();
            long longField = message.getLong_field();
            double doubleField = message.getDouble_field();
            String stringField = message.getString_field();
        }
        if (USE_IDENTITY_SCOPE) {
            mDaoSession.clear();
        }

        return System.nanoTime() - start;
    }

    @Override
    public long readWholeData() throws SQLException {
        long start = System.nanoTime();

        List<Message> messages = mDaoSession.getMessageDao().loadAll();
        for (Message message : messages) {
            int intField = message.getInt_field();
            long longField = message.getLong_field();
            double doubleField = message.getDouble_field();
            String stringField = message.getString_field();
        }
        if (USE_IDENTITY_SCOPE) {
            mDaoSession.clear();
        }

        return System.nanoTime() - start;
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        Database db = mHelper.getWritableDb();
        DaoMaster.dropAllTables(db, true);
        // Reset version, so OpenHelper does not get confused

        return System.nanoTime() - start;
    }

    @Override
    public String getOrmName() {
        return "GreenDAO";
    }
}
