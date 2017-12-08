package logic.problems;

import gui.ExtraData;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import logic.FeasableSolution;
import logic.Rectangle;
import logic.map.RectangleMap;
import logic.map.operation.Operation;
import logic.map.operation.OperationMove;
import logic.map.operation.OperationRotate;
import logic.map.operation.OperationSwitch;

public class GeometryBased implements FeasableSolution, ExtraData, Serializable{

	//private double cost;
	
	private RectangleMap rm;
	
	private List<Rectangle> rectangles;
	
	private double cost;
	
	private double costOld;
	
	private double costBefore;
	
	private LinkedList<Operation> taboos;
	
	private Operation lastOperation = null;
	
	//state control

	public final static int STATE_START = 0;
	public final static int STATE_ROTATE = 1;
	public final static int STATE_SWITCH = 2;
	public final static int STATE_MOVE1 = 3;
	public final static int STATE_MOVE2 = 4;
	
	private int state = STATE_START;
	private Iterator<Rectangle> it1;
	private Iterator<Rectangle> it2;
	private Iterator<Rectangle> it3;
	private Iterator<Point2D.Double> it_move1;
	private Rectangle r;
	private Rectangle r2;
	private Rectangle r3;
	private Point2D.Double p_move1;
	private boolean p_move1_set = false;
	
	
	public GeometryBased(List<Rectangle> rectangles, boolean randomFeasableSolution) {
		this.rectangles = rectangles;
		taboos = new LinkedList<Operation>();
		lastOperation = null;
		it1 = rectangles.iterator();
		it2 = rectangles.iterator();
		it3 = rectangles.iterator();
		if (randomFeasableSolution){
			RuleBased rb = new RuleBased(rectangles, 10, true);
			rm = rb.getRm();
		}
		else
			rm = new RectangleMap(rectangles, randomFeasableSolution);
		cost = rm.getCost();
		costBefore = cost;
		costOld = cost;
		//calculateCost();
	}
	
	public GeometryBased(List<Rectangle> rectangles, double fieldSize, boolean randomFeasableSolution) {
		this.rectangles = rectangles;
		taboos = new LinkedList<Operation>();
		lastOperation = null;
		it1 = rectangles.iterator();
		it2 = rectangles.iterator();
		it3 = rectangles.iterator();
		if (randomFeasableSolution){
			RuleBased rb = new RuleBased(rectangles, fieldSize, true);
			rm = rb.getRm();
		}
		else
			rm = new RectangleMap(rectangles, fieldSize, fieldSize, randomFeasableSolution, true);
		cost = rm.getCost();
		costOld = cost;
		costBefore = cost;
		//calculateCost();
	}
	
	private GeometryBased(List<Rectangle> rectangles, double fieldSize){
		this.rectangles = rectangles;
		taboos = new LinkedList<Operation>();
		lastOperation = null;
		rm = new RectangleMap(rectangles, fieldSize, fieldSize, false, true);
		cost = rm.getCost();
		costOld = cost;
		reset();
	}

	@Override
	public double getCost() {
		return cost;
	}

	@Override
	public boolean updateToNextFeasableSolution() {
		//System.out.println("start search");
		while (true){
			switch(state){
			case STATE_START:
				it1 = rectangles.iterator();
				state = STATE_ROTATE;
				break;
			case STATE_ROTATE:
				if (!it1.hasNext()){
					state = STATE_START;
					return false;
				}
				r = it1.next();
				it2 = rectangles.iterator();
				state = STATE_SWITCH;
				if (rotate()){
					lastOperation = new OperationRotate(r);
					costOld = cost;
					cost = rm.getCost();
					return true;
				}
				break;
			case STATE_SWITCH:
				if (!it2.hasNext()){
					state = STATE_ROTATE;
					break;
				}
				r2 = it2.next();
				if (r == r2)
					break;
				rm.switchRectangles(r, r2);
				state = STATE_MOVE1;
				p_move1_set = false;
				if (rm.isStateAfterOperationLegal() && !taboos.contains(new OperationSwitch(r, r2))){
					lastOperation = new OperationSwitch(r, r2);
					costOld = cost;
					cost = rm.getCost();
					return true;
				}
				rm.undo();
				break;
			case STATE_MOVE1:
				if (!p_move1_set){
					it_move1 = rm.getCanonicPositionsLeftCorner(r).iterator();
					p_move1_set = true;
				}
				
				//if (Rectangle.equal(r.getX(), 0.0)
				//		&& Rectangle.equal(r.getY(), 0.0)){ // rectangle in the left upper corner
					if (!it_move1.hasNext()){
						state = STATE_MOVE2;
						it3 = rectangles.iterator();
						p_move1_set = false;
						break;
					}
					p_move1 = it_move1.next();
					if (p_move1 != null && !(Rectangle.equal(p_move1.x, r2.getX()) && Rectangle.equal(p_move1.y, r2.getY()))){ // there is a canonic position.
						rm.moveRectangle(r2, p_move1);
						if (rm.isStateAfterOperationLegal() && !taboos.contains(new OperationMove(r2, p_move1))){
							lastOperation = new OperationMove(r2, p_move1);
							costOld = cost;
							cost = rm.getCost();
							return true;
						}
						rm.undo();
					}
				/*
				}
				else{ // rectangle that is incident to the border
					Point2D.Double p = rm.getCanonicPositionBorder(r);
					if (p != null){ // there is a canonic position.
						rm.moveRectangle(r2, p);
						if (rm.isStateAfterOperationLegal()){
							state = STATE_MOVE2;
							it3 = rectangles.iterator();
							p_move1_set = false;
							return true;
						}
						rm.undo();
					}
					state = STATE_MOVE2;
					it3 = rectangles.iterator();
					p_move1_set = false;
				}
				*/
				
				break;
			case STATE_MOVE2:
				if (!it3.hasNext()){
					state = STATE_SWITCH;
					break;
				}
				r3 = it3.next();
				if (r == r3 || r2 == r3)
					break;
				Point2D.Double p = rm.getCanonicPosition(r2, r3);
				if (p == null)
					break; // there is no canonic position.
				if (!(Rectangle.equal(p.x, r.getX()) && Rectangle.equal(p.y, r.getY()))){
					rm.moveRectangle(r, p);
					if (rm.isStateAfterOperationLegal() && !taboos.contains(new OperationMove(r, p))){
						lastOperation = new OperationMove(r, p);
						costOld = cost;
						cost = rm.getCost();
						return true;
					}
				}
				rm.undo();
				break;
			}
			
		}
	}
	
	private boolean rotate(){
		//rotate
		rm.rotateRectangle(r);
		if (rm.isStateAfterOperationLegal() && !taboos.contains(new OperationRotate(r)))
			return true;
		rm.undo();
		return false;
	}

	@Override
	public void undo() {
		rm.undo();
		cost = costOld;
	}

	@Override
	public void reset() {
		state = STATE_START;
		it1 = rectangles.iterator();
		it2 = rectangles.iterator();
		it3 = rectangles.iterator();
		p_move1_set = false;
	}

	public RectangleMap getRm() {
		return rm;
	}

	public List<Rectangle> getRectangles() {
		return rectangles;
	}
	
	public double getCostBefore() {
		return costBefore;
	}

	private void setCost(double cost){
		this.cost = cost;
	}
	
	@Override
	public FeasableSolution getCopy() {
		List<Rectangle> copy = new LinkedList<Rectangle>();
		for (Rectangle r : rectangles){
			copy.add(r.getCopy());
		}
		GeometryBased gb = new GeometryBased(copy, rm.getDh());
		gb.taboos = taboos;
		gb.setCost(cost);
		gb.costBefore = costBefore;
		gb.lastOperation = lastOperation;
		return gb;
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
