/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.ManufactoringTask;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import com.google.gson.Gson;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * A class describing the simulator for part 2 of the assignment
 */

public class Simulator {


	private static OrderOfWork.Waves[][] listOfwaves;
	private static Warehouse myWareHouse;
	private static WorkStealingThreadPool threadPool;
	private static AtomicBoolean notFinishedWave;
	private static ConcurrentLinkedQueue<Product> simulatorResult;
	private static ManufactoringPlan[] listOfplans;
	/**
	 * Begin the simulation
	 * Should not be called before attachWorkStealingThreadPool()
	 */
	public static ConcurrentLinkedQueue<Product> start() throws InterruptedException {

		threadPool.start();
		simulatorResult = new ConcurrentLinkedQueue<>();

		for (int i = 0; i < listOfwaves.length; i++) { //Starting reading the waves
			int howManyToCreate = 0;
			for (int n = 0 ; n < listOfwaves[i].length; n++){
				 howManyToCreate = howManyToCreate + Integer.parseInt(listOfwaves[i][n].qty);}
			CountDownLatch startSignal = new CountDownLatch(howManyToCreate); //continue the next wave only if the countdownlanch will decrement to zero

			for (int j = 0; j < listOfwaves[i].length; j++) { 	//Starting passing on each product in the current wave
				int qty = Integer.parseInt(listOfwaves[i][j].qty); //quantity to manufacture
				for (int k = 0; k < qty; k++) { //creates qty tasks
						Integer k1 = k;
						long start = Long.parseLong(listOfwaves[i][j].startId) + k1.longValue();
						ManufactoringTask toDo = new ManufactoringTask((listOfwaves[i][j].product), start,myWareHouse); //creating new Manufactoring Task
						simulatorResult.add(toDo.getProduct());
							toDo.getResult().whenResolved(() -> {
							startSignal.countDown();
						});
						threadPool.submit(toDo); //submit the task to the pool
					}
				}
				startSignal.await();
		}
		return simulatorResult;
	}
/*
	attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	@param myWorkStealingTh readPool - the WorkStealingThreadPool which will be used by the simulator

*/
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){

		threadPool = myWorkStealingThreadPool;
	}

	/**
	 *
	 * @param args
	 * @throws InterruptedException
	 * In the main we will read the gson file
	 * we will add the waves
	 * we will add the tools to the wareHouse
	 * we will add the plans to the wareHouse
	 * calls the simulator function
	 */
	public static void main(String [] args) throws InterruptedException {
		ConcurrentLinkedQueue<Product> queue = new ConcurrentLinkedQueue();
		Gson gson = new Gson();

		try {
			BufferedReader buffer = new BufferedReader(new FileReader(args[0]));
			OrderOfWork orderOfWork = gson.fromJson(buffer, OrderOfWork.class);
			System.out.println(orderOfWork.getThreads());
			listOfwaves = orderOfWork.getWaves();
			Simulator.attachWorkStealingThreadPool(new WorkStealingThreadPool(Integer.parseInt(orderOfWork.getThreads())));
			OrderOfWork.Tools[] listOftools = orderOfWork.getTools();
			myWareHouse = new Warehouse();
			for (int i = 0; i < listOftools.length; i++) {
				String tool = (String) listOftools[i].tool;
				int qty = Integer.parseInt(listOftools[i].qty);
				switch (tool) {
					case "gs-driver":
						myWareHouse.addTool(new GcdScrewDriver(), qty);
						break;
					case "np-hammer":
						myWareHouse.addTool(new NextPrimeHammer(), qty);
						break;
					case "rs-pliers":
						myWareHouse.addTool(new RandomSumPliers(), qty);
						break;
				}
			}
			listOfplans = orderOfWork.getPlans();
			for (int i = 0; i < listOfplans.length; i++) {
				String productName = listOfplans[i].getProductName();
				String[] arrayOfTools = listOfplans[i].getTools();
				String[] arrayOfParts = listOfplans[i].getParts();

				ManufactoringPlan p = new ManufactoringPlan(productName, arrayOfParts, arrayOfTools);
				myWareHouse.addPlan(p); //adds the plan to the WareHouse

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		queue = Simulator.start();
		try {
			FileOutputStream fout = new FileOutputStream("result.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(queue);
		} catch (IOException e) {}

		try {
			threadPool.shutdown(); //shutting down the pool after finish using it
		} catch (InterruptedException e) {
		}

	}

	/**
	 * @return The wareHouse in this programme
	 */
	public Warehouse getMyWareHouse(){
		return this.getMyWareHouse();
	}

}