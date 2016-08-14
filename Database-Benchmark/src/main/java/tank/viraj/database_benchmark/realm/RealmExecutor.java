package tank.viraj.database_benchmark.realm;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.util.Data;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class RealmExecutor implements BenchmarkExecutable {
    private Context mContext;
    private Realm realm;

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        mContext = context;
    }

    @Override
    public long createDbStructure() throws SQLException {
        long start = System.nanoTime();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mContext).build();
        Realm.deleteRealm(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);

        return (System.nanoTime() - start);
    }

    @Override
    public long writeWholeData() throws SQLException {
        List<Message> messages = new ArrayList<>(NUM_MESSAGE_INSERTS);
        for (int i = 0; i < NUM_MESSAGE_INSERTS; i++) {
            Message newMessage = new Message();
            newMessage.setIntField(i);
            newMessage.setLongField(Data.longData);
            newMessage.setDoubleField(Data.doubleData);
            newMessage.setStringField(Data.stringData);

            messages.add(newMessage);
        }

        long start = System.nanoTime();
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try {
            realm.insert(messages);
        } finally {
            realm.commitTransaction();
        }
        realm.close();

        return (System.nanoTime() - start);
    }

    @Override
    public long readSingleData() throws SQLException {
        long start = System.nanoTime();

        realm = Realm.getDefaultInstance();
        for (int i = 0; i < NUM_MESSAGE_QUERY; i++) {
            Message singleMessageResult = realm.where(Message.class).equalTo("intField", i).findFirst();
            int intField = singleMessageResult.getIntField();
            long longField = singleMessageResult.getLongField();
            double doubleField = singleMessageResult.getDoubleField();
            String stringField = singleMessageResult.getStringField();
        }
        realm.close();

        return (System.nanoTime() - start);
    }

    @Override
    public long readBatchData() throws SQLException {
        long start = System.nanoTime();

        realm = Realm.getDefaultInstance();
        RealmResults<Message> messageResults = realm.where(Message.class).greaterThan("intField", (NUM_BATCH_QUERY)).findAll();
        for (Message message : messageResults) {
            int intField = message.getIntField();
            long longField = message.getLongField();
            double doubleField = message.getDoubleField();
            String stringField = message.getStringField();
        }
        realm.close();

        return (System.nanoTime() - start);
    }

    @Override
    public long readWholeData() throws SQLException {
        long start = System.nanoTime();

        realm = Realm.getDefaultInstance();
        RealmResults<Message> messageResults = realm.where(Message.class).findAll();
        for (Message message : messageResults) {
            int intField = message.getIntField();
            long longField = message.getLongField();
            double doubleField = message.getDoubleField();
            String stringField = message.getStringField();
        }
        realm.close();

        return (System.nanoTime() - start);
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try {
            realm.deleteAll();
        } finally {
            realm.commitTransaction();
        }

        realm.close();

        return (System.nanoTime() - start);
    }

    @Override
    public String getOrmName() {
        return "Realm";
    }
}