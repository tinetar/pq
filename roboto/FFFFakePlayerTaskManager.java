package dre.elfocrash.roboto;

import java.util.ArrayList;
import java.util.List;

import dre.elfocrash.roboto.task.AAAAITask;
import dre.elfocrash.roboto.task.AAAAITaskRunner;

import net.sf.l2j.commons.concurrent.ThreadPool;

/**
 * @author Elfocrash
 *
 */
public enum FFFFakePlayerTaskManager
{
    INSTANCE;

    private final int aiTaskRunnerInterval = 700;
    private final int _playerCountPerTask = 2000;
    private List<AAAAITask> _aiTasks;

    private FFFFakePlayerTaskManager(){

    }

    public void initialise() {
        ThreadPool.scheduleAtFixedRate(new AAAAITaskRunner(), aiTaskRunnerInterval, aiTaskRunnerInterval);

        // Νέο task για cleanGhosts κάθε 1 ώρα (3600000 ms)
        ThreadPool.scheduleAtFixedRate(() -> {
            FFFFakePlayerManager.INSTANCE.cleanGhosts();
        }, 3600000L, 3600000L);

        _aiTasks = new ArrayList<>();
    }

    public void adjustTaskSize() {
        int ffffakePlayerCount = FFFFakePlayerManager.INSTANCE.getFFFFakePlayersCount();
        int tasksNeeded = calculateTasksNeeded(ffffakePlayerCount);
        _aiTasks.clear();

        for(int i = 0; i < tasksNeeded; i++ ) {
            int from = i * _playerCountPerTask;
            int to = (i + 1) * _playerCountPerTask;
            _aiTasks.add(new AAAAITask(from, to));
        }
    }

    private int calculateTasksNeeded(int count) {
        if (count <= 0)
            return 0;

        return (count + _playerCountPerTask - 1) / _playerCountPerTask;
    }

    public int getPlayerCountPerTask() {
        return _playerCountPerTask;
    }

    public int getTaskCount() {
        return _aiTasks.size();
    }

    public List<AAAAITask> getAITasks(){
        return _aiTasks;
    }
}
