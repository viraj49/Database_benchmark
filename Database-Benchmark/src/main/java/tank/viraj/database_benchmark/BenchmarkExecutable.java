package tank.viraj.database_benchmark;

import android.content.Context;

import java.sql.SQLException;

public interface BenchmarkExecutable {
    int NUM_MESSAGE_INSERTS = 20000;
    int NUM_MESSAGE_QUERY = 2000;
    int NUM_BATCH_QUERY = 2000;

    enum Task {
        CREATE_DB, WRITE_DATA, READ_DATA, READ_INDEXED, READ_SEARCH, DROP_DB;
    }

    String getOrmName();

    void init(Context context, boolean useInMemoryDb);

    long createDbStructure() throws SQLException;

    long writeWholeData() throws SQLException;

    long readSingleData() throws SQLException;

    long readBatchData() throws SQLException;

    long readWholeData() throws SQLException;

    long dropDb() throws SQLException;
}
