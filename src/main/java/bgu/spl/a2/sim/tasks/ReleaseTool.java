package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;

/**
 * This class represent the calling for the action of releasing tool from the warehouse
 */
public class ReleaseTool extends Task<Product> {
    protected Warehouse warehouse;
    protected Deferred<Tool> deferredTool;


    //Constructor
    public ReleaseTool(Warehouse warehouse,Deferred deferredTool){
        this.warehouse = warehouse;
        this.deferredTool = deferredTool;
    }

    //Start function, calling to release the given tool
    public void start(){

        warehouse.releaseTool(deferredTool.get());
    }



}
