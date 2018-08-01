package bgu.spl.a2.sim;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;

public class WarehouseTest {

	@Test
	public void testAcquireTool1() throws InterruptedException {
		try{
		Warehouse w = new Warehouse();
		AtomicInteger counter = new AtomicInteger(0);
		Thread t1 = new Thread( ()-> {
			w.acquireTool("np-hammer");
			assertTrue(true);
			counter.incrementAndGet();
		});
		t1.start();
		Thread.sleep(100);
		assertEquals(counter.get(),1);
		}
		catch(Exception ex){
			Assert.fail();
		}
	}
	

	@Test
	public void testAcquireTool3() throws InterruptedException{
		try{
		Warehouse w = new Warehouse();
		AtomicInteger counter = new AtomicInteger(0);
		Thread t1 = new Thread( ()-> {
			Deferred<Tool> t = w.acquireTool("np-hammer");
			t.whenResolved(()->{
				counter.incrementAndGet();
			});
		});
		t1.start();
		Thread.sleep(100);
		assertEquals(counter.get(),0);
	}
		catch(Exception ex){
			Assert.fail();
		}
	}
	@Test
	public void testReleaseTool1() throws InterruptedException {
		try{
		Warehouse w = new Warehouse();
		AtomicInteger counter = new AtomicInteger(0);
		Thread t1 = new Thread( ()-> {
			Deferred<Tool> t = w.acquireTool("np-hammer");
			t.whenResolved(()->{
				counter.incrementAndGet();
			});
		});
		
		t1.start();
		Thread.sleep(100);
		new Thread(()->{w.releaseTool(new NextPrimeHammer() );}).start();
		
		Thread.sleep(100);
		assertEquals(counter.get(),1);
		
	}
		catch(Exception ex){
			Assert.fail();
		}
	}
	
	@Test
	public void testReleaseTool2() throws InterruptedException {
		try{
		Warehouse w = new Warehouse();
		AtomicInteger counter = new AtomicInteger(0);
		Thread t1 = new Thread( ()-> {
			Deferred<Tool> t = w.acquireTool("np-hammer");
			t.whenResolved(()->{
				counter.incrementAndGet();
			});
		});
		
		t1.start();
		Thread.sleep(100);
		new Thread(()->{w.releaseTool(new RandomSumPliers() );}).start();
		Thread.sleep(100);
		assertEquals(counter.get(),0);
		
	}
		catch(Exception ex){
			Assert.fail();
		}
	}
}
