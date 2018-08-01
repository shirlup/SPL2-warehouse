package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RandomSumPliers implements Tool {

    /**
     * @param id
     * @return random number as requested
     */
    public long func(long id){
        Random r = new Random(id);
        long  sum = 0;
        for (long i = 0; i < id % 10000; i++) {
            sum += r.nextInt();
        }

        return sum;
    }

    /**
     * @param p - Product to use tool on
     * @return the value who needs to be added the the final id
     */
        public long useOn(Product p){
            AtomicLong value = new AtomicLong(0);
            for(Product part : p.getParts()){
                value.addAndGet(Math.abs(func(part.getFinalId())));
            }
            return value.get();
        }
    /**
     * @return The type of the tool
     */
    public String getType(){
        return "RandomSumPliers";
    }

}
