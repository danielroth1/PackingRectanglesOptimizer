package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import logic.Rectangle;

public class RectangleInfoPanel extends JPanel{

	private JTextField colorField;
	private JTextField xField;
	private JTextField yField;
	private JTextField widthField;
	private JTextField heightField;

	
	public RectangleInfoPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Box colorBox = createBox("Color: ");
		colorField = new JTextField();
		colorField.setForeground(Color.WHITE);
		colorBox.add(colorField);
		
		Box xFieldBox = createBox("x: ");
		xField = new JTextField();
		xFieldBox.add(xField);
		
		Box yFieldBox = createBox("y: ");
		yField = new JTextField();
		yFieldBox.add(yField);
		
		Box widthFieldBox = createBox("Width: ");
		widthField = new JTextField();
		widthFieldBox.add(widthField);
		
		Box heightFieldBox = createBox("Height: ");
		heightField = new JTextField();
		heightFieldBox.add(heightField);
		
		colorField.setEditable(false);
		xField.setEditable(false);
		yField.setEditable(false);
		widthField.setEditable(false);
		heightField.setEditable(false);
		Dimension d = new Dimension(1000, 30);
		colorField.setMaximumSize(d);
		xField.setMaximumSize(d);
		yField.setMaximumSize(d);
		widthField.setMaximumSize(d);
		heightField.setMaximumSize(d);
		
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
	
	public void update(Rectangle r){
		colorField.setText(r.getColor().toString());
		colorField.setBackground(r.getColor());
		xField.setText(String.valueOf(r.getX()));
		yField.setText(String.valueOf(r.getY()));
		widthField.setText(String.valueOf(r.getWidth()));
		heightField.setText(String.valueOf(r.getHeight()));
	}
	
	public void reset(){
		colorField.setText("");
		colorField.setBackground(UIManager.getColor("Panel.background"));
		xField.setText("");
		yField.setText("");
		widthField.setText("");
		heightField.setText("");
	}

	public JTextField getColorField() {
		return colorField;
	}

	public JTextField getxField() {
		return xField;
	}

	public JTextField getyField() {
		return yField;
	}

	public JTextField getWidthField() {
		return widthField;
	}

	public JTextField getHeightField() {
		return heightField;
	}
	
	

}
