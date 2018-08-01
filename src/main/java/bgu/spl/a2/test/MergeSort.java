/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this t
 * emplate file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;
    private List<Task<int[]>> tasks ;

    public MergeSort(int[] array) {
        this.array = array;
        this.tasks = new ArrayList<>();
    }

    /**
     * starts the process of the MergeSort
     */
    protected void start() {
        if(array.length != 1){

            int[] leftArray = new int [array.length/2];
            for(int i = 0; i < leftArray.length; i++)
                leftArray[i] = array[i];

            int[] rightArray = new int[array.length - array.length/2];
            for(int i = 0; i < rightArray.length; i++)
                rightArray[i] = array[i+array.length/2];

            //crating new subTasks
            MergeSort leftArraySubTask = new MergeSort(leftArray);
            MergeSort rightArraySubTask = new MergeSort(rightArray);
            tasks.add(leftArraySubTask);
            tasks.add(rightArraySubTask);


            Runnable callback = ()->{
                this.merge();
            };
            //after creating subTasks we want to know when they resolve
            this.whenResolved(tasks, callback);

            spawn(leftArraySubTask,rightArraySubTask);
        }
        //else the size of array is 1 and it is the result
        else complete(array);
    }

    private void merge() {
        int[] auxArray = new int[array.length];
        int i = 0;
        int j = 0; //i1
        int k = 0;//i2
        int size1 = tasks.get(0).getResult().get().length;
        int size2= tasks.get(1).getResult().get().length;

        while(j < size1 && k < size2){
            if(tasks.get(0).getResult().get()[j] < tasks.get(1).getResult().get()[k]){
                auxArray[i] = tasks.get(0).getResult().get()[j];
                j++;
            } else {
                auxArray[i] = tasks.get(1).getResult().get()[k];
                k++;
            }
            i++;
        }
        for(int n = j; n < size1; n++){
            auxArray[i] = tasks.get(0).getResult().get()[n];
            i++;
        }
        for(int m = k; m < size2; m++){
            auxArray[i] = tasks.get(1).getResult().get()[m];
            i++;
        }
        complete(auxArray);

    }

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 100; i++) {
            WorkStealingThreadPool pool = new WorkStealingThreadPool(5);
            int n = 10; //you may check on different number of elements if you like
            int[] array = new Random().ints(n).toArray();

            MergeSort task = new MergeSort(array);

            CountDownLatch l = new CountDownLatch(1);
            pool.start();
            pool.submit(task);
            task.getResult().whenResolved(() -> {
                //warning - a large print!! - you can remove this line if you wish
                System.out.println(Arrays.toString(task.getResult().get()));
                l.countDown();
            });

            l.await();
            pool.shutdown();
        }
    }
}