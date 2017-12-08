package gui.autotest;

import gui.Gui;
import gui.WorkspacePanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import logic.ProblemType;

public class AutoTestResultFrame extends JFrame{

	
	private JPanel workspacePanel;
	
	private double[] times;
	private double[] results;
	
	double maxHeight = 0;
	double maxHeightTime = 0;
	double maxHeightResults = 0;
	
	private int fontSize = 5;
	
	private boolean isUpdated = false;
	
	private int offsetY = 100;
	private int workspacePanelHeightWithoutOffset = 800;
	private int workspacePanelHeight = workspacePanelHeightWithoutOffset + offsetY*3;
	
	public AutoTestResultFrame() {
		
		workspacePanel = new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g2) {
				super.paintComponent(g2);
				Graphics2D g = (Graphics2D) g2;
				if (isUpdated){
					
					double dh = 800/3+offsetY;//AutoTestResultFrame.this.getSize().getHeight()/3+offsetY;
					draw(g, ProblemType.GEOMETRY_BASED, dh, (int)(offsetY+dh*1), maxHeight, maxHeightTime, maxHeightResults);
					draw(g, ProblemType.RULE_BASED, dh, (int)(offsetY*2+dh*2), maxHeight, maxHeightTime, maxHeightResults);
					draw(g, ProblemType.OVERLAP_BASED, dh, (int)(offsetY*3+dh*3), maxHeight, maxHeightTime, maxHeightResults);
				}
				
			}
			
			private void draw(Graphics2D g, ProblemType pt, double dh, int yPosition, double maxHeight, double maxHeightTime, double maxHeightResults){
				int offsetX = 10;
				
				double dw = AutoTestResultFrame.this.getSize().getWidth()/3+offsetX;
				int offsetArray = 0;
				switch (pt){
				case GEOMETRY_BASED:
					g.drawString("Geometry based", (int)offsetX, (int)(yPosition-dh-offsetY/2));
					offsetArray = 0;
					break;
				case RULE_BASED:
					g.drawString("Rule based", (int)offsetX, (int)(yPosition-dh-offsetY/2));
					offsetArray = 3;
					
					break;
				case OVERLAP_BASED:
					g.drawString("Allow partial overlapping", (int)offsetX, (int)(yPosition-dh-offsetY/2));
					offsetArray = 6;
					
					break;
				}
				
				int x = (int)(dw/2.0);
				int y = (int)yPosition;
				g.drawString(Gui.localSearch, x-Gui.localSearch.length()*fontSize/2, (int)(y-dh-offsetY/4));
				drawGraph(g, x, y, (int)(times[0+offsetArray]/maxHeightTime*dh), (int)(results[0+offsetArray]/maxHeightResults*dh), (int)(dh), times[0+offsetArray], results[0+offsetArray]);
				
				x = (int)(dw/2.0+dw);
				y = (int)yPosition;
				g.drawString(Gui.simulatedAnnealing, x-Gui.simulatedAnnealing.length()*fontSize/2, (int)(y-dh-offsetY/4));
				drawGraph(g, x, y, (int)(times[1+offsetArray]/maxHeightTime*dh), (int)(results[1+offsetArray]/maxHeightResults*dh), (int)(dh), times[1+offsetArray], results[1+offsetArray]);
				
				x = (int)(dw/2.0+dw*2);
				y = (int)yPosition;
				g.drawString(Gui.tabuSearch, x-Gui.tabuSearch.length()*fontSize/2, (int)(y-dh-offsetY/4));
				drawGraph(g, x, y, (int)(times[2+offsetArray]/maxHeightTime*dh), (int)(results[2+offsetArray]/maxHeightResults*dh), (int)(dh), times[2+offsetArray], results[2+offsetArray]);
				
			}
			
			private void drawGraph(Graphics2D g, int x, int y, int heightTime, int heightResult, int maxHeight, double time, double result){
				int widthBar = 20;
				int distanceBetweenBars = 40;
				int dbb = distanceBetweenBars/2;
				
				//time
				g.setColor(Color.RED);
				g.fillRect((int)x-widthBar-dbb, y-heightTime, widthBar, heightTime);
				
				//result
				g.setColor(Color.BLUE);
				g.fillRect((int)x+dbb, y-heightResult, widthBar, heightResult);
				
				g.setColor(Color.BLACK);
				g.drawRect((int)x-widthBar-dbb, y-heightTime, widthBar, heightTime);
				g.drawRect((int)x+dbb, y-heightResult, widthBar, heightResult);
				
				String timeString = String.valueOf(time);
				timeString = timeString.substring(0, Math.min(timeString.length(), 6));
				g.drawString(timeString, (int)(x-widthBar/2-timeString.length()*fontSize/2-dbb), y-maxHeight-2);
				g.setStroke(WorkspacePanel.basicStroke);
				g.drawLine((int)(x-widthBar/2-dbb), y-heightTime, (int)(x-widthBar/2-dbb), y-maxHeight+2);
				g.setStroke(new BasicStroke());
				
				String resultString = String.valueOf(result);
				resultString = resultString.substring(0, Math.min(resultString.length(), 6));
				g.drawString(resultString, (int)(x+widthBar/2-resultString.length()*fontSize/2+dbb), y-maxHeight-2);
				g.setStroke(WorkspacePanel.basicStroke);
				g.drawLine((int)(x+widthBar/2+dbb), y-heightResult, (int)(x+widthBar/2+dbb), y-maxHeight+2);
				g.setStroke(new BasicStroke());
			}
		};
		
		workspacePanel.setBackground(WorkspacePanel.standardColor);
		JScrollPane scrollPane = new JScrollPane(workspacePanel);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.add(scrollPane);
		setContentPane(contentPane);
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent arg0) {
				super.componentResized(arg0);
				Dimension size = new Dimension((int)(AutoTestResultFrame.this.getSize().getWidth()-30), workspacePanelHeight+workspacePanelHeight/3+50*3);
				workspacePanel.setPreferredSize(size);
				//scrollPane.setSize(AutoTestResultFrame.this.getSize());
			}
			
		});
//		setContentPane(workspacePanel);
		setBackground(WorkspacePanel.standardColor);
		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		
	}
	
	public void update(double[] times, double[] results){
		this.times = times;
		this.results = results;
		maxHeight = 0;
		maxHeightTime = 0;
		maxHeightResults = 0;
		
		for (double d : times){
			maxHeightTime = Math.max(maxHeightTime, d);
		}
		for (double d : results){
			maxHeightResults = Math.max(maxHeightResults, d);
		}
		maxHeight = Math.max(maxHeightTime, maxHeightResults);
		
		isUpdated = true;
		repaint();
	}
	
	

}
