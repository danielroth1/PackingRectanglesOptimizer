package logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import logic.Rectangle;
import logic.map.RectangleMap;

import org.junit.Before;
import org.junit.Test;

public class RectangleMapTest {

	private RectangleMap rm;
	
	private List<Rectangle> rectangles;
	
	@Before
	public void setUp() throws Exception {
		
		rectangles = new LinkedList<Rectangle>();
		//rm = new RectangleMap(rectangles);
		
	}

	@Test
	public void testIsPlacable() {
		Rectangle r1 = new Rectangle(0, 0, 55, 25);
		Rectangle r2 = new Rectangle(0, 0, 55, 25);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles);
		r1.setX(15);
		assertTrue(!rm.isPlacable(r1));
		assertTrue(!rm.isPlacable(r2));
	}
	
	@Test
	public void testIsPlacable2(){
		Rectangle r1 = new Rectangle(0, 0, 55, 25);
		Rectangle r2 = new Rectangle(55, 0, 55, 25);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles);
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
	}
	
	@Test
	public void testIsPlacable3(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 100);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles);
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
	}
	
	@Test
	public void testGetIntersectionFields(){
		Rectangle r1 = new Rectangle(55, 55, 50, 50);
		Rectangle r2 = new Rectangle(55, 105, 50, 50);
		Rectangle r3 = new Rectangle(5, 55, 50, 50);
		Rectangle r4 = new Rectangle(55, 5, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rectangles.add(r4);
		
		rm = new RectangleMap(rectangles);
		assertTrue("actual size = " + rm.getIntersectionFields(r1).size(), 
				rm.getIntersectionFields(r1).size() == 36);
		assertTrue("actual size = " + rm.getIntersectionFields(r2).size(), 
				rm.getIntersectionFields(r2).size() == 42);
		assertTrue("actual size = " + rm.getIntersectionFields(r3).size(), 
				rm.getIntersectionFields(r3).size() == 42);
		assertTrue("actual size = " + rm.getIntersectionFields(r4).size(), 
				rm.getIntersectionFields(r4).size() == 42);
	}
	
	@Test
	public void testGetSurroundingRectangles(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		Rectangle r3 = new Rectangle(0, 0, 50, 50);
		Rectangle r4 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rectangles.add(r4);
		
		rm = new RectangleMap(rectangles);
		assertTrue("actual size = " + rm.getSurroundingRectangles(r1).size(), 
				rm.getSurroundingRectangles(r1).size() == 1);
		assertTrue("actual size = " + rm.getSurroundingRectangles(r2).size(), 
				rm.getSurroundingRectangles(r2).size() == 2);
		assertTrue("actual size = " + rm.getSurroundingRectangles(r3).size(), 
				rm.getSurroundingRectangles(r3).size() == 2);
		assertTrue("actual size = " + rm.getSurroundingRectangles(r4).size(), 
				rm.getSurroundingRectangles(r4).size() == 1);
		
	}
	
	@Test
	public void testGetCanonicPosition(){
		Rectangle r1 = new Rectangle(50, 50, 50, 50);
		Rectangle r2 = new Rectangle(50, 100, 50, 50);
		Rectangle r3 = new Rectangle(0, 50, 50, 50);
		Rectangle r4 = new Rectangle(50, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rectangles.add(r4);
		
		rm = new RectangleMap(rectangles);
	}
	
	@Test
	public void testRotate(){
		Rectangle r1 = new Rectangle(0, 0, 100, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles);
		
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		rm.rotateRectangle(r1);
		assertTrue(rm.isPlacable(r1));
		assertTrue(!rm.isPlacable(r2));
		
	}
	
	@Test
	public void testRotate2(){
		Rectangle r1 = new Rectangle(0, 0, 100, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles);
		
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		rm.rotateRectangle(r2);
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		
	}
	
	@Test
	public void testMove(){
		Rectangle r1 = new Rectangle(0, 0, 100, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles);
		
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		rm.moveRectangle(r1, new Point2D.Double(50, 0));
		rm.moveRectangle(r2, new Point2D.Double(0, 0));
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		
	}
	
	@Test
	public void testMove2(){
		Rectangle r1 = new Rectangle(0, 0, 100, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles);
		
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		rm.moveRectangle(r1, new Point2D.Double(150, 0));
		assertTrue(rm.isPlacable(r1));
		assertTrue(!rm.isPlacable(r2));
		
	}
	
	@Test
	public void testSwitch(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		Rectangle r2 = new Rectangle(0, 0, 100, 50);
		Rectangle r3 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles);
		
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		assertTrue(rm.isPlacable(r3));
		rm.switchRectangles(r1, r3);
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		assertTrue(rm.isPlacable(r3));
		
	}
	
	@Test
	public void testComplex(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		Rectangle r2 = new Rectangle(0, 0, 100, 50);
		Rectangle r3 = new Rectangle(0, 0, 50, 50);
		Rectangle r4 = new Rectangle(0, 0, 100, 100);
		Rectangle r5 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rectangles.add(r4);
		rectangles.add(r5);
		rm = new RectangleMap(rectangles);
		
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		assertTrue(rm.isPlacable(r3));
		assertTrue(rm.isPlacable(r4));
		assertTrue(rm.isPlacable(r5));
		rm.moveRectangle(r3, new Point2D.Double(0, 100));
		rm.moveRectangle(r4, new Point2D.Double(0, 150));
		rm.moveRectangle(r5, new Point2D.Double(50, 50));
		//rm.switchRectangles(r1, r3);
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		assertTrue(rm.isPlacable(r3));
		assertTrue(rm.isPlacable(r4));
		assertTrue(rm.isPlacable(r5));
	}
	
	@Test
	public void testGetCanonicPositionsCorner(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		rectangles.add(r1);
		rm = new RectangleMap(rectangles);
		assertTrue(rm.isPlacable(r1));
		List<Point2D.Double> positions = rm.getCanonicPositionsLeftCorner(r1);
		for (Point2D.Double p : positions){
			assertTrue(pointsEqual(p, new Point2D.Double(50, 0)) 
					|| pointsEqual(p, new Point2D.Double(0, 100)));
		}
		
	}
	
	@Test
	public void testGetCanoncPositions(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		Rectangle r3 = new Rectangle(0, 0, 150, 50);
		Rectangle r4 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rectangles.add(r4);
		rm = new RectangleMap(rectangles);		
		rm.moveRectangle(r4, new Point2D.Double(0, 100));
		assertTrue(rm.isPlacable(r1));
		assertTrue(rm.isPlacable(r2));
		assertTrue(rm.isPlacable(r3));
		assertTrue(rm.isPlacable(r4));
		
		assertTrue("result was " + rm.getCanonicPositionBorder(r2), pointsEqual(rm.getCanonicPositionBorder(r2), new Point2D.Double(100, 0)));
		assertTrue(pointsEqual(rm.getCanonicPositionBorder(r3), new Point2D.Double(250, 0)));
		assertTrue(pointsEqual(rm.getCanonicPositionBorder(r4), new Point2D.Double(0, 150)));
	}
	
	@Test
	public void testSurroundingRectangles(){
		List<Rectangle> rectangles = new LinkedList<Rectangle>();
		Rectangle r1 = new Rectangle(55, 55, 50, 50);
		Rectangle r2 = new Rectangle(55, 105, 50, 50);
		Rectangle r3 = new Rectangle(5, 55, 50, 50);
		Rectangle r4 = new Rectangle(55, 5, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rectangles.add(r4);
		
	}
	
	@Test
	public void testIntersect(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		Rectangle r2 = new Rectangle(0, 0, 50, 100);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles, 100.0, 100.0);		
		rm.moveRectangle(r2, new Point2D.Double(0, 0));
//		assertTrue(!rm.isPlacable(r1));
//		assertTrue(!rm.isPlacable(r2));
		
		assertTrue(r1.intersect(r2));
		assertTrue(r2.intersect(r1));
	}
	
	private boolean pointsEqual(Point2D.Double p1, Point2D.Double p2){
		return Math.abs(p1.x-p2.x) < 0.0001 && Math.abs(p1.y-p2.y) < 0.0001;
	}
	
	@Test
	public void testIntersect2(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		Rectangle r2 = new Rectangle(0, 0, 60, 100);
		Rectangle r3 = new Rectangle(0, 0, 60, 100);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 100.0, 100.0);		
		rm.moveRectangle(r3, new Point2D.Double(0, 100));
//		assertTrue(!rm.isPlacable(r1));
//		assertTrue(!rm.isPlacable(r2));
		
		assertTrue(!r2.intersect(r3));
		assertTrue(!r3.intersect(r2));
	}
	
	@Test
	public void testIntersect3(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		Rectangle r2 = new Rectangle(0, 0, 50, 100);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles, 100.0, 100.0);		
		rm.moveRectangle(r2, new Point2D.Double(0, 10));
//		assertTrue(!rm.isPlacable(r1));
//		assertTrue(!rm.isPlacable(r2));
		
		assertTrue(r1.intersect(r2));
		assertTrue(r2.intersect(r1));
	}
	
	@Test
	public void testIntersect4(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		Rectangle r2 = new Rectangle(0, 0, 50, 100);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles, 100.0, 100.0);		
		rm.moveRectangle(r2, new Point2D.Double(10, 0));
//		assertTrue(!rm.isPlacable(r1));
//		assertTrue(!rm.isPlacable(r2));
		
		assertTrue(r1.intersect(r2));
		assertTrue(r2.intersect(r1));
	}
	
	@Test
	public void testgetCanonicPoints(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles, 40.0, 40.0);		
		rm.moveRectangle(r1, new Point2D.Double(0, 50));
		assertTrue("result was " + rm.getCanonicPosition(r1, r2), pointsEqual(rm.getCanonicPosition(r1, r2), new Point2D.Double(50, 50)));
		
		
	}
	
	@Test
	public void testIsPlacable4(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(0, 0, 100, 100);
		Rectangle r3 = new Rectangle(0, 0, 100, 20);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 40.0, 40.0);		
		rm.moveRectangle(r3, new Point2D.Double(0, 50));
		
		assertTrue(r2.intersect(r3));
		assertTrue(r3.intersect(r2));
		assertTrue(!rm.isStateAfterOperationLegal());
		
	}
	
	@Test
	public void testDifFieldSizes(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(0, 0, 100, 100);
		Rectangle r3 = new Rectangle(0, 0, 100, 20);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 40.0, 40.0);		
		rm.moveRectangle(r3, new Point2D.Double(0, 50));
		
		assertTrue(r2.intersect(r3));
		assertTrue(r3.intersect(r2));
		assertTrue(!rm.isStateAfterOperationLegal());
		
	}
	
	@Test
	public void testGetCost(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		Rectangle r2 = new Rectangle(0, 0, 50, 100);
		Rectangle r3 = new Rectangle(0, 0, 50, 100);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 100.0, 100.0);		
		rm.moveRectangle(r3, new Point2D.Double(0, 100));
//		assertTrue(!rm.isPlacable(r1));
//		assertTrue(!rm.isPlacable(r2));
		assertEquals(200.0, rm.getCost(), 0.001);
	}
	
	@Test
	public void testGetCost2(){
		Rectangle r1 = new Rectangle(0, 0, 50, 100);
		Rectangle r2 = new Rectangle(0, 0, 50, 100);
		Rectangle r3 = new Rectangle(0, 0, 50, 100);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 100.0, 100.0);
//		assertTrue(!rm.isPlacable(r1));
//		assertTrue(!rm.isPlacable(r2));
		assertEquals(150.0, rm.getCost(), 0.001);
	}
	
	@Test
	public void testGetCanonicPositionBorder(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rm = new RectangleMap(rectangles, 100.0, 100.0);
		rm.moveRectangle(r1, new Point2D.Double(100, 100));
//		assertTrue(!rm.isPlacable(r1));
//		assertTrue(!rm.isPlacable(r2));
		//assertTrue("result was " + rm.getCanonicPositionBorder(r1), pointsEqual(new Point2D.Double(0, 100), rm.getCanonicPositionBorder(r1)));
	}
	
	@Test
	public void testGetCanonicPosition2(){
		Rectangle r1 = new Rectangle(0, 0, 100, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		Rectangle r3 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rm = new RectangleMap(rectangles, 100.0, 100.0);
		rm.moveRectangle(r3, new Point2D.Double(0, 50));
//		assertTrue(!rm.isPlacable(r1));
//		assertTrue(!rm.isPlacable(r2));
		assertTrue("result was " + rm.getCanonicPosition(r1, r3), pointsEqual(new Point2D.Double(50, 50), rm.getCanonicPosition(r1, r3)));
	}

	@Test
	public void testIsPlacable5(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		Rectangle r3 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 0, 100.0);
		rm.moveRectangle(r2, new Point2D.Double(100, 50));
		rm.moveRectangle(r3, new Point2D.Double(0, 100));
		assertTrue(rm.isPlacable(r1));
		assertTrue(!rm.isPlacable(r2));
		assertTrue(!rm.isPlacable(r3));
	}
	
	@Test
	public void testIsPlacable6(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		Rectangle r3 = new Rectangle(0, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 0, 100.0);
		rm.moveRectangle(r2, new Point2D.Double(100, 0));
		rm.moveRectangle(r3, new Point2D.Double(100, 50));
		assertTrue(rm.isPlacable(r1));
		assertTrue(!rm.isPlacable(r2));
		assertTrue(!rm.isPlacable(r3));
	}
	
	@Test
	public void testOverlapping1(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(25, 0, 50, 50);
		Rectangle r3 = new Rectangle(50, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 0, 110.0, false, true);
		
		assertEquals(0.5, rm.getOverlappingRate(r1), 0.001);
		assertEquals(0.5, rm.getOverlappingRate(r2), 0.001);
		assertEquals(0.5, rm.getOverlappingRate(r3), 0.001);
		
	}
	
	@Test
	public void testOverlapping2(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(0, 25, 50, 50);
		Rectangle r3 = new Rectangle(0, 50, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 0, 110.0, false, true);
		
		assertEquals(0.5, rm.getOverlappingRate(r1), 0.001);
		assertEquals(0.5, rm.getOverlappingRate(r2), 0.001);
		assertEquals(0.5, rm.getOverlappingRate(r3), 0.001);
		
	}
	
	@Test
	public void testOverlapping3(){
		Rectangle r1 = new Rectangle(0, 0, 100, 100);
		Rectangle r2 = new Rectangle(0, 75, 50, 50);
		Rectangle r3 = new Rectangle(75, 0, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 0, 110.0, false, true);
		
		assertEquals(0.5, rm.getOverlappingRate(r1), 0.001);
		assertEquals(0.5, rm.getOverlappingRate(r2), 0.001);
		assertEquals(0.5, rm.getOverlappingRate(r3), 0.001);
		
	}
	
	@Test
	public void testOverlappingPosition1(){
		Rectangle r1 = new Rectangle(0, 0, 100, 100);
		Rectangle r2 = new Rectangle(100, 0, 50, 50);
		Rectangle r3 = new Rectangle(0, 100, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 0, 110.0, false, true);
		Point2D.Double p = rm.getOverlappingPosition(r2, r1, new Point2D.Double(100, 0), 0.5, Rectangle.DIRECTION_LEFT);
		assertTrue("result was " + p, pointsEqual(new Point2D.Double(75, 0), p));
		p = rm.getOverlappingPosition(r3, r1, new Point2D.Double(0, 100), 0.5, Rectangle.DIRECTION_UP);
		assertTrue("result was " + p, pointsEqual(new Point2D.Double(0, 75), p));
		
	}
	
	@Test
	public void testOverlappingPosition2(){
		Rectangle r1 = new Rectangle(0, 0, 100, 100);
		Rectangle r2 = new Rectangle(50, 100, 100, 200);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles, 0, 110.0, false, true);
		Point2D.Double p = rm.getOverlappingPosition(r2, r1, new Point2D.Double(50, 100), 0.5, Rectangle.DIRECTION_UP);
		assertTrue("result was " + p, pointsEqual(new Point2D.Double(50, 0), p));
		
	}
	
	@Test
	public void testOverlappingPosition3(){
		Rectangle r1 = new Rectangle(100, 20, 100, 100);
		Rectangle r2 = new Rectangle(0, 140, 100, 200);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles, 0, 110.0, false, true);
		Point2D.Double p = rm.getOverlappingPosition(r2, r1, new Point2D.Double(0, 120), 0.5, Rectangle.DIRECTION_UP);
		assertTrue("result was " + p, pointsEqual(new Point2D.Double(0, 20), p));
	}
	
	@Test
	public void testOverlappingPosition4(){
		Rectangle r1 = new Rectangle(50, 20, 100, 100);
		Rectangle r2 = new Rectangle(0, 140, 100, 200);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles, 0, 110.0, false, true);
		Point2D.Double p = rm.getOverlappingPosition(r2, r1, new Point2D.Double(0, 140), 0.5, Rectangle.DIRECTION_UP);
		assertTrue("result was " + p, pointsEqual(new Point2D.Double(0, 20), p));
	}
	
	@Test
	public void testOverlappingPosition5(){
		Rectangle r1 = new Rectangle(60, 20, 100, 100);
		Rectangle r2 = new Rectangle(0, 140, 100, 200);
		rectangles.add(r1);
		rectangles.add(r2);
		rm = new RectangleMap(rectangles, 0, 110.0, false, true);
		Point2D.Double p = rm.getOverlappingPosition(r2, r1, new Point2D.Double(0, 140), 0.5, Rectangle.DIRECTION_UP);
		assertTrue("result was " + p, pointsEqual(new Point2D.Double(0, 0), p));
	}
	
	@Test
	public void testOverlappingOverlappingRate(){
		Rectangle r1 = new Rectangle(0, 0, 100, 100);
		Rectangle r2 = new Rectangle(0, 25, 100, 100);
		Rectangle r3 = new Rectangle(0, 100, 100, 100);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rm = new RectangleMap(rectangles, 0, 110.0, false, true);
		
		assertEquals(0.75, rm.getOverlappingRate(r1), 0.001);
		assertEquals(0.75, rm.getOverlappingRate(r2), 0.001);
		assertEquals(0.25, rm.getOverlappingRate(r3), 0.001);
		
	}

}
