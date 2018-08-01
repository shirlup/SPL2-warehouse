package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.concurrent.atomic.AtomicLong;

public class NextPrimeHammer implements Tool{

    /**
     * @param id
     * @return return the next prime number after the given id
     */
    public long func(long id) {

        long v =id + 1;
        while (!isPrime(v)) {
            v++;
        }
        return v;
    }
    /**
     * @param value
     * @return true if a number is prime
     */
    private boolean isPrime(long value) {
        if(value < 2) return false;
        if(value == 2) return true;
        long sq = (long) Math.sqrt(value);
        for (long i = 2; i <= sq; i++) {
            if (value % i == 0) {
                return false;
            }
        }

        return true;
    }
    /**
     *
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
        return "NextPrimeHammer";
    }
}
