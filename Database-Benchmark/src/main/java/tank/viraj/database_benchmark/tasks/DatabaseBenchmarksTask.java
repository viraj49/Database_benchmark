package tank.viraj.database_benchmark.tasks;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import tank.viraj.database_benchmark.BenchmarkExecutable;
import tank.viraj.database_benchmark.cupboard.CupboardExecutor;
import tank.viraj.database_benchmark.dbflow.DBFlowExecutor;
import tank.viraj.database_benchmark.greendao.GreenDaoExecutor;
import tank.viraj.database_benchmark.ormlite.ORMLiteExecutor;
import tank.viraj.database_benchmark.realm.RealmExecutor;
import tank.viraj.database_benchmark.requery.RequeryExecutor;
import tank.viraj.database_benchmark.sqlite.SQLiteExecutor;
import tank.viraj.database_benchmark.sqliteoptimized.OptimizedSQLiteExecutor;
import tank.viraj.database_benchmark.squeaky.SqueakyExecutor;
import tank.viraj.database_benchmark.squeakyfinal.SqueakyFinalExecutor;
import tank.viraj.database_benchmark.squidb.SquidbExecutor;
import tank.viraj.database_benchmark.sugarorm.SugarOrmExecutor;

/**
 * Created by kgalligan, 10/24/15.
 * Edited by Viraj Tank, 10/08/16.
 */
public class DatabaseBenchmarksTask extends Task {
    private static final boolean USE_IN_MEMORY_DB = false;
    private static final int NUM_ITERATIONS = 3;

    private BenchmarkExecutable[] ormList = new BenchmarkExecutable[]{
            new CupboardExecutor(),
            new DBFlowExecutor(),
            new GreenDaoExecutor(),
            new RealmExecutor(),
            new RequeryExecutor(),
            new SugarOrmExecutor(),
            new SqueakyExecutor(),
            new SqueakyFinalExecutor(),
            new SquidbExecutor(),
            new SQLiteExecutor(),
            new OptimizedSQLiteExecutor(),
            new ORMLiteExecutor()
    };

    public String resultString;

    private Map<String, Map<String, Long>> benchmarkResults = new TreeMap<>();

    private enum BenchmarkTask {
        CREATE_DB,
        WRITE_WHOLE_DATA,
        READ_SINGLE_DATA,
        READ_BATCH_DATA,
        READ_WHOLE_DATA,
        DROP_DB
    }

    @Override
    protected void run(Context context) throws Throwable {
        for (BenchmarkExecutable orm : ormList) {
            orm.init(context, USE_IN_MEMORY_DB);
        }

        for (int i = 0; i < NUM_ITERATIONS; i++) {
            for (BenchmarkExecutable item : ormList) {
                sendStatusUpdate("Iteration: " + (i + 1) + "/" + item.getOrmName() + " running");

                for (BenchmarkTask task : BenchmarkTask.values()) {
                    long result = 0;

                    try {
                        switch (task) {
                            case CREATE_DB:
                                result = item.createDbStructure();
                                break;
                            case WRITE_WHOLE_DATA:
                                result = item.writeWholeData();
                                break;
                            case READ_WHOLE_DATA:
                                result = item.readWholeData();
                                break;
                            case READ_SINGLE_DATA:
                                result = item.readSingleData();
                                break;
                            case READ_BATCH_DATA:
                                result = item.readBatchData();
                                break;
                            case DROP_DB:
                                result = item.dropDb();
                                break;
                        }
                    } catch (Exception e) {
                        result = Long.MIN_VALUE;
                    }

                    addProfilerResult(item.getOrmName(), task, result);
                }
                sendStatusUpdate("Iteration: " + (i + 1) + "/" + item.getOrmName() + " complete");
            }
        }
        buildResultString();
    }

    private void sendStatusUpdate(String status) throws InterruptedException {
        EventBusExt.getDefault().post(new StatusUpdate(status));

        //Let the main thread breath and update status
        Thread.sleep(200);
    }

    public static class StatusUpdate {
        public final String status;

        StatusUpdate(String status) {
            this.status = status;
        }
    }

    private void buildResultString() {
        StringBuilder sb = new StringBuilder();

        BenchmarkTask[] bTasks = new BenchmarkTask[]{BenchmarkTask.CREATE_DB,
                BenchmarkTask.WRITE_WHOLE_DATA, BenchmarkTask.READ_WHOLE_DATA,
                BenchmarkTask.READ_SINGLE_DATA, BenchmarkTask.READ_BATCH_DATA,
                BenchmarkTask.DROP_DB};
        for (BenchmarkTask bTask : bTasks) {
            sb.append("<b>").append(bTask.name()).append("</b>").append("<br/>");
            Map<String, Long> stringLongMap = benchmarkResults.get(bTask.name());
            stringLongMap = sortHashMapByValues(stringLongMap);
            if (stringLongMap != null) {
                for (String ormName : stringLongMap.keySet()) {

                    long result = stringLongMap.get(ormName);
                    double printResult = ((double) result / (double) NUM_ITERATIONS) / ((double) 1000000);
                    sb.append(ormName).append(" - ");

                    if (printResult < 0) {
                        sb.append("(crashed)");
                    } else {
                        sb.append(Math.round(printResult)).append("ms");
                    }

                    sb.append("<br/>");
                }
            }
        }

        resultString = sb.toString();
    }

    private void addProfilerResult(String ormName, BenchmarkTask task, long result) {
        Map<String, Long> taskMap = benchmarkResults.get(task.name());
        if (taskMap == null) {
            taskMap = new TreeMap<>();
            benchmarkResults.put(task.name(), taskMap);
        }
        Long storedResult = taskMap.get(ormName);
        if (storedResult == null) {
            storedResult = 0L;
        }
        long value = result + storedResult;
        taskMap.put(ormName, value);
    }

    @Override
    protected boolean handleError(Context context, Throwable e) {
        return false;
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);
    }

    private LinkedHashMap<String, Long> sortHashMapByValues(Map<String, Long> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Long> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();

        for (Long val : mapValues) {
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Long comp1 = passedMap.get(key);

                if (comp1.equals(val)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
