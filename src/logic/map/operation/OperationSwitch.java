package logic.map.operation;

import java.io.Serializable;

import logic.Rectangle;

public class OperationSwitch implements Operation, Serializable{

	private Rectangle rectangleA;
	
	private Rectangle rectangleB;

	public OperationSwitch(Rectangle rectangleA, Rectangle rectangleB) {
		super();
		this.rectangleA = rectangleA;
		this.rectangleB = rectangleB;
	}

	@Override
	public OperationType getOperationType() {
		return OperationType.SWITCH;
	}

	public Rectangle getRectangleA() {
		return rectangleA;
	}

	public Rectangle getRectangleB() {
		return rectangleB;
	}
	
	public boolean equals(Object o){
		if (o instanceof OperationSwitch)
			return (rectangleA.equals(((OperationSwitch)o).rectangleA)
					&& rectangleB.equals(((OperationSwitch)o).rectangleB))
					|| (rectangleA.equals(((OperationSwitch)o).rectangleB)
					&& rectangleB.equals(((OperationSwitch)o).rectangleA));
		return false;
	}

	
}
