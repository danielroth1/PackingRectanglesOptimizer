package main;

import gui.Gui;

public class GuiControl {

	private Gui gui;
	
	private LogicControl logicControl;
	
	public GuiControl(LogicControl logicControl) {
		gui = new Gui(logicControl);
		this.logicControl = logicControl;
	}

	public static void main(String[] args){
		

		String s = "";
		/*
		for (int i = 0; i < 500; ++i){
			s += (i*2+1) + ", ";
		}
		*/
		/*
		List<Integer> testList = new LinkedList<Integer>();
		for (int i = 0; i < 10; ++i){
			testList.add(i);
		}
		String string = "";
		Iterator<Integer> it = testList.iterator();
		loop:
		while (it.hasNext()){
			
			int next = it.next();
			if (!it.hasNext())
				break loop;
			string += next;
		}
		System.out.println(string);
		*/
		System.out.println(s);
		new LogicControl();
	}
	
	public void repaint(){
		gui.updateInfoPanel();
		gui.repaint();
	}
	
	public void updateTestSelected(){
		gui.updateTestSelected();
	}
	
	public void updateResultFrame(double[] times, double[] results){
		gui.updateResultFrame(times, results);
	}
	
}
