package logic;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Rectangle implements Serializable, Comparable<Rectangle>{
	
	public static double collisionTolerance = 0.001;
	
	public static int DIRECTION_UP = 0;
	
	public static int DIRECTION_LEFT = 1;
	
	private static int idCounter = 0;
	
	private double x;
	private double y;
	private double width;
	private double height;
	public final int id;
	
	private Color color;
	
	private boolean rotated;

	/**
	 * Creates a new Rectangle.
	 * @param x Position
	 * @param y Position
	 * @param width
	 * @param height
	 */
	public Rectangle(double x, double y, double width, double height) {
		this(x, y, width, height, idCounter);
		idCounter++;
	}
	

	
	private Rectangle(double x, double y, double width, double height, int id){
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rotated = false;
		color = new Color((int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random()));
	}
	
	/**
	 * Rotates the Rectangle by switching the width and height parameters.
	 */
	public void rotate(){
		double widthTemp = width;
		width = height;
		height = widthTemp;
		rotated = !rotated;
	}

	/**
	 * Switch the position of the Rectangle with a given one. Only the x and y coordinates
	 * are getting manipulated.
	 * @param r
	 */
	public void switchPosition(Rectangle r){
		double xTemp = x;
		double yTemp = y;
		x = r.getX();
		y = r.getY();
		r.setX(xTemp);
		r.setY(yTemp);
	}
	
	public CollisionType getCollisionType(Rectangle r){
		if (intersect(r))
			return CollisionType.INTERSECTS;
		else{
			if (equal(x, r.getX()+r.getWidth()) && equal(y, r.getY()+ r.getHeight()))
				return CollisionType.TOUCHES_TOP_LEFT;
			else if (equal(x, r.getX()+r.getWidth())
					&& r.getY()-height < y
					&& y < r.getY()+height+r.getHeight())
				return CollisionType.TOUCHES_LEFT;
			else if (equal(x+width, r.getX())
					&& r.getY()-height < y
					&& y < r.getY()+height+r.getHeight())
				return CollisionType.TOUCHES_RIGHT;
			else if (equal(y, r.getY()+ r.getHeight())
					&& r.getX()-width < x
					&& x < r.getX()+width+r.getWidth())
				return CollisionType.TOUCHES_TOP;
			else if (equal(y+height, r.getY())
					&& r.getX()-width < x
					&& x < r.getX()+width+r.getWidth())
				return CollisionType.TOUCHES_BOTTOM;
		}
		return CollisionType.NO_COLLISION;
	}
	
	public CollisionType getCollisionTypeWithBorder(){
		if (equal(x, 0.0) && equal(y, 0.0))
			return CollisionType.TOUCHES_TOP_LEFT;
		else if (equal(x, 0.0))
			return CollisionType.TOUCHES_LEFT;
		else if (equal(y,0.0))
			return CollisionType.TOUCHES_TOP;
		return CollisionType.NO_COLLISION;
	}
	
	/**
	 * Checks weather p is inside the Rectangle. p is not inside if it touches the side.
	 * @param p
	 * @return
	 */
	public boolean intersect(Point2D.Double p){
		return p.x > x+collisionTolerance && p.x < x + width - collisionTolerance
				&& p.y > y+collisionTolerance && p.y < y + height - collisionTolerance;
	}
	
	/**
	 * Checks weather the rectangle intersects with the given Rectangle r.
	 * @param r
	 * @return true if the rectangles intersect.
	 */
	public boolean intersect(Rectangle r){
		boolean xIntersect = false;
		boolean yIntersect = false;
		if (equal(x, r.getX()) && equal(y, r.getY())) // rectangles are on top of themself
			return true;
		if (r.getX()-Rectangle.collisionTolerance>x-r.getWidth() && r.getX()<x+width-Rectangle.collisionTolerance)
			xIntersect = true;
		if (r.getY()-Rectangle.collisionTolerance>y-r.getHeight() && r.getY()<y+height-Rectangle.collisionTolerance)
			yIntersect = true;
		
		/*
		if (r.isWithinRangeX(x) || isWithinRangeX(r.getX()))
			xIntersect = true;
		if (r.isWithinRangeY(y) || isWithinRangeY(r.getY()))
			yIntersect = true;
		 */
		return xIntersect && yIntersect;
	}
	
	public boolean isWithinRangeX(double xRange){
		return x+collisionTolerance < xRange
				&& xRange < x+width-collisionTolerance;
	}
	
	public boolean isWithinRangeY(double yRange){
		return y+collisionTolerance < yRange
				&& yRange < y+height-collisionTolerance;
	}
	
	public List<Point2D.Double> getEdges(){
		List<Point2D.Double> edges = new LinkedList<Point2D.Double>();
		edges.add(new Point2D.Double(x, y)); edges.add(new Point2D.Double(x, y+height));
		edges.add(new Point2D.Double(x+width, y+height)); edges.add(new Point2D.Double(y+height, x));
		return edges;
	}
	
	public static boolean equal(double d1, double d2){
		return Math.abs(d1-d2) < collisionTolerance;
	}
	
	public double getOverlappingRate(Rectangle r){
		Rectangle r1;
		Rectangle r2;
		r1 = getX() < r.getX() ? this : r;
		r2 = r == r1 ? this : r;
		double kx = Math.max(0.0, Math.min(r1.getX()+r1.getWidth(), r2.getX()+r2.getWidth())-r2.getX());
		r1 = getY() < r.getY() ? this : r;
		r2 = r == r1 ? this : r;
		double ky = Math.max(0.0, Math.min(r1.getY()+r1.getHeight(), r2.getY()+r2.getHeight())-r2.getY());
		r1 = r.compareTo(this) < 0 ? this : r;
		return (kx*ky)/r1.getArea();
	}
	
	public Rectangle getCopy(){
		Rectangle r = new Rectangle(x, y, width, height, id);
		r.color = color;
		r.rotated = rotated;
		return r;
	}
	
	public double getArea(){
		return width * height;
	}
	

	public double getX() {
		return x;
	}


	public double getY() {
		return y;
	}


	public double getWidth() {
		return width;
	}


	public double getHeight() {
		return height;
	}


	public void setX(double x) {
		this.x = x;
	}


	public void setY(double y) {
		this.y = y;
	}


	public void setWidth(double width) {
		this.width = width;
	}


	public void setHeight(double height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isRotated() {
		return rotated;
	}
	
	public boolean equals(Object o){
		if (o instanceof Rectangle)
			return id == ((Rectangle)o).id;
		return false;
	}
	
	@Override
	public int compareTo(Rectangle r) {
		if (r.getWidth()*r.getHeight() > width*height)
			return 1;
		else if (r.getWidth()*r.getHeight() < width*height)
			return -1;
		else return 0;

	}
	
	

}
