package main;

import gui.ExtraData;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import logic.Logic;
import logic.ProblemType;
import logic.Rectangle;
import logic.SearchAlgorithm;
import logic.tests.MainTest;

public class LogicControl {
	
	private GuiControl guiControl;
	private Logic logic;
	private boolean debugState = false;
	private Thread searchThread;
	private List<Rectangle> rectanglesCopy;
	private MainTest mainTest;
	
	private int millisBetweenUpdates = 50;
	
	public LogicControl() {
		//logic = new Logic(ProblemType.GEOMETRY_BASED);
		create(ProblemType.GEOMETRY_BASED, SearchAlgorithm.LOCAL_SEARCH, 0, 0, 0);
		Thread t = new Thread(){

			@Override
			public void run() {
				super.run();
				guiControl = new GuiControl(LogicControl.this);
			}
			
		};
		
		t.start();
		rectanglesCopy = new LinkedList<Rectangle>();
		
		mainTest = new MainTest(this, guiControl);
	}
	
	public void create(ProblemType problemType, SearchAlgorithm searchAlogrithm, int number, double minSize, double maxSize){
		
		List<Rectangle> rectangles = new LinkedList<Rectangle>();
		Rectangle r1 = new Rectangle(55, 55, 50, 50);
		Rectangle r2 = new Rectangle(55, 105, 50, 50);
		Rectangle r3 = new Rectangle(5, 55, 50, 50);
		Rectangle r4 = new Rectangle(55, 5, 50, 50);
		rectangles.add(r1);
		rectangles.add(r2);
		rectangles.add(r3);
		rectangles.add(r4);
		
		List<Rectangle> list = getRandomRectangles(number, minSize, maxSize);
		rectanglesCopy = new LinkedList<Rectangle>();
		for (Rectangle r : list){
			rectanglesCopy.add(r.getCopy());
		}
		//logic = new Logic(problemType, rectangles);
		double fieldSize = (int)((minSize+maxSize)/2.0);
		logic = new Logic(problemType, list, fieldSize, true);
		logic.setDebugMode(debugState);
	}
	
	public void search(final SearchAlgorithm searchAlogrithm){
		logic.setSearchRunning(true);
		LogicControl.this.guiControl.repaint();
		searchThread = new Thread(){

			@Override
			public void run() {
				super.run();
				
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
				logic.setSearchRunning(false);
				LogicControl.this.guiControl.repaint();
			}
			
			
		};
		searchThread.start();
		
		Thread guiUpdateThread = new Thread(){

			@Override
			public void run() {
				super.run();
				while (LogicControl.this.logic.isSearchRunning()){
					
					try {
						LogicControl.this.guiControl.repaint();
					} catch (ConcurrentModificationException | NoSuchElementException | NullPointerException e) {

					}
					try {
						Thread.sleep(millisBetweenUpdates);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				LogicControl.this.guiControl.repaint();
			}
			
		};
		
		guiUpdateThread.start();
		/*
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
		
		guiControl.repaint();
		*/
	}
	
	public void stop(){
		logic.setSearchRunning(false);
		guiControl.repaint();
	}
	
	public void reset(ProblemType problemType){
		logic = new Logic(problemType, rectanglesCopy, ((ExtraData)logic.getFs()).getRm().getDh(), true);
		logic.setDebugMode(debugState);
		guiControl.repaint();
	}
	
	public void debugButton1Pressed(SearchAlgorithm searchAlogrithm){
		logic.debugButton1Pressed(searchAlogrithm);
		guiControl.repaint();
	}
	
	public void debugButton2Pressed(final SearchAlgorithm searchAlogrithm){
		logic.setSearchRunning(true);
		Thread guiUpdateThread = new Thread(){
			@Override
			public void run() {
				super.run();
				while (LogicControl.this.logic.isSearchRunning()){
					
					try {
						LogicControl.this.guiControl.repaint();
					} catch (ConcurrentModificationException | NoSuchElementException | NullPointerException e) {

					}
					try {
						Thread.sleep(millisBetweenUpdates);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				LogicControl.this.guiControl.repaint();
			}
			
		};
		
		searchThread = new Thread(){

			@Override
			public void run() {
				super.run();
				logic.debugButton2Pressed(searchAlogrithm);
				logic.setSearchRunning(false);
			}
			
		};
		guiUpdateThread.start();
		searchThread.start();
		guiControl.repaint();
	}
	
	public void debugButton3Pressed(final SearchAlgorithm searchAlogrithm){
		logic.setSearchRunning(true);
		Thread guiUpdateThread = new Thread(){
			@Override
			public void run() {
				super.run();
				while (LogicControl.this.logic.isSearchRunning()){
					
					try {
						LogicControl.this.guiControl.repaint();
					} catch (ConcurrentModificationException | NoSuchElementException | NullPointerException e) {

					}
					try {
						Thread.sleep(millisBetweenUpdates);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				LogicControl.this.guiControl.repaint();
			}
			
		};
		
		searchThread = new Thread(){

			@Override
			public void run() {
				super.run();
				logic.debugButton3Pressed(searchAlogrithm);
				logic.setSearchRunning(false);
			}
			
		};
		guiUpdateThread.start();
		searchThread.start();
		guiControl.repaint();
	}
	
	public void activeDebugMode(boolean activate){
		logic.setDebugMode(activate);
		debugState = activate;
	}
	
	public static List<Rectangle> getRandomRectangles(int number, double minSize, double maxSize){
		List<Rectangle> list = new LinkedList<Rectangle>();
		for (int i = 0; i < number; ++i){
			list.add(new Rectangle(0, 0, Math.max(minSize, Math.random()*maxSize), Math.max(minSize, Math.random()*maxSize)));
		}
		return list;
	}

	public Logic getLogic() {
		return logic;
	}
	
	public void readFile(List<Rectangle> rectangles, ProblemType problemType, double minSize, double maxSize){
		double fieldSize = (int)((minSize+maxSize)/2.0);
		this.logic = new Logic(problemType, rectangles, fieldSize, false);
		logic.setDebugMode(debugState);
		guiControl.repaint();
	}
	
	public void startNewTest(final int numberInstances, final int numberRectangles, final double minSize, final double maxSize){
		guiControl.updateTestSelected();
		System.out.println("test started");
		mainTest.startNewTest(numberInstances, numberRectangles, minSize, maxSize);
		
	}
	
	public void testFinished(double[] times, double[] results){
		guiControl.updateResultFrame(times, results);
		System.out.println("Results updated");
	}
	
	
	

}
