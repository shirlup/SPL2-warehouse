package bgu.spl.a2.sim;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import bgu.spl.a2.WorkStealingThreadPool;
import junit.framework.Assert;

public class SimulatorTest {

	@Test
	public void simulatorTest() throws InterruptedException {
		try{
		String args[] = {"simulation.json"};
		AtomicInteger errors = new AtomicInteger(0);
		
		Thread t1 = new Thread(()->{
			try{
			   Simulator.main(args);
			}
			catch(Exception ex){
				errors.incrementAndGet();
			}
			
		});
	
		t1.start();
		Thread.sleep(10000);
		WorkStealingThreadPool pool = new WorkStealingThreadPool(1);
		Simulator.attachWorkStealingThreadPool(pool);
		Thread t2 = new Thread(()->{
			try{
				Simulator.start();
				errors.incrementAndGet();
				}
				catch(Exception ex){
					errors.incrementAndGet();
				}
				
		});
		t2.start();
		
		Thread.sleep(10000);
		simTask s = new simTask();
		pool.submit(s);
		
			CountDownLatch l = new CountDownLatch(1);
		
			Thread.sleep(10000);
			
	        s.getResult().whenResolved(() -> {      
	        });
	
	        assertEquals(s.getResult().get().intValue(),5);	
	        assertEquals(errors.get(),0);	
			
	 
	}
	catch(Exception ex){
		assertFalse(true);
	}

	}
	

}
