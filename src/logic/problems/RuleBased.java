package logic.problems;

import gui.ExtraData;

import java.awt.geom.Point2D;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import logic.FeasableSolution;
import logic.Rectangle;
import logic.map.RectangleMap;
import logic.map.operation.Operation;
import logic.map.operation.OperationSwap;

public class RuleBased implements FeasableSolution, ExtraData {
	
	private List<Rectangle> rectangles;
	
	private RectangleMap rm;
	
	private double fieldSize;
	
	private double cost = Integer.MAX_VALUE;
	
	private double costOld;
	
	private double costBefore;
	
	private LinkedList<Operation> taboos;
	
	private Operation lastOperation = null;
	
	int numberOfRectangles;
	int index;
	int minIndex;
	int oldIndex;
	int oldMinIndex;
	
	public RuleBased(List<Rectangle> rectangles, double fieldSize){
		this(rectangles, fieldSize, true);
	}
	
	public RuleBased(List<Rectangle> rectangles, double fieldSize, boolean sort){
		this.rectangles = rectangles;
		this.fieldSize = fieldSize;
		lastOperation = null;
		taboos = new LinkedList<Operation>();
		if (sort)
			Collections.sort(rectangles);
		rm = new RectangleMap(rectangles, fieldSize, fieldSize, false, false);
		numberOfRectangles = rectangles.size();
		reset();
		
		performRule();
		cost = rm.getCost();
		costBefore = cost;
		costOld = cost;
	}
	
	@Override
	public double getCost() {
		return cost;
	}

	@Override
	public boolean updateToNextFeasableSolution() {
		if (!setNextPermutation()){
			//performRule();
			return false;
		}
		Collections.swap(rectangles, minIndex, index);
		lastOperation = new OperationSwap(minIndex, index);
		costOld = cost;
		performRule();
		cost = rm.getCost();
		return true;
	}

	private void performRule() {
		rm = new RectangleMap(rectangles, fieldSize, fieldSize, false, false);
		
		for (Rectangle r : rectangles){
			if (r.isRotated())
				r.rotate();
			boolean positionFound = false;
			double cost = rm.getCost();
			if (r==rectangles.get(0)){
				r.setX(0); r.setY(0);
				rm.add(r);
				positionFound = true;
				continue;
			}
			List<SimpleEntry<Point2D.Double, Double>> possibleLocations = new LinkedList<SimpleEntry<Point2D.Double, Double>>();
			rectangleInserted:
			for (Rectangle r2 : rectangles){
				//already inserted rectangles
				if (r == r2)
					break;
				for (Point2D.Double p_move1 : rm.getCanonicPositionsLeftCorner(r2)){
					if (p_move1 != null && !(Rectangle.equal(p_move1.x, r2.getX()) && Rectangle.equal(p_move1.y, r2.getY()))){ // there is a canonic position.
						r.setX(p_move1.x); r.setY(p_move1.y);
						rm.add(r);
						if (rm.isPlacable(r)){
							if (rm.getPossibleCosts(r)<=cost){
								positionFound = true;
								break rectangleInserted;
							}
							possibleLocations.add(new SimpleEntry<Point2D.Double, Double>(p_move1, rm.getPossibleCosts(r)));
						}
						rm.remove(r);
					}
				}
				for (Rectangle r3 : rectangles){
					if (r == r3 || r2 == r3)
						break;
					Point2D.Double p = rm.getCanonicPosition(r2, r3);
					if (p == null)
						continue; // there is no canonic position.
					
					r.setX(p.x); r.setY(p.y);
					rm.add(r);
					if (rm.isPlacable(r)){
						if (rm.getPossibleCosts(r)<=cost){
							positionFound = true;
							break rectangleInserted;
						}
						possibleLocations.add(new SimpleEntry<Point2D.Double, Double>(p, rm.getPossibleCosts(r)));
					}
					rm.remove(r);
				}
			}
			//rotation
			if (positionFound){
				double costBefore = rm.getPossibleCosts(r);
				rm.rotateRectangle(r);
				
				if (!rm.isStateAfterOperationLegal() || costBefore < rm.getPossibleCosts(r))
					rm.undo();
				
			}
			else{
				SimpleEntry<Point2D.Double, Double> minEntry = possibleLocations.get(0);
				for (SimpleEntry<Point2D.Double, Double> entry : possibleLocations){
					if (entry.getValue() < minEntry.getValue()){
						minEntry = entry;
					}
				}
				r.setX(minEntry.getKey().x);
				r.setY(minEntry.getKey().y);
				rm.add(r);
				
				rm.rotateRectangle(r);
				if (!rm.isStateAfterOperationLegal() || minEntry.getValue() < rm.getPossibleCosts(r)) // if there is no cost reduction after rotation
					rm.undo();
				
			}
		}
		
	}

	@Override
	public void undo() {
		//if (oldIndex < rectangles.size() && oldMinIndex < rectangles.size())
		Collections.swap(rectangles, minIndex, index);
		cost = costOld;
		//performRule();
		//cost = rm.getCost();//costOld;
	}

	@Override
	public void reset() {
		performRule();
		//cost = rm.getCost();//costOld;
		minIndex = numberOfRectangles-1;
		index = numberOfRectangles;
	}
	
	private boolean setNextPermutation(){
		while (true){
			oldIndex = index;
			oldMinIndex = minIndex;
			index--;
			if (index == minIndex){
				minIndex--;
				index = numberOfRectangles-1;
				if (minIndex < 0){
					return false;
				}
			}
			if (!taboos.contains(new OperationSwap(index, minIndex)))
				break;
		}
		return true;
	}

	public RectangleMap getRm() {
		return rm;
	}

	public List<Rectangle> getRectangles() {
		return rectangles;
	}
	
	public double getCostBefore(){
		return costBefore;
	}

	@Override
	public FeasableSolution getCopy() {
		List<Rectangle> copy = new LinkedList<Rectangle>();
		for (Rectangle r : rectangles){
			copy.add(r.getCopy());
		}
		RuleBased rb = new RuleBased(copy, fieldSize, false);
		rb.taboos = taboos;
		rb.lastOperation = lastOperation;
		
		rb.cost = cost;
		rb.costBefore = costBefore;
		return rb;
	}

	@Override
	public LinkedList<Operation> getTaboos() {
		return taboos;
	}

	@Override
	public Operation getLastOperation() {
		return lastOperation;
	}
	

}
