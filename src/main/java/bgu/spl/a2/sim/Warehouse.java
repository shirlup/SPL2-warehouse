package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;
//import com.sun.java.util.jar.pack.Instruction;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class representing the warehouse in your simulation
 * 
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 */
public class Warehouse {

	public AtomicInteger nextPrimeHammerCounter;
	public AtomicInteger gcdScrewDriverCounter;
	public AtomicInteger randomSumPliersCounter;
	private ConcurrentLinkedDeque<Deferred<Tool>> randomSumPliersQueue;
	private ConcurrentLinkedDeque<Deferred<Tool>> gcdScrewDriversQueue;
	private ConcurrentLinkedDeque<Deferred<Tool>> nextPrimeHammersQueue;
	private HashMap<String,ManufactoringPlan> mapOfPlans;
	/**
	* Constructor
	*/
    public Warehouse(){

		 nextPrimeHammerCounter = new AtomicInteger(0);
		 gcdScrewDriverCounter = new AtomicInteger(0);
		 randomSumPliersCounter = new AtomicInteger(0);
		 nextPrimeHammersQueue = new ConcurrentLinkedDeque<>();
		 gcdScrewDriversQueue = new ConcurrentLinkedDeque<>();
		 randomSumPliersQueue = new ConcurrentLinkedDeque<>();
		 mapOfPlans = new HashMap<>();
	}

	/**
	* Tool acquisition procedure
	* Note that this procedure is non-blocking and should return immediatly
	* @param type - string describing the required tool
	* @return a deferred promise for the  requested tool
	 * This method is synchronized because it depended on the number of tools in the warehouse
	 * one thread can change the number of tools while another one asks for a tool and the counter will be a falsed counter
	*/

    public Deferred<Tool> acquireTool(String type) {

		switch (type) {
			case "gs-driver":
				synchronized (this) {
					Deferred<Tool> deferredTool = new Deferred();
					if (gcdScrewDriverCounter.get() > 0) {
						deferredTool.resolve(new GcdScrewDriver());
					} else this.gcdScrewDriversQueue.addLast(deferredTool);
					return deferredTool;
				}
			case "np-hammer":
				synchronized (this) {
					Deferred<Tool> deferredTool1 = new Deferred();
					if (nextPrimeHammerCounter.get() > 0) {
						deferredTool1.resolve(new NextPrimeHammer());

					} else this.nextPrimeHammersQueue.addLast(deferredTool1);
					return deferredTool1;
				}
				//RandomSumPliers
			default:
				synchronized (this) {
					Deferred<Tool> deferredTool2 = new Deferred();
					if (randomSumPliersCounter.get() > 0) {
						deferredTool2.resolve(new RandomSumPliers());
					} else this.randomSumPliersQueue.addLast(deferredTool2);
					return deferredTool2;
				}
		}
	}

	/**
	* Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
	* @param tool - The tool to be returned
	*We used three locks to synchronized because it depended on the number of tools in the warehouse
	 */
    public void releaseTool(Tool tool) {
			switch (tool.getType()) {
				case "GcdScrewDriver":
					synchronized (this) {
						gcdScrewDriverCounter.incrementAndGet();
						Deferred<Tool> deftool = gcdScrewDriversQueue.pollFirst();
						if (deftool != null) deftool.resolve(new GcdScrewDriver());
						break;
					}
			case "NextPrimeHammer":
				synchronized (this) {
					nextPrimeHammerCounter.incrementAndGet();
					Deferred<Tool> deftool1 = gcdScrewDriversQueue.pollFirst();
					if (deftool1 != null) deftool1.resolve(new NextPrimeHammer());
					break;
				}
				//RandomSumPliers
			default:
				synchronized (this){
				randomSumPliersCounter.incrementAndGet();
				Deferred<Tool> deftool2 = gcdScrewDriversQueue.pollFirst();
				if(deftool2 != null) deftool2.resolve(new RandomSumPliers());
				break;
			}
			}
	}
	
	/**
	* Getter for ManufactoringPlans
	* @param product - a string with the product name for which a ManufactoringPlan is desired
	* @return A ManufactoringPlan for product
	*/
    public ManufactoringPlan getPlan(String product){
		return mapOfPlans.get(product);
	}
	
	/**
	* Store a ManufactoringPlan in the warehouse for later retrieval
	* @param plan - a ManufactoringPlan to be stored
	*/
    public void addPlan(ManufactoringPlan plan){

		mapOfPlans.put(plan.getProductName(),plan);
	}
    
	/**
	* Store a qty Amount of tools of type tool in the warehouse for later retrieval
	* @param tool - type of tool to be stored
	* @param qty - amount of tools of type tool to be stored
	*/
    public void addTool(Tool tool, int qty){

    	switch (tool.getType()){

			case "GcdScrewDriver":
				gcdScrewDriverCounter.addAndGet(qty);
				break;

			case "NextPrimeHammer":
				nextPrimeHammerCounter.addAndGet(qty);
				break;

				//RandomSumPliers
			default:
				randomSumPliersCounter.addAndGet(qty);
				break;

		}
	}

}
