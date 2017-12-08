package logic.map.operation;

import java.io.Serializable;

import logic.Rectangle;

public class OperationRotate implements Operation, Serializable{

	private Rectangle rectangle;
	
	public OperationRotate(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	@Override
	public OperationType getOperationType() {
		return OperationType.ROTATE;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public boolean equals(Object o){
		if (o instanceof OperationRotate)
			return rectangle.equals(((OperationRotate)o).rectangle);
		return false;
	}

}
