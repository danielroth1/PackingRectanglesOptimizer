package logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import logic.Rectangle;
import logic.map.operation.Operation;
import logic.map.operation.OperationMove;
import logic.map.operation.OperationRotate;
import logic.map.operation.OperationSwap;
import logic.map.operation.OperationSwitch;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RectangleTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testOverlapping(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(25, 0, 50, 50);
		
		assertEquals(0.5, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.5, r2.getOverlappingRate(r1), 0.001);
		
		r1 = new Rectangle(0, 0, 50, 50);
		r2 = new Rectangle(0, 25, 50, 50);
		
		assertEquals(0.5, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.5, r2.getOverlappingRate(r1), 0.001);
	}
	
	@Test
	public void testOverlapping2(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(50, 0, 50, 50);
		
		assertEquals(0, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0, r2.getOverlappingRate(r1), 0.001);
		
		r1 = new Rectangle(0, 0, 50, 50);
		r2 = new Rectangle(0, 50, 50, 50);
		
		assertEquals(0, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0, r2.getOverlappingRate(r1), 0.001);
	}

	@Test
	public void testOverlapping3(){
		Rectangle r1 = new Rectangle(0, 0, 50, 50);
		Rectangle r2 = new Rectangle(0, 0, 50, 50);
		
		assertEquals(1, r1.getOverlappingRate(r1), 0.001);
		assertEquals(1, r2.getOverlappingRate(r2), 0.001);
		assertEquals(1, r2.getOverlappingRate(r1), 0.001);
		assertEquals(1, r1.getOverlappingRate(r2), 0.001);
	}
	
	@Test
	public void testOverlapping4(){
		Rectangle r1 = new Rectangle(0, 0, 100, 100);
		Rectangle r2 = new Rectangle(75, 0, 100, 100);
		
		assertEquals(0.25, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.25, r2.getOverlappingRate(r1), 0.001);
		
		r1 = new Rectangle(0, 0, 100, 100);
		r2 = new Rectangle(75, 0, 100, 100);
		
		assertEquals(0.25, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.25, r2.getOverlappingRate(r1), 0.001);
	}
	
	@Test
	public void testOverlapping5(){
		Rectangle r1 = new Rectangle(0, 0, 100, 100);
		Rectangle r2 = new Rectangle(25, 0, 100, 100);
		
		assertEquals(0.75, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.75, r2.getOverlappingRate(r1), 0.001);
		
		r1 = new Rectangle(0, 0, 100, 100);
		r2 = new Rectangle(0, 25, 100, 100);
		
		assertEquals(0.75, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.75, r2.getOverlappingRate(r1), 0.001);
	}
	
	@Test
	public void testOverlapping6(){
		Rectangle r1 = new Rectangle(0, 0, 100, 100);
		Rectangle r2 = new Rectangle(25, 0, 100, 100);
		
		assertEquals(0.75, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.75, r2.getOverlappingRate(r1), 0.001);
		
		r1 = new Rectangle(0, 0, 100, 100);
		r2 = new Rectangle(0, 25, 100, 100);
		
		assertEquals(0.75, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.75, r2.getOverlappingRate(r1), 0.001);
	}
	
	@Test
	public void testOverlapping7(){
		Rectangle r1 = new Rectangle(0, 0, 100, 100);
		Rectangle r2 = new Rectangle(75, 0, 50, 50);
		
		assertEquals(0.5, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.5, r2.getOverlappingRate(r1), 0.001);
		
		r1 = new Rectangle(0, 0, 100, 100);
		r2 = new Rectangle(0, 75, 50, 50);
		
		assertEquals(0.5, r1.getOverlappingRate(r2), 0.001);
		assertEquals(0.5, r2.getOverlappingRate(r1), 0.001);
	}

	@Test
	public void testContains(){
		List<Operation> operations = new LinkedList<Operation>();
		Rectangle r1 = new Rectangle(0, 0, 100, 100);
		Rectangle r2 = new Rectangle(75, 0, 50, 50);
		operations.add(new OperationMove(r1, new Point2D.Double(10, 20)));
		operations.add(new OperationSwap(10, 20));
		operations.add(new OperationRotate(r1));
		operations.add(new OperationSwitch(r1, r2));
		
		assertTrue(operations.contains(new OperationMove(r1, new Point2D.Double(10, 20))));
		assertTrue(operations.contains(new OperationSwap(10, 20)));
		assertTrue(operations.contains(new OperationSwap(20, 10)));
		assertTrue(operations.contains(new OperationRotate(r1)));
		assertTrue(operations.contains(new OperationSwitch(r1, r2)));
		assertTrue(operations.contains(new OperationSwitch(r2, r1)));
	}
	
}
