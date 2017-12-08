package logic.tests;

import java.util.LinkedList;
import java.util.List;

import logic.Logic;
import logic.ProblemType;
import logic.Rectangle;
import logic.SearchAlgorithm;
import main.GuiControl;
import main.LogicControl;

public class MainTest {
	
	

	private LogicControl logicControl;
	
	private GuiControl guiControl;
	
	private boolean testRunning = false;
	
	private long calculationTime = 0;
	
	private double[] times;
	private double[] results;
	
	public MainTest(LogicControl logicControl, GuiControl guiControl) {
		super();
		this.logicControl = logicControl;
		this.guiControl = guiControl;
		times = new double[9];
		results = new double[9];
	}
	
	public long startNewTest(final int numberInstances, final int numberRectangles, final double minSize, final double maxSize){
		
		Thread t = new Thread(){
			public void run(){
				calculationTime = System.currentTimeMillis();
				testRunning = true;
				startTest(numberInstances, numberRectangles, minSize, maxSize);
				testRunning = false;
				calculationTime = calculationTime - System.currentTimeMillis();
			}
		};
		t.start();
		
		return calculationTime;
	}
	
	private  void startTest(final int numberInstances, final int numberRectangles, final double minSize, final double maxSize){
		for (int i = 0; i < 9; ++i){
			times[i] = 0;
			results[i] = 0;
		}
		//write times + results
		Thread searchThread = new Thread(){

			@Override
			public void run() {
				super.run();
				double fieldSize = (int)((minSize+maxSize)/2.0);
				for (int num = 0; num < numberInstances; ++num){
					List<Rectangle> rectangles = LogicControl.getRandomRectangles(numberRectangles, minSize, maxSize);
					
					int counterPt = -1;
					for (ProblemType pt : ProblemType.values()){
						counterPt++;
						//initiate
						
						int counterSa = -1;
						long calculationTime;
						for (SearchAlgorithm searchAlogrithm : SearchAlgorithm.values()){
							System.out.println("calculate problem " + pt + " with search algorithm " + searchAlogrithm);
							counterSa++;
							List<Rectangle> rectanglesCopy = new LinkedList<Rectangle>();
							for (Rectangle r : rectangles){
								rectanglesCopy.add(r.getCopy());
							}
							Logic logic = new Logic(pt, rectanglesCopy, fieldSize, true);
							logic.setSearchRunning(true);
							//start search
							calculationTime = System.currentTimeMillis();
							switch (searchAlogrithm){
							case LOCAL_SEARCH: 
								logic.localSearch();
								break;
							case SIMULATED_ANNEALING:
								logic.simulatedAnnealing();
								break;
							case TABU_SEARCH:
								logic.tabuSearch();
								break;
							}
							calculationTime = System.currentTimeMillis() - calculationTime;
							times[counterPt*3+counterSa] += calculationTime/numberInstances;
							results[counterPt*3+counterSa] += logic.getFs().getCost()/numberInstances;
							//end search
						}
					}
				}
				testRunning = false;
				System.out.println("Test finished");
				logicControl.testFinished(times, results);
			}
			
			
		};
		searchThread.start();
		
		
	}
}
