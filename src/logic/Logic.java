package logic;

import java.util.LinkedList;
import java.util.List;

import logic.problems.GeometryBased;
import logic.problems.OverlapBased;
import logic.problems.RuleBased;

public class Logic{

	private ProblemType problemType;
	
	private List<Rectangle> rectangles;
	
	private FeasableSolution fs;
	
	private boolean debugMode;
	
	private boolean stepByStep1 = false;
	
	private double temperature = 100;
	
	private double temperatureSteps = 0.5;
	
	private FeasableSolution bestFeasableSolutionSeenSoFar = null;
	
	private int numberOfTaboosSearchSteps = 10;
	
	private int tabooStepCounter = 0;
	
	private int tabooRemoveFrequency = 20;
	
	private boolean searchRunning = false;
	
	public Logic(ProblemType problemType){
		this.problemType = problemType;
		rectangles = new LinkedList<Rectangle>();
		debugMode = false;
		initiate(10.0, true);
		bestFeasableSolutionSeenSoFar = fs.getCopy();
	}
	
	public Logic(ProblemType problemType, boolean randomFeasableSolution){
		this.problemType = problemType;
		rectangles = new LinkedList<Rectangle>();
		debugMode = false;
		initiate(10.0, randomFeasableSolution);
		bestFeasableSolutionSeenSoFar = fs.getCopy();
	}
	
	
	/**
	 * 
	 * @param rectangles list of the rectangles on which the algorithms will be performed.
	 */
	public Logic(ProblemType problemType, List<Rectangle> rectangles, double fieldSize, boolean randomFeasableSolution) {
		this.problemType = problemType;
		this.rectangles = rectangles;
		if (problemType == ProblemType.GEOMETRY_BASED)
			initiate((int)(fieldSize), randomFeasableSolution);
		else if (problemType == ProblemType.RULE_BASED)
			fs = new RuleBased(rectangles, fieldSize);
		else if (problemType == ProblemType.OVERLAP_BASED){
			fs = new OverlapBased(rectangles, fieldSize);
		}
		bestFeasableSolutionSeenSoFar = fs.getCopy();
	}
	
	private void initiate(double fieldSize, boolean randomFeasableSolution){
		fs = new GeometryBased(rectangles, fieldSize, randomFeasableSolution);
	}

	public List<Rectangle> getRectangles() {
		return rectangles;
	}
	
	public void localSearch(){
		double cost = fs.getCost();
		if (!debugMode){
			while (fs.updateToNextFeasableSolution() && searchRunning){
				if (fs.getCost() < cost-Rectangle.collisionTolerance){
					cost = fs.getCost();
					fs.reset();

				}
				else if (Rectangle.equal(fs.getCost(), cost)){
					fs.undo();
					//return;
				}
				else{
					fs.undo();
				}
				
			}
			fs.reset();
		}
		else{
			while (fs.updateToNextFeasableSolution()){
				//if (stepByStep)
				//	return;
				if (fs.getCost() < cost){
					cost = fs.getCost();
					fs.reset();
					if (stepByStep1)
						break;
				}
				else if (Rectangle.equal(fs.getCost(), cost)){
					
					fs.undo();
					//return;
				}
				else{
					fs.undo();
				}
			}
			fs.reset();
		}
//		if (!Rectangle.equal(((RuleBased) fs).getRm().getCost(), fs.getCost())){
//			double i = ((RuleBased) fs).getRm().getCost();
//			double j = fs.getCost();
//			int z = 0;
//		}
	}
	
	public void simulatedAnnealing(){
		
		double cost = fs.getCost();
		if (!debugMode){
			boolean doReset = true;
			while (fs.updateToNextFeasableSolution() && searchRunning){
				if (fs.getCost() < cost-Rectangle.collisionTolerance){
					cost = fs.getCost();
					fs.reset();
					if (bestFeasableSolutionSeenSoFar == null)
						bestFeasableSolutionSeenSoFar = fs.getCopy();
					if (bestFeasableSolutionSeenSoFar != null
							&& bestFeasableSolutionSeenSoFar.getCost() > fs.getCost())
						bestFeasableSolutionSeenSoFar = fs.getCopy();

				}
				else if (probabilisticYesNoDecision(cost-Rectangle.collisionTolerance, fs.getCost())){
					cost = fs.getCost(); // NO RESET!
					doReset = false;
				}
				else {
					fs.undo();
				}

				decreaseTemperature();
			}
			if (doReset)
				fs.reset();
			if (bestFeasableSolutionSeenSoFar != null){
				bestFeasableSolutionSeenSoFar.reset();
				fs = bestFeasableSolutionSeenSoFar;
			}
			
		}
		else{
			boolean doReset = true;
			boolean result = fs.updateToNextFeasableSolution();
			while (result){
				//if (stepByStep)
				//	return;
				if (fs.getCost() < cost-Rectangle.collisionTolerance){
					cost = fs.getCost();
					fs.reset();
					if (bestFeasableSolutionSeenSoFar == null)
						bestFeasableSolutionSeenSoFar = fs.getCopy();
					if (bestFeasableSolutionSeenSoFar != null
							&& bestFeasableSolutionSeenSoFar.getCost() > fs.getCost())
						bestFeasableSolutionSeenSoFar = fs.getCopy();
					if (stepByStep1){
						decreaseTemperature();
						break;
					}
				}
				else if (probabilisticYesNoDecision(cost-Rectangle.collisionTolerance, fs.getCost())){
					cost = fs.getCost(); // NO RESET!
					if (stepByStep1){
						decreaseTemperature();
						doReset = false;
						break;
					}
				}
				else {
					fs.undo();
				}
				decreaseTemperature();
				result = fs.updateToNextFeasableSolution();
			}
			if (doReset)
				fs.reset();
			if (!result){
				if (bestFeasableSolutionSeenSoFar != null){
					bestFeasableSolutionSeenSoFar.reset();
					fs = bestFeasableSolutionSeenSoFar;
				}
			}
				
		}
	}
	
	private void decreaseTemperature(){
//		System.out.println("temperature = " + temperature);
		temperature = Math.max(0, temperature-temperatureSteps);
	}
	
	private boolean probabilisticYesNoDecision(double cost_star, double cost){
		
		if (Rectangle.equal(0, temperature))
			return false;
		double decision = Math.pow(Math.E, (cost_star-cost)/(temperature));
		//System.out.println("temperature = " + temperature + ", decision = " + decision);
		return Math.random() < decision;
	}
	
	public void tabuSearch(){
		FeasableSolution bestFeasableSolution = null;
		double cost = Integer.MAX_VALUE;//fs.getCost();
		if (!debugMode){
			tabooStepCounter = 0;
			while (tabooStepCounter < numberOfTaboosSearchSteps && searchRunning){
				++tabooStepCounter;
				bestFeasableSolution = null;
				cost = Integer.MAX_VALUE;
				while (fs.updateToNextFeasableSolution()){ // search for the best neighbored solution
					if (fs.getCost() < cost-Rectangle.collisionTolerance){
						cost = fs.getCost();
						
						if (bestFeasableSolution == null)
							bestFeasableSolution = fs;//.getCopy();
						if (bestFeasableSolution != null
								&& bestFeasableSolution.getCost() > fs.getCost()){
							bestFeasableSolution = fs.getCopy();
						}
					}
					else {
						fs.undo();
					}
				}
				
				if (bestFeasableSolution != null)
					fs = bestFeasableSolution;
				fs.reset();
				if (fs.getLastOperation() != null)
					fs.getTaboos().addLast(fs.getLastOperation());
				if (tabooStepCounter % 10 == 0)
					fs.getTaboos().removeFirst();
				if (bestFeasableSolution != null &&  bestFeasableSolutionSeenSoFar != null
						&& bestFeasableSolution.getCost() < bestFeasableSolutionSeenSoFar.getCost()){
					bestFeasableSolutionSeenSoFar = bestFeasableSolution.getCopy();
					numberOfTaboosSearchSteps = tabooStepCounter+10;
				}
			}
			bestFeasableSolutionSeenSoFar.reset();
			fs = bestFeasableSolutionSeenSoFar;
		}
		else{
			if (tabooStepCounter < numberOfTaboosSearchSteps){
				++tabooStepCounter;
				bestFeasableSolution = null;
				cost = Integer.MAX_VALUE;
				while (fs.updateToNextFeasableSolution()){
					//if (stepByStep)
					//	return;
					if (fs.getCost() < cost-Rectangle.collisionTolerance){
						cost = fs.getCost();
						
						if (bestFeasableSolution == null)
							bestFeasableSolution = fs.getCopy();//.getCopy();
						if (bestFeasableSolution != null
								&& bestFeasableSolution.getCost() > fs.getCost())
							bestFeasableSolution = fs.getCopy();//.getCopy();
					}
					fs.undo();
				}
				
				if (bestFeasableSolution != null)
					fs = bestFeasableSolution;
				fs.reset();
				if (fs.getLastOperation() != null)
					fs.getTaboos().addLast(fs.getLastOperation());
				if (tabooStepCounter % 10 == 0)
					fs.getTaboos().removeFirst();
				if (bestFeasableSolution.getCost() < bestFeasableSolutionSeenSoFar.getCost()){
					bestFeasableSolutionSeenSoFar = bestFeasableSolution.getCopy();
					numberOfTaboosSearchSteps = tabooStepCounter+10;
				}
			}
			else{
				bestFeasableSolutionSeenSoFar.reset();
				fs = bestFeasableSolutionSeenSoFar;
			}
		}
	}

	public FeasableSolution getFs() {
		return fs;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	public void debugButton1Pressed(SearchAlgorithm searchAlogrithm){
		if (debugMode){
			
		}
	}
	
	public void debugButton2Pressed(SearchAlgorithm searchAlogrithm){
		if (debugMode){
			
			stepByStep1 = true;
			switch(searchAlogrithm){
			case LOCAL_SEARCH:
				localSearch();
				break;
			case SIMULATED_ANNEALING:
				simulatedAnnealing();
				break;
			case TABU_SEARCH:
				tabuSearch();
				break;
			}
		}
	}
	
	public void debugButton3Pressed(SearchAlgorithm searchAlogrithm){
		if (debugMode){
			debugMode = false;
			switch(searchAlogrithm){
			case LOCAL_SEARCH:
				localSearch();
				break;
			case SIMULATED_ANNEALING:
				simulatedAnnealing();
				break;
			case TABU_SEARCH:
				tabuSearch();
				break;
			}
		}
	}

	public boolean isSearchRunning() {
		return searchRunning;
	}

	public void setSearchRunning(boolean searchRunning) {
		this.searchRunning = searchRunning;
	}
	
	
}
