package bgu.spl.a2.sim.conf;

import bgu.spl.a2.Task;

/**
 * a class that represents a manufacturing plan.
 *
 **/
public class ManufactoringPlan  {
	/** ManufactoringPlan constructor
	* @param product - product name
	* @param parts - array of strings describing the plans part names
	* @param tools - array of strings describing the plans tools names
	*/

	String product;
	String[] parts;
	String[] tools;

    public ManufactoringPlan(String product, String[] parts, String[] tools){
		this.product = product;
		this.parts = parts.clone();
		this.tools = tools.clone();
	}

	/**
	* @return array of strings describing the plans part names
	*/
    public String[] getParts(){

    	return parts;
	}

	/**
	* @return string containing product name
	*/
    public String getProductName(){

    	return product;
	}
	/**
	* @return array of strings describing the plans tools names
	*/
    public String[] getTools(){

    	return tools;
	}

}
