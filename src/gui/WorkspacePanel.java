package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import logic.FeasableSolution;
import logic.Rectangle;
import logic.map.RectangleMap;
import logic.problems.GeometryBased;
import logic.problems.OverlapBased;
import logic.problems.RuleBased;
import main.LogicControl;

public class WorkspacePanel extends JPanel{

	private Gui gui;
	
	private LogicControl logicControl;
	
	private boolean freezeRectangleInfo = false;
	
	private Rectangle currentRectangle = null;
	
	private double zoomFactor = 1;
	
	private boolean visibleMapLayout = false;
	
	private boolean visibleMinimumSquare = true;
	
	public static final Color standardColor = new Color(230, 233, 236);
	
	final static float dash1[] = {10.0f};
	
	public final static BasicStroke basicStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
	
	public WorkspacePanel(Gui gui, LogicControl logicControl) {
		this.gui = gui;
		this.logicControl = logicControl;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				freezeRectangleInfo = !freezeRectangleInfo;
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				if (!freezeRectangleInfo){
					if (WorkspacePanel.this.logicControl.getLogic().getFs() instanceof GeometryBased){
						Point2D.Double p = new Point2D.Double(e.getX()/zoomFactor, e.getY()/zoomFactor);
						GeometryBased gb = (GeometryBased) WorkspacePanel.this.logicControl.getLogic().getFs();
						Rectangle r = gb.getRm().getRectangleAt(new Point2D.Double(p.x, p.y));
						if (r != null && currentRectangle != r){
							currentRectangle = r;
							WorkspacePanel.this.gui.getRectangleInfoPanel().update(r);
						}
						if (r == null){
							currentRectangle = null;
							WorkspacePanel.this.gui.getRectangleInfoPanel().reset();
						}
					}
					else if (WorkspacePanel.this.logicControl.getLogic().getFs() instanceof RuleBased){
						Point2D.Double p = new Point2D.Double(e.getX()/zoomFactor, e.getY()/zoomFactor);
						RuleBased rb = (RuleBased) WorkspacePanel.this.logicControl.getLogic().getFs();
						Rectangle r = rb.getRm().getRectangleAt(new Point2D.Double(p.x, p.y));
						if (r != null && currentRectangle != r){
							currentRectangle = r;
							WorkspacePanel.this.gui.getRectangleInfoPanel().update(r);
						}
						if (r == null){
							currentRectangle = null;
							WorkspacePanel.this.gui.getRectangleInfoPanel().reset();
						}
					} 
					else if (WorkspacePanel.this.logicControl.getLogic().getFs() instanceof OverlapBased){
						Point2D.Double p = new Point2D.Double(e.getX()/zoomFactor, e.getY()/zoomFactor);
						OverlapBased rb = (OverlapBased) WorkspacePanel.this.logicControl.getLogic().getFs();
						Rectangle r = rb.getRm().getRectangleAt(new Point2D.Double(p.x, p.y));
						if (r != null && currentRectangle != r){
							currentRectangle = r;
							WorkspacePanel.this.gui.getRectangleInfoPanel().update(r);
						}
						if (r == null){
							currentRectangle = null;
							WorkspacePanel.this.gui.getRectangleInfoPanel().reset();
						}
					} 
					
				}
			}
		});
		addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				super.mouseWheelMoved(e);
				if (e.getWheelRotation()<0){ //up
					zoomFactor = Math.max(zoomFactor+0.1, 0.1);
				}
				else{ //down
					zoomFactor = Math.min(zoomFactor-0.1, 10);
				}
				WorkspacePanel.this.gui.repaint();
			}
		});
		setBackground(standardColor);
	}

	@Override
	protected void paintComponent(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		super.paintComponent(g);
		
		RectangleMap rm = null;
		FeasableSolution fs = null;
		ExtraData ed = (ExtraData)logicControl.getLogic().getFs();
		if (logicControl.getLogic().getFs() instanceof GeometryBased){
			fs = (GeometryBased)logicControl.getLogic().getFs();
			rm = ((GeometryBased)logicControl.getLogic().getFs()).getRm();
		}
		else if (logicControl.getLogic().getFs() instanceof RuleBased){
			fs = (RuleBased)logicControl.getLogic().getFs();
			rm = ((RuleBased)logicControl.getLogic().getFs()).getRm();
		}
		else if (logicControl.getLogic().getFs() instanceof OverlapBased){
			fs = (OverlapBased)logicControl.getLogic().getFs();
			rm = ((OverlapBased)logicControl.getLogic().getFs()).getRm();
		}
		
		if (visibleMinimumSquare){
			if (rm != null){
				g.setColor(Color.black);
				g.setStroke(basicStroke);
				g.drawRect(0, 0, (int)(ed.getCostBefore()*zoomFactor), (int)(ed.getCostBefore()*zoomFactor));
				g.setStroke(new BasicStroke());
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, (int)(fs.getCost()*zoomFactor), (int)(fs.getCost()*zoomFactor));
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, (int)(fs.getCost()*zoomFactor), (int)(fs.getCost()*zoomFactor));
			}
		}
		
		//draw rectangles
		for(Rectangle r : rm.getRectangles()){
			g.setColor(r.getColor());
			g.fillRect((int)(r.getX()*zoomFactor), (int)(r.getY()*zoomFactor), (int)(r.getWidth()*zoomFactor), (int)(r.getHeight()*zoomFactor));
			
			g.setColor(Color.BLACK);
			drawtRectangle(r, g);
		}

		//draw field
		if (visibleMapLayout){
			if (rm != null){
				for (int x = 0; x < rm.getNumberOfFieldsX(); ++x){
					for (int y = 0; y < rm.getNumberOfFieldsY(); ++y){
						g.setColor(new Color(255, 172, 0, Math.min((int)(255*4.0/5.0), (int)(rm.getMap()[x][y].getRectangles().size()*255.0/5.0))));
						g.fillRect((int)((rm.getDw()*x)*zoomFactor), (int)((rm.getDh()*y)*zoomFactor), 
								(int)((rm.getDw())*zoomFactor), (int)((rm.getDh())*zoomFactor));
					}
				}
			}
		}
		
		
		
		
	}
	
	private void drawtRectangle(Rectangle r, Graphics g){
		g.drawRect((int)(r.getX()*zoomFactor), (int)(r.getY()*zoomFactor), (int)(r.getWidth()*zoomFactor), (int)(r.getHeight()*zoomFactor));
	}

	public void setVisibleMapLayout(boolean visible){
		visibleMapLayout = visible;
	}
	
	public void setVisibleMinimumSquare(boolean visible){
		visibleMinimumSquare = visible;
	}
	
	

}
