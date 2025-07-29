package dre.elfocrash.roboto.task;

import java.util.List;

import dre.elfocrash.roboto.FFFFakePlayerTaskManager;

import net.sf.l2j.commons.concurrent.ThreadPool;

/**
 * @author Elfocrash
 *
 */
public class AAAAITaskRunner implements Runnable
{	
	@Override
	public void run()
	{		
		FFFFakePlayerTaskManager.INSTANCE.adjustTaskSize();
		List<AAAAITask> aiTasks = FFFFakePlayerTaskManager.INSTANCE.getAITasks();		
		aiTasks.forEach(aiTask -> ThreadPool.execute(aiTask));
	}	
}
