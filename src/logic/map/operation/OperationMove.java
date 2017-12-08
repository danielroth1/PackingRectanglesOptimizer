package logic.map.operation;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;

import logic.Rectangle;

public class OperationMove implements Operation, Serializable{

	private Rectangle rectangle;
	
	private Point2D.Double p;
	
	
	public OperationMove(Rectangle rectangle, Double p) {
		super();
		this.rectangle = rectangle;
		this.p = p;
	}

	@Override
	public OperationType getOperationType() {
		return OperationType.MOVE;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public Point2D.Double getP() {
		return p;
	}

	public boolean equals(Object o){
		if (o instanceof OperationMove)
			return rectangle.equals(((OperationMove)o).rectangle)
					&& pointsEqual(p, ((OperationMove)o).p);
		return false;
	}
	
	public static boolean pointsEqual(Point2D.Double p1, Point2D.Double p2){
		return Math.abs(p1.x-p2.x) < Rectangle.collisionTolerance
				&& Math.abs(p1.y-p2.y) < Rectangle.collisionTolerance;
	}
}
