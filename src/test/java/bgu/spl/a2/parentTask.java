package bgu.spl.a2;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

public class parentTask extends Task<Integer>{
	static int numTasks=0;
	public int resolved = 0;
	public Object o;
	Integer result = null;
	public WorkStealingThreadPool myPool;
	boolean notWorks = false;
	public parentTask(Object o, WorkStealingThreadPool pool){
		this.o=o;
		this.myPool = pool;
	}
	@Override
	protected void start()  {
		if(numTasks<=6){
		parentTask p1 = new parentTask(o,myPool);
		numTasks++;
		childTask1 p2 = new childTask1(o);
		numTasks++;
		parentTask p3 = new parentTask(o,myPool);
		numTasks++;
		
     	List<Task<Integer>> tasks = new ArrayList<>();
     	
     	tasks.add(p1);
     	tasks.add(p2);
     	tasks.add(p3);
     	
     	try{
     	p1.complete(8);
     	p2.complete(8);
     	}
     	catch(Exception ex){
     		notWorks=true;
		}
     	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		whenResolved(tasks,()->{
			resolved++;
			try{
			complete(8);
			}
			catch(Exception ex){
				notWorks=true;
			}
		});
		
		}
		else {
			try{
			complete(8);
			}
			catch(Exception ex){
				notWorks=true;
			}
			
		}
		
		
		
		
	}

}
