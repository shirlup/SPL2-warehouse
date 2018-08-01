package bgu.spl.a2;

public class childTask1 extends Task<Integer> {
	public Object o;
	Integer res = null;
	public childTask1(Object o){
		this.o=o;
	}
	@Override
	protected void start() {
		synchronized(o){
		try {
			o.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		complete(8);
		
	}

}
