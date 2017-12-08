package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logic.map.RectangleMap;

public class InfoPanel extends JPanel{
	
	private JTextField costFiel;
	private JTextField numberOfRectanlgesField;


	public InfoPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Box numberOfRectanlgesBox = createBox("Number of rectangles: ");
		numberOfRectanlgesField = new JTextField();
		numberOfRectanlgesBox.add(numberOfRectanlgesField);
		
		Box costBox = createBox("Costs: ");
		costFiel = new JTextField();
		costBox.add(costFiel);
		
		
		costFiel.setEditable(false);
		numberOfRectanlgesField.setEditable(false);
		Dimension d = new Dimension(1000, 30);
		costFiel.setMaximumSize(d);
		numberOfRectanlgesField.setMaximumSize(d);
		
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5))));
	}

	private Box createBox(String labelName){
		Box box = new Box(BoxLayout.X_AXIS);
		JLabel label = new JLabel(labelName);
		box.add(label);
		box.add(Box.createRigidArea(new Dimension(5, 0)));
		add(box);
		add(Box.createRigidArea(new Dimension(0, 5)));
		return box;
	}
	
	public void update(RectangleMap rm){
		if (rm == null){
			costFiel.setText("");
			numberOfRectanlgesField.setText("");
		}
		else{
			String s;
			try {
				s = String.valueOf(rm.getCost());
				costFiel.setText(s.substring(0, (int)Math.min(10, s.length())));
				numberOfRectanlgesField.setText(String.valueOf(rm.getRectangles().size()));
			} catch (NullPointerException e) {
				costFiel.setText("");
				numberOfRectanlgesField.setText("");
			}
		}

	}

	public JTextField getCostFiel() {
		return costFiel;
	}

	public JTextField getNumberOfRectanlgesField() {
		return numberOfRectanlgesField;
	}
	
	
}
