package bgu.spl.a2.sim.tools;
import bgu.spl.a2.sim.Product;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GcdScrewDriver implements Tool {

    /**
    *@param id - gets the final id
     *@return the new id
     */
    public long func(long id){
        BigInteger b1 = BigInteger.valueOf(id);
        BigInteger b2 = BigInteger.valueOf(reverse(id));
        long value= (b1.gcd(b2)).longValue();
        return value;
    }

    /**
     * @param n id
     * @return the reverse of the id as requested
     */
    public long reverse(long n){
        long reverse=0;
        while( n != 0 ){
            reverse = reverse * 10;
            reverse = reverse + n%10;
            n = n/10;
        }
        return reverse;
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
        return "GcdScrewDriver";
    }
}

