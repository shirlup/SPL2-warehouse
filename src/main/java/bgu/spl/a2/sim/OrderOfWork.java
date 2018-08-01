package bgu.spl.a2.sim;

import bgu.spl.a2.sim.conf.ManufactoringPlan;
import com.google.gson.internal.LinkedTreeMap;
import java.util.List;
import java.util.TreeMap;

/**
 * This class has been created to get all the gson inputs and put it in sort of ORDER OF WORK
 */
public class OrderOfWork {

    private String threads;
    private Tools[] tools;
     ManufactoringPlan[] plans;
    private Waves[][] waves;

    /**
     * @return Returns number of Threads
     */
    public String getThreads() {
        return threads;
    }

    /**
     * @return Returns an array of given waves
     */
    public Waves[][] getWaves() {
        return waves;
    }

    /**
     * @return Returns an array of tools that should be added
     */
    public Tools[] getTools(){
        return tools;
    }

    /**
     * @return Returns an array of plans that should be added
     */
    public ManufactoringPlan[] getPlans() {
        return plans;
    }

    /*
    *Class represent the tools
     */
    public class Tools{

        String tool;
        String qty;
    }

    /*
    *Class represent the waves
     */
    public class Waves{

     String product;
     String qty;
     String startId;

    }
}
