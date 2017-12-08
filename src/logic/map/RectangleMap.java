package logic.map;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import logic.CollisionType;
import logic.Rectangle;
import logic.map.operation.Operation;
import logic.map.operation.OperationMove;
import logic.map.operation.OperationRotate;
import logic.map.operation.OperationSwitch;
import logic.map.operation.OperationType;

/**
 * This provides a data structure to allow an efficient access to neighbored
 * rectangles.
 * @author Daniel Roth
 */
public class RectangleMap implements Serializable {
	
	private List<Rectangle> rectangles;
	
	private RectangleField[][] map;
	
	private double width;
	
	private double height;
	
	private double dw;
	private double dh;
	
	private double cost;
	
	private double costOld;
	
	private int numberOfFieldsX;
	
	private int numberOfFieldsY;
	
	private Operation lastOperation;
	
	private Set<Rectangle> surroundingRectanglesBeforeOperation;
	
	
	public RectangleMap(List<Rectangle> rectangles) {
		this.rectangles = rectangles;
		dw = 10.0;
		dh = 10.0;
		initiate(true, false);
	}
	
	public RectangleMap(List<Rectangle> rectangles, boolean randomFeasableSolution) {
		this.rectangles = rectangles;
		dw = 10.0;
		dh = 10.0;
		initiate(randomFeasableSolution, false);
	}
	
	public RectangleMap(List<Rectangle> rectangles, double dw, double dh){
		this.rectangles = rectangles;
		this.dw = dw;
		this.dh = dh;
		initiate(true, false);
	}
	
	public RectangleMap(List<Rectangle> rectangles, double dw, double dh, boolean randomFeasableSolution){
		this.rectangles = rectangles;
		this.dw = dw;
		this.dh = dh;
		initiate(randomFeasableSolution, false);
	}
	
	public RectangleMap(List<Rectangle> rectangles, double dw, double dh, boolean randomFeasableSolution, boolean addRectangles){
		this.rectangles = rectangles;
		this.dw = dw;
		this.dh = dh;
		initiate(randomFeasableSolution, addRectangles);
	}
	
	private void initiate(boolean randomFeasableSolution, boolean addRectangles){
		surroundingRectanglesBeforeOperation = new HashSet<Rectangle>();
		initiateBoundaries(rectangles);
		map = new RectangleField[numberOfFieldsX][numberOfFieldsY];
		for (int x = 0; x < numberOfFieldsX; ++x){
			for (int y = 0; y < numberOfFieldsY; ++y){
				map[x][y] = new RectangleField();
			}
		}
		if (randomFeasableSolution)
			updateRandomFeasableSolution(rectangles);
		else if (addRectangles)
			addAll(rectangles);
		lastOperation = null;
	}
	
	public void add(Rectangle r){
		//costOld = cost;
		for (RectangleField rf : getIntersectionFields(r)){
			rf.getRectangles().add(r);
		}
		cost = Math.max(cost, getPossibleCosts(r));
	}
	
	public void remove(Rectangle r){
		for (RectangleField rf : getIntersectionFields(r)){
			rf.getRectangles().remove(r);
		}
		//cost = costOld;
	}
	
	public void switchRectangles(Rectangle r1, Rectangle r2){
		costOld = cost;
		surroundingRectanglesBeforeOperation = getSurroundingRectangles(r1);
		surroundingRectanglesBeforeOperation.addAll(getSurroundingRectangles(r2));
		remove(r1);
		remove(r2);
		r1.switchPosition(r2);
		add(r1);
		add(r2);
		lastOperation = new OperationSwitch(r1, r2);
		cost = Math.max(cost, getPossibleCosts(r1));
		cost = Math.max(cost, getPossibleCosts(r2));
	}
	
	public void rotateRectangle(Rectangle r){
		costOld = cost;
		surroundingRectanglesBeforeOperation = getSurroundingRectangles(r);
		remove(r);
		r.rotate();
		add(r);
		lastOperation = new OperationRotate(r);
		cost = Math.max(cost, getPossibleCosts(r));
	}
	
	public void moveRectangle(Rectangle r, Point2D.Double p){
		costOld = cost;
		lastOperation = new OperationMove(r, new Point2D.Double(r.getX(), r.getY()));
		surroundingRectanglesBeforeOperation = getSurroundingRectangles(r);
		remove(r);
		r.setX(p.x);
		r.setY(p.y);
		add(r);
		cost = Math.max(cost, getPossibleCosts(r));
	}
	
	public boolean isCanonic(Rectangle r){
		boolean left = false;
		boolean top = false;
		CollisionType ct2 = r.getCollisionTypeWithBorder();
		if (ct2 == CollisionType.TOUCHES_LEFT)
			left = true;
		else if(ct2 == CollisionType.TOUCHES_TOP)
			top = true;
		else if (ct2 == CollisionType.TOUCHES_TOP_LEFT)
			return true;
		for (Rectangle r2 : getSurroundingRectangles(r)){
			CollisionType ct = r.getCollisionType(r2);
			if (ct == CollisionType.TOUCHES_LEFT)
				left = true;
			else if(ct == CollisionType.TOUCHES_TOP)
				top = true;
		}
		if (left && top)
			return true;
		return false;
	}
	
	public List<Rectangle> getTouchingRectangles(Rectangle r, CollisionType ct){
		List<Rectangle> list = new LinkedList<Rectangle>();
		for (Rectangle r2 : getSurroundingRectangles(r)){
			CollisionType ct2 = r2.getCollisionType(r2);
			if (ct2 == ct)
				list.add(r2);
		}
		return list;
	}
	
	public Set<Rectangle> getSurroundingRectangles(Rectangle r){
		Set<Rectangle> list = new HashSet<Rectangle>();
		for (RectangleField rf : getIntersectionFields(r))
			for (Rectangle r2 : rf.getRectangles()){
				list.add(r2);
			}
		list.remove(r);
		return list;
	}
	
	
	/**
	 * Returns a list of the to r neighbored rectangles that are incident to the
	 * upper or left edge of r.
	 * @param r Rectangle
	 * @return List<Rectangle>
	 */
	public List<Rectangle> getCanonicRectangles(Rectangle r){
		List<Rectangle> list = new LinkedList<Rectangle>();
		for (Rectangle r2 : getSurroundingRectangles(r)){
			CollisionType ct2 = r2.getCollisionType(r2);
			if (ct2 == CollisionType.TOUCHES_LEFT || ct2 == CollisionType.TOUCHES_TOP)
				list.add(r2);
		}
		return list;
	}
	
	public List<Rectangle> getTouchingRectangles(Rectangle r){
		List<Rectangle> list = new LinkedList<Rectangle>();
		for (Rectangle r2 : getSurroundingRectangles(r)){
			CollisionType ct = r2.getCollisionType(r2);
			if (ct == CollisionType.TOUCHES_BOTTOM || ct == CollisionType.TOUCHES_LEFT
					|| ct == CollisionType.TOUCHES_RIGHT || ct == CollisionType.TOUCHES_TOP)
				list.add(r2);
		}
		return list;
	}
	
	
	public List<Rectangle> getIntersectingRectangles(Rectangle r){
		List<Rectangle> list = new LinkedList<Rectangle>();
		Set<Rectangle> alreadySeen = new HashSet<Rectangle>();
		for (RectangleField rf : getIntersectionFields(r)){
			for (Rectangle r1 : rf.getRectangles()){
				if (r == r1)
					continue;
				if (!alreadySeen.contains(r1)){
					alreadySeen.add(r1);
					if (r.intersect(r1))
						list.add(r1);
				}
			}
		}
		return list;
	}
	
	public boolean isPlacable(Rectangle r){
		for (Rectangle r2 : getSurroundingRectangles(r)){
			if (r.intersect(r2))// || !isCanonic(r2))
				return false;
		}
		return isCanonic(r);
	}
	
	public boolean isPlacable(Rectangle rFrom, Rectangle rTo, double overlappingRate){
		//boolean intersection = false;
		if (getOverlappingRate(rFrom)-Rectangle.collisionTolerance > overlappingRate)
			return false;
		/*
		for (Rectangle r2 : getSurroundingRectangles(rFrom)){
			if (rFrom.intersect(r2)){// || !isCanonic(r2))
				if (r2 == rTo){ // check, if rFrom intersects with rTo with the given overlappingRate
					intersection = false;
					break;
				}
				intersection = true;
			}
		}
		
		if (intersection)
			return false;
			*/
		if (Rectangle.equal(overlappingRate, 0))
			return isCanonic(rFrom);
		return true;
	}
	
	/**
	 * Returns the position of 2 rectangles that can be used to place another rectangle while the placed
	 * rectangle fulfills guaranteed the condition for a canonic solution. It is not tested if the rectangle
	 * intersects with other rectangles.
	 * @param r1 Rectangle
	 * @param r2 Rectangle
	 * @return Point2D.Double the coordinates of the position
	 */
	public Point2D.Double getCanonicPosition(Rectangle r1, Rectangle r2){
		boolean checkR1 = false; boolean checkR2 = false;
		double x1 = r1.getX()+r1.getWidth(); double x2 = r2.getX()+r2.getWidth();
		double y1 = r1.getY()+r1.getHeight(); double y2 = r2.getY()+r2.getHeight();
		
		if (Rectangle.equal(x1, x2) || Rectangle.equal(y1, y2))
			return null;
		
		double x; double y;
		if (x1 < x2){
			checkR1 = true;
			x = x1;
		}
		else{
			checkR2 = true;
			x = x2;
		}
		if (y1 < y2){
			checkR1 = true;
			y = y1;
		}
		else{
			checkR2 = true;
			y = y2;
		}
		if (!checkR1 || !checkR2)
			return null;
		
		return new Point2D.Double(x, y);
	}
	
	public List<Point2D.Double> getCanonicPosition(Rectangle r1, Rectangle r2, Rectangle rFrom, double overlappingRate){
		List<Point2D.Double> list = new LinkedList<Point2D.Double>();
		boolean checkR1 = false; boolean checkR2 = false;
		double x1 = r1.getX()+r1.getWidth(); double x2 = r2.getX()+r2.getWidth();
		double y1 = r1.getY()+r1.getHeight(); double y2 = r2.getY()+r2.getHeight();
		
		if (Rectangle.equal(x1, x2) || Rectangle.equal(y1, y2))
			return new LinkedList<Point2D.Double>(); //null
		
		double x; double y;
		if (x1 < x2){
			checkR1 = true;
			x = x1;
		}
		else{
			checkR2 = true;
			x = x2;
		}
		if (y1 < y2){
			checkR1 = true;
			y = y1;
		}
		else{
			checkR2 = true;
			y = y2;
		}
		if (!checkR1 || !checkR2)
			return new LinkedList<Point2D.Double>(); //null
		list.add(getOverlappingPosition(rFrom, r1, new Point2D.Double(x, y), overlappingRate, Rectangle.DIRECTION_LEFT));
		list.add(getOverlappingPosition(rFrom, r1, new Point2D.Double(x, y), overlappingRate, Rectangle.DIRECTION_UP));
		list.add(getOverlappingPosition(rFrom, r2, new Point2D.Double(x, y), overlappingRate, Rectangle.DIRECTION_LEFT));
		list.add(getOverlappingPosition(rFrom, r2, new Point2D.Double(x, y), overlappingRate, Rectangle.DIRECTION_UP));
		return list;//new Point2D.Double(x, y);
	}
	
	/**
	 * Returns the canonic position of the rectangle and the upper or left border.
	 * @param r1 Rectangle
	 * @return Point2D.Double
	 */
	public Point2D.Double getCanonicPositionBorder(Rectangle r1){
		if (Rectangle.equal(r1.getY(), 0.0))
			return new Point2D.Double(r1.getX()+r1.getWidth(), 0.0);
		else if (Rectangle.equal(r1.getX(), 0.0))
			return new Point2D.Double(0.0, r1.getY()+r1.getHeight());
		return null;
	}
	
	/**
	 * Returns the both canoic positions when a rectangle is in the left corner.
	 * @param r1 Rectangle
	 * @return List<Point2D.Double>
	 */
	public List<Point2D.Double> getCanonicPositionsLeftCorner(Rectangle r1){
		List<Point2D.Double> list = new LinkedList<Point2D.Double>();
		//if (Rectangle.equal(r1.getX(), 0.0) && Rectangle.equal(r1.getY(), 0.0)){
			list.add(new Point2D.Double(r1.getX()+r1.getWidth(), 0.0));
			list.add(new Point2D.Double(0.0, r1.getY()+r1.getHeight()));
		//}
		return list;
	}
	
	public List<Point2D.Double> getCanonicPositionsLeftCorner(Rectangle rTo, Rectangle rFrom, double overlappingRate){
		List<Point2D.Double> list = new LinkedList<Point2D.Double>();
		//if (Rectangle.equal(r1.getX(), 0.0) && Rectangle.equal(r1.getY(), 0.0)){
			list.add(getOverlappingPosition(rFrom, rTo, new Point2D.Double(rTo.getX()+rTo.getWidth(), 0.0), overlappingRate, Rectangle.DIRECTION_LEFT));
			list.add(getOverlappingPosition(rFrom, rTo, new Point2D.Double(0.0, rTo.getY()+rTo.getHeight()), overlappingRate, Rectangle.DIRECTION_UP));
		//}
		return list;
	}
	
	public void addAll(List<Rectangle> rectangles){
		for (Rectangle r : rectangles){
			add(r);
		}
	}
	
	public void updateRandomFeasableSolution(List<Rectangle> rectangles){
		double x = 0;
		for (Rectangle r : rectangles){
			r.setX(x);
			r.setY(0);
			add(r);
			x += r.getWidth();
		}
	}
	
	/**
	 * Undos the last rotate, switch or move operation. It is recommended to call this function
	 * only if there was a rotate, switch or move operation call before.
	 */
	public void undo(){
		if (lastOperation != null){
			if (lastOperation.getOperationType() == OperationType.ROTATE){
				rotateRectangle(((OperationRotate)lastOperation).getRectangle());
			}
			else if (lastOperation.getOperationType() == OperationType.MOVE){
				OperationMove om = (OperationMove) lastOperation;
				moveRectangle(om.getRectangle(), om.getP());
			}
			else if (lastOperation.getOperationType() == OperationType.SWITCH){
				OperationSwitch os = (OperationSwitch) lastOperation;
				switchRectangles(os.getRectangleA(), os.getRectangleB());
			}
		}
		lastOperation = null;
		cost = costOld;
	}
	
	public List<RectangleField> getIntersectionFields(Rectangle r){
		List<RectangleField> list = new LinkedList<RectangleField>();
		Point startPoint = getRectangleFieldPoint(new Point2D.Double(r.getX(), r.getY()));
		Point endPoint = getRectangleFieldPoint(new Point2D.Double(r.getX()+r.getWidth(), r.getY()+r.getHeight()));
		if (endPoint.x < numberOfFieldsX) endPoint.x++; 
		if (endPoint.y < numberOfFieldsY) endPoint.y++;
		if (startPoint.x > 0) startPoint.x--;
		if (startPoint.y > 0) startPoint.y--;
		for (int x = startPoint.x; x < endPoint.x; ++x){
			for (int y = startPoint.y; y < endPoint.y; ++y){
				list.add(map[x][y]);
			}
		}
		return list;
	}
	
	private Point getRectangleFieldPoint(Point2D.Double p){
		int x = (int)Math.floor(p.x/dw);
		int y = (int)Math.floor(p.y/dh);
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (x > numberOfFieldsX-1)
			x = numberOfFieldsX-1;
		if (y > numberOfFieldsY-1)
			y = numberOfFieldsY-1;
		return new Point(x, y);
	}
	
	public boolean isOutOfBounds(Point2D.Double p){
		return p.x < 0 || p.y < 0 || p.x > width || p.y > height;
	}
	
	/**
	 * The area of the smallest rectangle that surrounds all rectangles.
	 * @return double
	 */
	public double getCost(){
		//return cost;
		double x = 0;
		double y = 0;
		//initiateBoundaries(rectangles);
		if (numberOfFieldsX == 0 || numberOfFieldsY == 0)
			return 0;
		for (int x1 = 0; x1 < numberOfFieldsX; ++x1){
			for (int y1 = 0; y1 < numberOfFieldsY; ++y1){
				x = Math.max(x, getMaxValue(map[x1][y1].getRectangles(), true));
				y = Math.max(y, getMaxValue(map[x1][y1].getRectangles(), false));
			}
			//if (x1 == numberOfFieldsX-1 || map[x1+1][0].getRectangles().size() == 0){
			//}
		}
		
			//if (y1 == numberOfFieldsY-1 || map[0][y1+1].getRectangles().size() == 0){
			
			//}
		
		return Math.max(x, y);
		/*
		double x = 0;
		double y = 0;
		//initiateBoundaries(rectangles);
		if (numberOfFieldsX == 0 || numberOfFieldsY == 0)
			return 0;
		for (int x1 = 0; x1 < numberOfFieldsX; ++x1){
			//if (x1 == numberOfFieldsX-1 || map[x1+1][0].getRectangles().size() == 0){
			x = Math.max(x, getMaxValue(map[x1][0].getRectangles(), true));
			//}
		}
		for (int y1 = 0; y1 < numberOfFieldsY; ++y1){
			//if (y1 == numberOfFieldsY-1 || map[0][y1+1].getRectangles().size() == 0){
			y = Math.max(y, getMaxValue(map[0][y1].getRectangles(), false));
			//}
		}
		return Math.max(x, y);
		*/
	}
	
	public double getPossibleCosts(Rectangle r){
		return Math.max(r.getX()+r.getWidth(), r.getY()+r.getHeight());
	}
	
	public boolean isStateAfterOperationLegal(){
		if (lastOperation instanceof OperationRotate){
			if (!isPlacable(((OperationRotate)lastOperation).getRectangle()))
				return false;
		}
		else if (lastOperation instanceof OperationSwitch){
			if (!isPlacable(((OperationSwitch)lastOperation).getRectangleA()))
				return false;
			if (!isPlacable(((OperationSwitch)lastOperation).getRectangleB()))
				return false;
		}
		else if (lastOperation instanceof OperationMove){
			if (!isPlacable(((OperationMove)lastOperation).getRectangle()))
				return false;
		}
		for (Rectangle r : surroundingRectanglesBeforeOperation){
			if (!isPlacable(r))
				return false;
		}
		
		
		return true;
	}
	
	private double getMaxValue(List<Rectangle> rectangles, boolean xAxis){
		if (rectangles.isEmpty())
			return 0;
		Rectangle currentRectangle = rectangles.get(0);
		double maxValue;
		if (xAxis)
			maxValue = currentRectangle.getX()+currentRectangle.getWidth();
		else
			maxValue = currentRectangle.getY()+currentRectangle.getHeight();
		for (Rectangle r : rectangles){
			double value;
			if (xAxis)
				value = r.getX()+r.getWidth();
			else
				value = r.getY()+r.getHeight();
			if (value > maxValue){
				maxValue = value;
			}
		}
		return maxValue;
	}
	
	private void initiateBoundaries(List<Rectangle> rectangles){
		width = 0;
		height = 0;
		for (Rectangle r : rectangles){
			width += r.getWidth();
			height += r.getHeight();
		}
		width = width - width % dw + dw;
		height = height - height % dh + dh;
		numberOfFieldsX = (int)(width/dw)+1;
		numberOfFieldsY = (int)(height/dh)+1;
	}
	
	public Rectangle getRectangleAt(Point2D.Double p){
		
		Point fieldPoint;
		try {
			fieldPoint = getRectangleFieldPoint(p);
			for (Rectangle r : map[fieldPoint.x][fieldPoint.y].getRectangles()){
				if (r.intersect(p))
					return r;
			}
		} catch (ConcurrentModificationException | NoSuchElementException | NullPointerException e) {
			
		}
		
		return null;
	}
	
	public double getOverlappingRate(Rectangle r){
		double overlappingRate = 0;
		for (Rectangle r2 : getIntersectingRectangles(r)){
			double rate = r.getOverlappingRate(r2);
			if (rate > overlappingRate)
				overlappingRate = rate;
		}
		return overlappingRate;
	}
	
	public Point2D.Double getOverlappingPosition(Rectangle rFrom, Rectangle rTo, Point2D.Double startPoint, double overlappingRate, int direction){
		double minSize = (rTo.compareTo(rFrom) < 0 ? rFrom : rTo).getArea();
		double x;
		double y;
		double X1 = Math.max(rTo.getX(), startPoint.getX());
		double Y1 = Math.max(rTo.getY(), startPoint.getY());
		double Y2 = Math.min(rTo.getY() + rTo.getHeight(), startPoint.getY() + rFrom.getHeight());
		double X2 = Math.min(rTo.getX() + rTo.getWidth(), startPoint.getX() + rFrom.getWidth());
		if (direction == Rectangle.DIRECTION_LEFT){
			if (Math.abs(Y2-Y1) < Rectangle.collisionTolerance)
				x = Math.min(rTo.getX(), startPoint.getX());//rTo.getX();
			else
				x = X2-(overlappingRate*minSize)/(Y2-Y1);
			x = Math.max(0, x);
			y = startPoint.getY();
		}
		else{
			x = startPoint.getX();
			if (Math.abs(X2-X1) < Rectangle.collisionTolerance)
				y = Math.min(rTo.getY(), startPoint.getY());//rTo.getY();
			else
				y = Y2-(overlappingRate*minSize)/(X2-X1);
			y = Math.max(0,  y);
				
		}
		return new Point2D.Double(x, y);
	}

	public int getNumberOfFieldsX() {
		return numberOfFieldsX;
	}

	public int getNumberOfFieldsY() {
		return numberOfFieldsY;
	}

	public double getDw() {
		return dw;
	}

	public double getDh() {
		return dh;
	}

	public RectangleField[][] getMap() {
		return map;
	}

	public List<Rectangle> getRectangles() {
		return rectangles;
	}
}
