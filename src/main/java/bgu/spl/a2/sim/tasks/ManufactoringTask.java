package bgu.spl.a2.sim.tasks;
import bgu.spl.a2.Deferred;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ****Class ManufactoringTask extends Task****
 * This class represent an object which is a product who needs to be created
 */
public class ManufactoringTask extends Task<Product> {

    //Fields
    private String productName;
    private ManufactoringPlan productPlan;
    private AtomicLong startId = new AtomicLong();
    private Product product;
    private List<ManufactoringTask> partsList;
    private List<String> productTools;
    private AtomicInteger numOfTools = new AtomicInteger();
    private Warehouse warehouse;

    //Constructor
    public ManufactoringTask(String productName, long startId, Warehouse warehouse) {

        this.partsList = new LinkedList<>();
        this.productTools = new LinkedList<>();
        this.productName = productName;
        this.startId.set(startId);
        this.product = new Product(startId, productName);
        this.productPlan = warehouse.getPlan(productName);
        this.warehouse = warehouse;
    }

    /*
    This method starts the executing of the current task
     */
    protected void start() {

        //adds the required tools to the product's tool list
        for (String toolName : productPlan.getTools())
            productTools.add(toolName);
        numOfTools.set(productTools.size());

        //Checks if there are sub parts to create to construct the current product
        if (productPlan.getParts().length > 0) {
            for (String partName : productPlan.getParts()) {
                ManufactoringTask taskToAdd = new ManufactoringTask(partName, startId.get() + 1,warehouse);
                partsList.add(taskToAdd);
                //**spawn all the sub tasks**
                spawn(taskToAdd);
            }

            //sending all subTasks to whenResolved.
            //after resolving will enter the lambda
           whenResolved(partsList, () -> {

               for (ManufactoringTask childPartList : partsList) {
                   this.product.addPart(childPartList.getResult().get());
               }

                for (String toolName : productTools) {
                    Deferred<Tool> promise = warehouse.acquireTool(toolName);
                    promise.whenResolved(() -> {
                        Long add = promise.get().useOn(product);
                       this.product.addToFinallId(add);
                       numOfTools.decrementAndGet();
                       ReleaseTool releaseTool = new ReleaseTool(warehouse,promise);
                       spawn(releaseTool);
                    });
                }
                complete(product);
            });

            } else {
            complete(product);
        }
    }


    //getter
    public Product getProduct(){
       return product;
    }
}