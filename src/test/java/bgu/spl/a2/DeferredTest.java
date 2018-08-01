package bgu.spl.a2;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import junit.framework.Assert;

public class DeferredTest {

	@Test
	
	public void testGet1() {
		try{
		Deferred<Integer> d = new Deferred<>();
		d.resolve(5);
		int x= d.get();
		assertEquals(x,5);
		}
		catch(Exception ex){
			Assert.fail();
		}
}
	@Test
	public void testGet2() {
		Deferred<Integer> d = null;
		try{
		 d = new Deferred<>();
		}
		catch(Exception ex){
			Assert.fail();
		}
		try{
		int x= d.get();
		Assert.fail();
		}
		catch(IllegalStateException ex){
		
		}
		catch(Exception ex){
			Assert.fail();
		}
	}
	
	@Test
	public void testResolve1() throws InterruptedException{
		try{
		Deferred<Integer> d = new Deferred<>();
		AtomicInteger counter = new AtomicInteger(0);
		AtomicInteger errors = new AtomicInteger(0);
		for(int i= 0 ; i < 8 ; i++){
			Thread t1 = new Thread(()->{
				try{
					d.whenResolved(() -> {
			            counter.incrementAndGet();
			          
			        });
				}
				catch(Exception ex){
					errors.incrementAndGet();
				}
				
				}
			);
	
				
		t1.start();
	}
		d.resolve(5);
		Thread.sleep(10000);
		assertEquals(counter.get(),8);
		assertEquals(errors.get(),0);
	}
		catch(Exception ex){
			assertFalse(true);
		}
	}
	

	@Test
	public void testResolve2(){
		try{
		Deferred<Integer> d = new Deferred<>();
		d.resolve(5);
		try{
		d.resolve(6);
		Assert.fail();
		}
		catch(IllegalStateException ex){
			int x= d.get();
			assertEquals(x,5);
		}
		catch(Exception ex){
			Assert.fail();
		}
	}
		catch(Exception ex){
			Assert.fail();
		}
	}
	
	@Test
	public void testWhenResolved1() throws InterruptedException{
		try{
		Deferred<Integer> d = new Deferred<>();
		d.resolve(5);
		AtomicInteger counter = new AtomicInteger(0);
		AtomicInteger errors = new AtomicInteger(0);
		Thread t1 = new Thread(()->{
			try{
				for(int i= 0 ; i < 5 ; i++){
					d.whenResolved(() -> {
			            counter.incrementAndGet();
			        });
				}
			}
			catch(Exception ex){
				
			}
		});
	
		t1.start();
		
		Thread.sleep(10000);
		assertEquals(counter.get(),5);
		assertEquals(errors.get(),0);
	}
		catch(Exception ex){
			Assert.fail();
		}
	}
	@Test
	public void testWhenResolved2() throws InterruptedException{
		try{
		Deferred<Integer> d = new Deferred<>();
		AtomicInteger counter = new AtomicInteger(0);
		AtomicInteger errors = new AtomicInteger(0);
		Thread t1 = new Thread(()->{
			try{
				long mainThreadId = Thread.currentThread().getId();
				d.whenResolved(() -> {
				long currentThreadId = (int) Thread.currentThread().getId();
	            counter.incrementAndGet();
	            if(currentThreadId!=mainThreadId)
	            	errors.incrementAndGet();
	        
			});
			}
			catch(Exception ex){
				errors.incrementAndGet();
			}
		});
	
		t1.start();
		Thread.sleep(100);
		assertEquals(errors.get(),0);
}
		catch(Exception ex){
			Assert.fail();
		}
	}
	
	
	@Test
	public void testwhenResolved3() throws InterruptedException{
		//checks that whenResolved is not blocking
		try{
		Deferred<Integer> d = new Deferred<>();
		AtomicInteger counter = new AtomicInteger(0);
		AtomicInteger errors = new AtomicInteger(0);
		Thread t1 = new Thread(()->{
			try{
				d.whenResolved(() -> {
		            
		        });
				 counter.incrementAndGet();
			}
			catch(Exception ex){
				errors.incrementAndGet();
			}
		});
		t1.start();
		Thread.sleep(100);
		assertEquals(counter.get(),1);
		assertEquals(errors.get(),0);
	}
		catch(Exception ex){
			Assert.fail();
		}
	}
	
	@Test
	public void testIsResolved() {
		try{
		Deferred<Integer> d = new Deferred<>();
		assertFalse(d.isResolved());
		d.resolve(5);
		assertTrue(d.isResolved());
	}
		catch(Exception ex){
			Assert.fail();
		}
	}
	
	
	
	
	

}
