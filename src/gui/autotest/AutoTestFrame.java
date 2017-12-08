package gui.autotest;

import gui.WorkspacePanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.LogicControl;

public class AutoTestFrame extends JDialog{

	
	private LogicControl logicControl;
	private JPanel contentPane;
	//int numberInstances, int numberRectangles, double minSize, double maxSize
	private JTextField numberInstancesField;
	private JTextField numberRectangles;
	private JTextField minSize;
	private JTextField maxSize;
	
	public AutoTestFrame(LogicControl logicControl) {
		this.logicControl = logicControl;
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		numberInstancesField = new JTextField("3");
		numberRectangles = new JTextField("20");
		minSize = new JTextField("10");
		maxSize = new JTextField("200");
		
		Box fieldBox = new Box(BoxLayout.Y_AXIS);
		Box numberBox = new Box(BoxLayout.X_AXIS);
		numberBox.add(new JLabel("#Instances: "));
		numberBox.add(Box.createRigidArea(new Dimension(5, 0)));
		numberBox.add(numberInstancesField);
		numberBox.add(Box.createRigidArea(new Dimension(10, 0)));
		numberBox.add(new JLabel("#Rectangles: "));
		numberBox.add(Box.createRigidArea(new Dimension(5, 0)));
		numberBox.add(numberRectangles);
		fieldBox.add(numberBox);
		Box sizeBox = new Box(BoxLayout.X_AXIS);
		sizeBox.add(new JLabel("min: "));
		sizeBox.add(Box.createRigidArea(new Dimension(5, 0)));
		sizeBox.add(minSize);
		sizeBox.add(Box.createRigidArea(new Dimension(10, 0)));
		sizeBox.add(new JLabel("max: "));
		sizeBox.add(Box.createRigidArea(new Dimension(5, 0)));
		sizeBox.add(maxSize);
		fieldBox.add(Box.createRigidArea(new Dimension(0, 10)));
		fieldBox.add(sizeBox);
		fieldBox.add(Box.createRigidArea(new Dimension(0, 10)));
		
		minSize.setMinimumSize(new Dimension(30, 20));
		maxSize.setMinimumSize(new Dimension(30, 20));
		numberRectangles.setMinimumSize(new Dimension(30, 20));
		numberInstancesField.setMinimumSize(new Dimension(30, 20));
		minSize.setMaximumSize(new Dimension(200, 30));
		maxSize.setMaximumSize(new Dimension(200, 30));
		numberRectangles.setMaximumSize(new Dimension(200, 30));
		numberInstancesField.setMaximumSize(new Dimension(200, 30));
		
		contentPane.add(fieldBox);
		contentPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), 
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		
		JButton buttonStartTest = new JButton("Execute");
		buttonStartTest.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonStartTest.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AutoTestFrame.this.logicControl.startNewTest(Integer.valueOf(numberInstancesField.getText()), Integer.valueOf(numberRectangles.getText()),
						Integer.valueOf(minSize.getText()), Integer.valueOf(maxSize.getText()));
				
			}
		});
		Box buttonBox = new Box(BoxLayout.X_AXIS);
		buttonBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBox.add(buttonStartTest);
		contentPane.add(buttonBox);
		
		setBackground(WorkspacePanel.standardColor);
		setSize(new Dimension(300, 150));
		setLocationRelativeTo(null);
	}

}
