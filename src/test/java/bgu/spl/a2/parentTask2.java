package bgu.spl.a2;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;


public class parentTask2 extends Task<Integer>{
	static int numTasks=0;
	public int resolved = 0;
	public Object o;
	Integer result = null;
	public WorkStealingThreadPool myPool;
	boolean notWorks = false;
	public parentTask2(Object o, WorkStealingThreadPool pool){
		this.o=o;
		this.myPool = pool;
	}
	@Override
	protected void start()  {
		
		if(numTasks<=3){
		parentTask2 p1 = new parentTask2(o,myPool);
		numTasks++;
		childTask1 p2 = new childTask1(o);
		numTasks++;
		parentTask2 p3 = new parentTask2(o,myPool);
		numTasks++;
		numTasks=3;
     	List<Task<Integer>> tasks = new ArrayList<>();
     	tasks.add(p1);
     	tasks.add(p3);
  
     
 
     	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	p1.complete(8);
     	p3.complete(8);
		whenResolved(tasks,()->{
			resolved++;
			try{
			if(p1.getResult().get()==8)
				resolved++;
			}
			catch(Exception ex){
				
			}
			try{
			if(p2.getResult().get()==8)
				resolved++;
			}
			catch(Exception ex){
				
			}
			try{
			if(p3.getResult().get()==8)
				resolved++;
			}
			catch(Exception ex){
				
			}
			try{
			p2.complete(8);
			}
			catch(Exception ex){
				notWorks = true;
			}
			tasks.clear();
			tasks.add(p2);
			whenResolved(tasks,()->{
				try{
				if(p2.getResult().get()==8)
					resolved++;
				}
				catch(Exception ex){
					
				}
				try{
				complete(resolved);
				}
				catch(Exception ex){
					notWorks = true;
				}
			});
			
		});
		
		}
		else {
			try{
			complete(8);
			}
			catch(Exception ex){
				notWorks = true;
			}
		}
		
		
		
		
	}
	}
	


