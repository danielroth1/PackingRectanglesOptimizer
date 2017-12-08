package logic.map.operation;


public interface Operation {

	public abstract OperationType getOperationType();
	
	public boolean equals(Object o);
}
