package logic.map.operation;

public class OperationSwap implements Operation{

	private int i;
	
	private int j;

	public OperationSwap(int i, int j) {
		super();
		this.i = i;
		this.j = j;
	}



	public int getI() {
		return i;
	}



	public int getJ() {
		return j;
	}



	public void setI(int i) {
		this.i = i;
	}



	public void setJ(int j) {
		this.j = j;
	}



	@Override
	public OperationType getOperationType() {
		return OperationType.SWAP;
	}



	@Override
	public boolean equals(Object o) {
		if (o instanceof OperationSwap)
			return (i == ((OperationSwap)o).getI() && j == ((OperationSwap)o).getJ())
					|| (i == ((OperationSwap)o).getJ() && j == ((OperationSwap)o).getI());
		return false;
	}

	
}
