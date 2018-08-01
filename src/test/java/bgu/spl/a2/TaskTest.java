package bgu.spl.a2;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import junit.framework.Assert;

public class TaskTest {

	@Test
	public void testTaskWhenResolved1() throws InterruptedException {
		try{
		WorkStealingThreadPool pool= new WorkStealingThreadPool(20);
		
		Object o = new Object();
		parentTask p = new parentTask(o,pool);
		
		AtomicInteger errors = new AtomicInteger(0);
		Thread t1 = new Thread(()->{
			try{
				pool.start();
			}
			catch(Exception ex){
				errors.incrementAndGet();
			}
		});
				
		t1.start();
		Thread.sleep(10000);
		pool.submit(p);
		Thread.sleep(10000);
		assertEquals(p.resolved,0);
		assertEquals(errors.get(),0);
		assertFalse(p.notWorks);
		
		}
		catch(Exception ex){
			Assert.fail();
		}
		
	}
	
}
