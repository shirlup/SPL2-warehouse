package bgu.spl.a2.sim;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class that represents a product produced during the simulation.
 */
public class Product implements Serializable {

	long startId;
	AtomicLong finalId;
	String name;
	List<Product> listOfParts;

	/**
	 * Constructor
	 * @param startId - Product start id
	 * @param name - Product name
	 */
    public Product(long startId, String name){

		this.startId = startId;
		this.name = name;
		this.finalId = new AtomicLong(startId);
		this.listOfParts = new LinkedList<>();
	}

	/**
	* @return The product name as a string
	*/
    public String getName(){

    	return name;
	}

	/**
	 * @return The product start ID as a long. start ID should never be changed.
	*/
    public long getStartId(){

    	return startId;
	}
    
	/**
	* @return The product final ID as a long. 
	* final ID is the ID the product received as the sum of all UseOn(); 
	*/
    public long getFinalId(){

    	return finalId.get();
	}

	/**
	* @return Returns all parts of this product as a List of Products
	*/
    public List<Product> getParts(){

    	return listOfParts;
	}

	/**
	* Add a new part to the product
	* @param p - part to be added as a Product object
	*/
    public void addPart(Product p){

    	listOfParts.add(p);
	}

	/**
	Adds the long toAdd to the current finalId
	@param toAdd - value to add
	  */
	public void addToFinallId(long toAdd){

    	this.finalId.addAndGet(toAdd);

	}

}
