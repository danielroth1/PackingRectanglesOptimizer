package logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import logic.FeasableSolution;
import logic.Logic;
import logic.ProblemType;
import logic.Rectangle;
import logic.problems.RuleBased;
import main.LogicControl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RuleBasedTest {

	private List<List<Rectangle>> rectanglesTestSuit;
	//private List<List<Rectangle>> rectanglesTestSuit2;
		
	private int numberOfTests = 10;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		rectanglesTestSuit = new LinkedList<List<Rectangle>>();
		for (int i = 0; i < numberOfTests; ++i){
			rectanglesTestSuit.add(LogicControl.getRandomRectangles(20, 10, 200));
		}
		
		/*
		rectanglesTestSuit2 = new LinkedList<List<Rectangle>>();
		
		File f = new File("C:\\Users\\Daniel\\Documents\\Optimierungsalgorithmen\\Test4");
		FileInputStream fin = new FileInputStream(f);
		ObjectInputStream oin = new ObjectInputStream(fin);
		List<Rectangle> list = (List<Rectangle>) oin.readObject();
		fin.close();
		oin.close();
		rectanglesTestSuit2.add(list);
		
		f = new File("C:\\Users\\Daniel\\Documents\\Optimierungsalgorithmen\\Test5");
		fin = new FileInputStream(f);
		oin = new ObjectInputStream(fin);
		list = (List<Rectangle>) oin.readObject();
		fin.close();
		oin.close();
		
		rectanglesTestSuit2.add(list);
		*/
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSwap(){
		for (List<Rectangle> rectangles : rectanglesTestSuit){
			for (int i = 0; i < rectangles.size(); ++i){
				for (int j = 0; j < rectangles.size(); ++j){
					if (j == i)
						break;
					List<Rectangle> rectanglesClone = new LinkedList<Rectangle>(rectangles);
					Collections.swap(rectanglesClone, i, j);
					Collections.swap(rectanglesClone, i, j);
					for (int k = 0; k < rectangles.size(); ++k){
						assertTrue(rectangles.get(k) == rectanglesClone.get(k));
					}
				}
			}
		}
	}

	@Test
	public void test() {
		for (List<Rectangle> rectangles : rectanglesTestSuit){
			Logic logic = new Logic(ProblemType.RULE_BASED, rectangles, 100, false);
			//RuleBased rb = new RuleBased(rectangles, 100);
			logic.localSearch();
			FeasableSolution fs = logic.getFs();
			assertEquals(fs.getCost(), ((RuleBased) fs).getRm().getCost(), 0.001);
			//for (int i = 0; i < rectangles.size(); ++i){
			//	assertTrue(rectangles.get(i) == rb.getRm().getRectangles().get(i));
			//}
		}
	}
	
	@Test
	public void test2(){
		for (List<Rectangle> rectangles : rectanglesTestSuit){
			RuleBased fs = new RuleBased(rectangles, 100);
			double cost = fs.getCost();
			List<Rectangle> rectanglesBefore = new LinkedList<Rectangle>(fs.getRectangles());
			fs.updateToNextFeasableSolution();
			fs.undo();
			assertTrue(listsAreEqual(rectanglesBefore, fs.getRectangles()));
			/*
			while (fs.updateToNextFeasableSolution()){
				//if (stepByStep)
				//	return;
				if (fs.getCost() < cost){
					cost = fs.getCost();
					fs.reset();
				}
				else if (Rectangle.equal(fs.getCost(), cost)){
					rectanglesBefore = new LinkedList<Rectangle>(fs.getRm().getRectangles());
					fs.undo();
					listsAreEqual(rectanglesBefore, fs.getRm().getRectangles());
					//return;
				}
				else{
					
					fs.undo();
					assertTrue(listsAreEqual(rectanglesBefore, fs.getRm().getRectangles()));
				}
			}
			*/
		}
	}
	
	@Test
	public void test3(){
		
		
		for (List<Rectangle> rectangles : rectanglesTestSuit){
			RuleBased fs = new RuleBased(rectangles, 100);
			double cost = fs.getCost();
			List<Rectangle> rectanglesBefore = new LinkedList<Rectangle>(fs.getRm().getRectangles());
			
			while (fs.updateToNextFeasableSolution()){
				//if (stepByStep)
				//	return;
				if (fs.getCost() < cost-Rectangle.collisionTolerance){
					rectanglesBefore = new LinkedList<Rectangle>(fs.getRectangles());
					RuleBased rb2 = new RuleBased(rectanglesBefore, 100, false);
					cost = fs.getCost();
					assertEquals(rb2.getCost(), cost, 0.001);
					fs.reset();
				}
				else if (Rectangle.equal(fs.getCost(), cost)){
					fs.undo();
					assertTrue(listsAreEqual(rectanglesBefore, fs.getRm().getRectangles()));
					assertTrue(listsAreEqual(rectanglesBefore, fs.getRectangles()));
					//assertEquals(fs.getCost(), ((RuleBased) fs).getRm().getCost(), 0.001);
					//return;
				}
				else{
					
					fs.undo();
					assertTrue(listsAreEqual(rectanglesBefore, fs.getRm().getRectangles()));
					assertTrue(listsAreEqual(rectanglesBefore, fs.getRectangles()));
					//assertEquals(fs.getCost(), ((RuleBased) fs).getRm().getCost(), 0.001);
				}
			}
			fs.reset();
			assertTrue(listsAreEqual(rectanglesBefore, fs.getRm().getRectangles()));
			assertTrue(listsAreEqual(rectanglesBefore, fs.getRectangles()));
			assertEquals(fs.getCost(), ((RuleBased) fs).getRm().getCost(), 0.001);
			
		}
	}
	
	
	
	private boolean listsAreEqual(List<Rectangle> r1, List<Rectangle> r2){
		for (int i = 0; i < r1.size(); ++i){
			if (r1.get(i) != r2.get(i))
				return false;
		}
		return true;
	}

}
