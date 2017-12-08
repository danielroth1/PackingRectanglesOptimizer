package gui;

import gui.autotest.AutoTestFrame;
import gui.autotest.AutoTestResultFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import logic.ProblemType;
import logic.Rectangle;
import logic.SearchAlgorithm;
import logic.map.RectangleMap;
import logic.problems.GeometryBased;
import logic.problems.OverlapBased;
import logic.problems.RuleBased;
import main.LogicControl;

public class Gui {

	private JFrame mainFrame;
	
	private JDesktopPane contentPanel;
	
	private JInternalFrame controlFrame;
	
	private JInternalFrame rectangleInfoFrame;
	private JInternalFrame infoFrame;
	
	private JPanel rectangleInfoPanel;
	private JPanel infoPanel;
	
	private WorkspacePanel workspacePanel;
	
	private JFrame aboutFrame;
	
	private JFileChooser fileChooser;
	
	private JComboBox<String> algorithmComboBox;
	
	private JComboBox<String> searchComboBox;
	
	private LogicControl logicControl;
	
	private JTextField numberField = new JTextField();
	private JTextField minField = new JTextField();
	private JTextField maxField = new JTextField();
	private JCheckBox debugCheckBox;
	private JCheckBoxMenuItem debugItem;
	
	private JLabel searchingLabel;
	
	//Tests
	private AutoTestFrame testFrame;
	private AutoTestResultFrame testResultFrame;
	
	//
	public static final String localSearch = "Local Search"; 
	public static final String simulatedAnnealing = "Simulated Annealing"; 
	public static final String tabuSearch = "Tabu Search";
	
	//
	public static final String geometryBased = "Geometry based";
	public static final String ruleBased = "Rule based";
	public static final String overlapBased = "Allow partial overlapping";
	
	private JMenuBar menuBar;

	
	public Gui(LogicControl logicControl) {
		this.logicControl = logicControl;
		testFrame = new AutoTestFrame(logicControl);
		testResultFrame = new AutoTestResultFrame();
		mainFrame = new JFrame("Packing Rectangles Optimizer");
		contentPanel = new JDesktopPane();
		mainFrame.setContentPane(contentPanel);
		
		controlFrame = new JInternalFrame("Tools");
		controlFrame.setVisible(true);
		JPanel controlPanel = (JPanel)controlFrame.getContentPane();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		//controlPanel.setMinimumSize(new Dimension(60, 0));
		//controlPanel.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
		//controlPanel.setPreferredSize(new Dimension(60, Integer.MAX_VALUE));
		
		//JTextField numberField = new JTextField();
		numberField.setText("20");
		//JTextField minField = new JTextField();
		minField.setText("20");
		//JTextField maxField = new JTextField();
		maxField.setText("200");
		
		JButton createButton = new JButton("Create");
		createButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				actionCreate();
			}
			
		});
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Gui.this.logicControl.search(readSearchAlgorithm());
				
			}
		});
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Gui.this.logicControl.stop();
				
			}
		});
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Gui.this.logicControl.reset(readyProblemType());
				
			}
		});
		
		JButton debugButton1 = new JButton(">");
		debugButton1.setToolTipText("Perform a single step in the solution search process.");
		JButton debugButton2 = new JButton(">>");
		debugButton2.setToolTipText("Go to the next solution.");
		JButton debugButton3 = new JButton(">>>");
		debugButton3.setToolTipText("Execute the algorithms without any interruptions.");
		debugButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Gui.this.logicControl.debugButton1Pressed(Gui.this.readSearchAlgorithm());
			}
		});
		debugButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Gui.this.logicControl.debugButton2Pressed(Gui.this.readSearchAlgorithm());
			}
		});
		debugButton3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Gui.this.logicControl.debugButton3Pressed(Gui.this.readSearchAlgorithm());
			}
		});
		debugCheckBox = new JCheckBox("Debug mode");
		debugCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					Gui.this.debugItem.setSelected(true);
					Gui.this.logicControl.activeDebugMode(true);
				}
				else{
					Gui.this.debugItem.setSelected(false);
					Gui.this.logicControl.activeDebugMode(false);
				}
				
			}
		});
		
		
		Box controlBox = new Box(BoxLayout.Y_AXIS);
		Dimension d = new Dimension(Integer.MAX_VALUE, 30);
//		controlBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//		controlBox.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		
		Box numberBox = new Box(BoxLayout.X_AXIS);
		numberBox.add(new JLabel("Quantity: "));
		numberBox.add(numberField);
		numberBox.setMaximumSize(d);
		controlBox.add(numberBox);
		controlBox.add(Box.createRigidArea(new Dimension(0, 5)));
		
//		Box sideLengthBox = new Box(BoxLayout.PAGE_AXIS);
		
//		sideLengthBox.add(new JLabel("Seitenl�nge:"));
		Box sideLengthBox = new Box(BoxLayout.X_AXIS);
		sideLengthBox.add(new JLabel("Side length"));
		sideLengthBox.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		sideLengthBox.setMaximumSize(new Dimension(d));
		controlBox.add(sideLengthBox);
		
		Box minBox = new Box(BoxLayout.X_AXIS);
		minBox.add(Box.createRigidArea(new Dimension(30, 0)));
		JLabel minLabel = new JLabel("min: ");
		JLabel maxLabel = new JLabel("max: ");
		minLabel.setPreferredSize(maxLabel.getPreferredSize());
		minBox.add(minLabel);
		minBox.add(minField);
		minBox.setMaximumSize(d);
		controlBox.add(minBox);
		controlBox.add(Box.createRigidArea(new Dimension(0, 5)));
		Box maxBox = new Box(BoxLayout.X_AXIS);
		maxBox.add(Box.createRigidArea(new Dimension(30, 0)));
		maxBox.add(maxLabel);
		maxBox.add(maxField);
		maxBox.setMaximumSize(d);
		controlBox.add(maxBox);
		controlBox.add(Box.createRigidArea(new Dimension(0, 5)));
//		sideLengthBox.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
//		controlBox.add(sideLengthBox);
		
		Box algorithmBox = new Box(BoxLayout.X_AXIS);
		
		algorithmBox.add(new JLabel("Problem type: "));
		algorithmBox.setMaximumSize(d);
		controlBox.add(algorithmBox);
		
		
		Box algorithmBox2 = new Box(BoxLayout.X_AXIS);
		initiateAlgorithmComboBox();
		algorithmBox2.add(algorithmComboBox);
		algorithmBox2.setMaximumSize(d);
		controlBox.add(algorithmBox2);
		controlBox.add(Box.createRigidArea(new Dimension(0, 5)));
		
		Box createBox = new Box(BoxLayout.X_AXIS);
		createBox.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		createBox.add(createButton);
		controlBox.add(createBox);
		
		Box searchBox = new Box(BoxLayout.X_AXIS);
		searchBox.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		searchBox.add(new JLabel("Search algo.: "));
		searchBox.setMaximumSize(d);
		controlBox.add(searchBox);
		
		Box searchBox2 = new Box(BoxLayout.X_AXIS);
		searchBox2.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		String[] algos = {localSearch, simulatedAnnealing, tabuSearch};
		searchComboBox = new JComboBox<String>(algos);
		searchBox2.add(searchComboBox);
		searchBox2.setMaximumSize(d);
		controlBox.add(searchBox2);
		controlBox.add(Box.createRigidArea(new Dimension(0, 5)));
		
		Box functionBox = new Box(BoxLayout.X_AXIS);
		functionBox.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		functionBox.add(startButton);
		functionBox.add(Box.createRigidArea(new Dimension(5, 0)));
		functionBox.add(stopButton);
		functionBox.setMinimumSize(d);
		controlBox.add(functionBox);
		controlBox.add(Box.createRigidArea(new Dimension(0, 5)));
		
		Box functionBox2 = new Box(BoxLayout.X_AXIS);
		functionBox2.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		functionBox2.add(resetButton);
		functionBox2.setMinimumSize(d);
		controlBox.add(functionBox2);
		controlBox.add(Box.createRigidArea(new Dimension(0, 5)));
		
		
		
		
		JLabel l = new JLabel("Debug:");
		Box debugLabelBox = new Box(BoxLayout.X_AXIS);
		debugLabelBox.add(l);
		debugLabelBox.setMaximumSize(d);
		debugLabelBox.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		debugLabelBox.add(debugCheckBox);
		controlBox.add(debugLabelBox);
		debugCheckBox.setSelected(false);
		
		
		Box debugBox = new Box(BoxLayout.X_AXIS);
		debugBox.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		//debugBox.add(debugButton1);
		//debugBox.add(Box.createRigidArea(new Dimension(5, 0)));
		debugBox.add(debugButton2);
		debugBox.add(Box.createRigidArea(new Dimension(5, 0)));
		debugBox.add(debugButton3);
		controlBox.add(Box.createRigidArea(new Dimension(0, 5)));
		debugBox.setMinimumSize(d);
		controlBox.add(debugBox);
		controlBox.add(Box.createRigidArea(new Dimension(0, 5)));
		
		Box searchingBox = new Box(BoxLayout.X_AXIS);
		searchingBox.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		searchingBox.setMaximumSize(d);
		searchingLabel = new JLabel("seraching...");
		searchingLabel.setVisible(false);
		searchingLabel.setForeground(Color.GREEN);
		searchingBox.add(searchingLabel);
		controlBox.add(searchingBox);
		
		controlPanel.add(controlBox);
//		controlPanel.add(Box.createVerticalGlue());
		controlPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		
		/*
		JLabel numberLabel = new JLabel("Anzahl");
		JLabel sideLengthLabel = new JLabel("Seitenl�nge");
		JLabel minLabel = new JLabel("min");
		JLabel maxLabel = new JLabel("max");
		JLabel algorithmLabel = new JLabel("Algorithmus");
		*/
		
		
		/*
		GroupLayout layout = new GroupLayout(controlPanel);
		controlPanel.setLayout(layout);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(numberLabel)
						.addComponent(sideLengthLabel)
						.addGroup(layout.createParallelGroup()
								.addComponent(minLabel)
								.addComponent(maxLabel))
						.addComponent(algorithmLabel))
				.addGroup(layout.createParallelGroup()
						.addComponent(numberField)
						.addComponent(minField)
						.addComponent(maxField)
						.addComponent(algorithmBar))
				.addComponent(createButton)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(numberLabel)
						.addComponent(numberField))
				.addComponent(sideLengthLabel)
				.addGroup(layout.createParallelGroup()
						.addComponent(minLabel)
						.addComponent(minField))
				.addGroup(layout.createParallelGroup()
						.addComponent(maxLabel)
						.addComponent(maxField))
				.addGroup(layout.createParallelGroup()
						.addComponent(algorithmLabel)
						.addComponent(algorithmBar))
				.addComponent(createButton)
		);
		*/
		
		Dimension d2 = new Dimension(200, 500);
		controlPanel.setMinimumSize(d2);
		controlPanel.setPreferredSize(d2);
		controlPanel.setMaximumSize(d2);
		controlPanel.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		controlPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		controlFrame.setMinimumSize(d2);
		controlFrame.setPreferredSize(d2);
		controlFrame.setMaximumSize(d2);
		
		controlBox.setOpaque(true);
		
		Dimension d3 = new Dimension(280, 250);
		rectangleInfoFrame = new JInternalFrame("Rectangle Information");
		rectangleInfoPanel = new RectangleInfoPanel();
		rectangleInfoFrame.setContentPane(rectangleInfoPanel);
		
		rectangleInfoFrame.setVisible(true);
		rectangleInfoFrame.setMinimumSize(d3);
		rectangleInfoFrame.setPreferredSize(d3);
		rectangleInfoFrame.setMaximumSize(d3);
		
		final Dimension d4 = new Dimension(d2.width, 150);
		infoFrame = new JInternalFrame("Information");
		infoPanel = new InfoPanel();
		infoFrame.setContentPane(infoPanel);
		
		infoFrame.setVisible(true);
		infoFrame.setMinimumSize(d3);
		infoFrame.setPreferredSize(d3);
		infoFrame.setMaximumSize(d3);
		
		workspacePanel = new WorkspacePanel(this, logicControl);
//		controlPanel.setBackground(Color.cyan);
//		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));
		//mainFrame.setLayout(null);
		contentPanel.setLayout(null);
		contentPanel.add(infoFrame, 0);
		contentPanel.add(controlFrame, 1);
		contentPanel.add(rectangleInfoFrame, 2);
		contentPanel.add(workspacePanel, 3);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(new Dimension(1024, 768));
		//controlFrame.setLocation(mainFrame.getWidth()-200, mainFrame.getHeight()-300);
		controlFrame.setBounds(mainFrame.getWidth()-(int)d2.getWidth()-20, mainFrame.getHeight()-(int)d2.getHeight()-40, 
				(int)d2.getWidth(), (int)d2.getHeight());
		rectangleInfoFrame.setBounds(mainFrame.getWidth()-(int)d2.getWidth()-20-(int)d3.getWidth()-20, mainFrame.getHeight()-(int)d3.getHeight()-40, 
				(int)d3.getWidth(), (int)d3.getHeight());
		infoFrame.setBounds(mainFrame.getWidth()-(int)d2.getWidth()-20, mainFrame.getHeight()-(int)d2.getHeight()-(int)d4.getHeight()-80, 
				(int)d4.getWidth(), (int)d4.getHeight());
		
		workspacePanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
		
		initiateFileChooser();
		initiateAboutFrame();
		initiateMenu();
		mainFrame.setJMenuBar(menuBar);
		
		mainFrame.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent arg0) {
				super.componentResized(arg0);
				workspacePanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
				Dimension d2 = new Dimension(200, 500);
				Dimension d3 = new Dimension(280, 250);
				controlFrame.setBounds(mainFrame.getWidth()-(int)d2.getWidth()-20, mainFrame.getHeight()-(int)d2.getHeight()-60, 
						(int)d2.getWidth(), (int)d2.getHeight());
				rectangleInfoFrame.setBounds(mainFrame.getWidth()-(int)d2.getWidth()-20-(int)d3.getWidth()-20, mainFrame.getHeight()-(int)d3.getHeight()-60, 
						(int)d3.getWidth(), (int)d3.getHeight());
				infoFrame.setBounds(mainFrame.getWidth()-(int)d2.getWidth()-20, mainFrame.getHeight()-(int)d2.getHeight()-(int)d4.getHeight()-80, 
						(int)d4.getWidth(), (int)d4.getHeight());
			}
			
		});
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	
	private void initiateFileChooser() {
		fileChooser = new JFileChooser();
		
	}

	private void initiateAboutFrame() {
		aboutFrame = new JFrame("About");
		aboutFrame.setMinimumSize(new Dimension(300, 200));
		aboutFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				super.windowClosing(arg0);
				aboutFrame.setVisible(false);
			}
		});
		JPanel contentPanel = (JPanel)aboutFrame.getContentPane();
		contentPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), 
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		JTextPane textField = new JTextPane();
		textField.setText("\nDeveloper: Daniel Roth\n"
				+ "\n"
				+ "If you have any questions, please don't hesitate to "
				+ "contact me at: daniel.roth@mailbase.info");
		
		StyledDocument sd = textField.getStyledDocument();
		Style style = textField.addStyle("Italic", null);
		StyleConstants.setItalic(style, true);
		style = textField.addStyle("Courier", null);
		StyleConstants.setFontFamily(style, "Courier");
		style = textField.addStyle("center", null);
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
		
		//sd.setParagraphAttributes(arg0, arg1, arg2, arg3);
		//textField.getStyledDocument().setCharacterAttributes(10, 10, textField.getStyle("Italic"), true);
		//sd.setCharacterAttributes(0, textField.getText().length(), textField.getStyle("center"), true);
		sd.setParagraphAttributes(0, textField.getText().length(), textField.getStyle("Courier"), true);
		sd.setParagraphAttributes(0, textField.getText().length(), textField.getStyle("center"), false);
		
		textField.setEditable(false);
		contentPanel.add(textField);
		aboutFrame.setLocationRelativeTo(null);
	}

	private void initiateAlgorithmComboBox(){
		String[] algos = {geometryBased, ruleBased, overlapBased};
		algorithmComboBox = new JComboBox<String>(algos);
		algorithmComboBox.setSelectedIndex(1);
		
	}
	
	private void initiateMenu(){
		menuBar = new JMenuBar();
		JMenu dataMenu = new JMenu("Data");
		JMenu editMenu = new JMenu("Edit");
		JMenu viewMenu = new JMenu("Views");
		JMenu testMenu = new JMenu("Test");
		JMenu helpMenu = new JMenu("Help");
		
		menuBar.add(dataMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		menuBar.add(testMenu);
		menuBar.add(helpMenu);
		
		JMenuItem newItem = new JMenuItem("New");
		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem loadItem = new JMenuItem("Load");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		dataMenu.add(newItem);
		dataMenu.add(saveItem);
		dataMenu.add(loadItem);
		dataMenu.add(exitItem);
		newItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Gui.this.actionCreate();
			}
		});
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fileChooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					try {
						FileOutputStream fout = new FileOutputStream(file);
						ObjectOutputStream oout = new ObjectOutputStream(fout);
						oout.writeObject(Gui.this.logicControl.getLogic().getRectangles());
						oout.close();
						fout.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		loadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					try {
						FileInputStream fin = new FileInputStream(file);
						ObjectInputStream oin = new ObjectInputStream(fin);
						@SuppressWarnings("unchecked")
						List<Rectangle> rectangles = (List<Rectangle>) oin.readObject();
						Gui.this.logicControl.readFile(rectangles, readyProblemType(), 
								Integer.valueOf(minField.getText()), Integer.valueOf(maxField.getText()));
						oin.close();
						fin.close();
					} catch (IOException | ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		JMenuItem startItem = new JMenuItem("Start");
		startItem.setToolTipText("Start search algorithm");
		debugItem = new JCheckBoxMenuItem("Debug mode");
		debugItem.setSelected(false);
		
		editMenu.add(startItem);
		editMenu.add(debugItem);
		
		startItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Gui.this.logicControl.search(readSearchAlgorithm());
			}
		});
		debugItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					Gui.this.debugCheckBox.setSelected(true);
					Gui.this.logicControl.activeDebugMode(true);
				}
				else{
					Gui.this.debugCheckBox.setSelected(false);
					Gui.this.logicControl.activeDebugMode(false);
				}
				
			}
		});
		
		
		JCheckBoxMenuItem toolsView = new JCheckBoxMenuItem("Tools");
		JCheckBoxMenuItem rectangleView = new JCheckBoxMenuItem("Rectangle information");
		JCheckBoxMenuItem mapItem = new JCheckBoxMenuItem("Map layout");
		JCheckBoxMenuItem minimumItem = new JCheckBoxMenuItem("Minimal rectangle");
		JCheckBoxMenuItem infoView = new JCheckBoxMenuItem("Information");
		toolsView.setSelected(true);
		rectangleView.setSelected(true);
		mapItem.setSelected(false);
		minimumItem.setSelected(true);
		infoView.setSelected(true);
		
		viewMenu.add(toolsView);
		viewMenu.add(rectangleView);
		viewMenu.add(infoView);
		
		viewMenu.add(mapItem);
		viewMenu.add(minimumItem);
		
		toolsView.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					controlFrame.setVisible(true);
				}
				else{
					controlFrame.setVisible(false);
				}
			}
		});
		rectangleView.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					rectangleInfoFrame.setVisible(true);
				}
				else{
					rectangleInfoFrame.setVisible(false);
				}
			}
		});
		infoView.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					infoFrame.setVisible(true);
				}
				else{
					infoFrame.setVisible(false);
				}
			}
		});
		
		mapItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					Gui.this.setVisibleMapLayout(true);
				}
				else{
					Gui.this.setVisibleMapLayout(false);
				}
				
			}
		});
		minimumItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					Gui.this.setVisibleMinimumSquare(true);
				}
				else{
					Gui.this.setVisibleMinimumSquare(false);
				}
				
			}
		});
		
		JMenuItem testItem = new JMenuItem("Test");
		JMenuItem resultItem = new JMenuItem("Results");
		testItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				testFrame.setVisible(true);
			}
		});
		resultItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				testResultFrame.setVisible(true);
			}
		});
		testMenu.add(testItem);
		testMenu.add(resultItem);
		
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				aboutFrame.setVisible(true);
			}
		});
		helpMenu.add(aboutItem);
		
	}
	
	private SearchAlgorithm readSearchAlgorithm(){
		SearchAlgorithm sa;
		String s = (String)searchComboBox.getSelectedItem();
		
		switch(s){
		case Gui.localSearch:
			sa = SearchAlgorithm.LOCAL_SEARCH;
			break;
		case Gui.simulatedAnnealing:
			sa = SearchAlgorithm.SIMULATED_ANNEALING;
			break;
		case Gui.tabuSearch:
			sa = SearchAlgorithm.TABU_SEARCH;
			break;
		default:
			sa = SearchAlgorithm.LOCAL_SEARCH;
			break;
		}
		return sa;
	}
	
	private ProblemType readyProblemType(){
		ProblemType pt;
		String s = (String)algorithmComboBox.getSelectedItem();
		switch(s){
		case Gui.geometryBased:
			pt = ProblemType.GEOMETRY_BASED;
			break;
		case Gui.ruleBased:
			pt = ProblemType.RULE_BASED;
			break;
		case Gui.overlapBased:
			pt = ProblemType.OVERLAP_BASED;
			break;
		default:
			pt = ProblemType.GEOMETRY_BASED;
			break;
		}
		return pt;
	}
	
	public void repaint(){
		searchingLabel.setVisible(logicControl.getLogic().isSearchRunning());
		mainFrame.repaint();
	}
	
	public RectangleInfoPanel getRectangleInfoPanel(){
		return (RectangleInfoPanel) rectangleInfoPanel;
	}

	private void actionCreate(){
		int number = Integer.valueOf(Gui.this.numberField.getText());
		double max = Integer.valueOf(Gui.this.maxField.getText());
		double min = Integer.valueOf(Gui.this.minField.getText());
		Gui.this.logicControl.create(readyProblemType(), readSearchAlgorithm(), number, min, max);
		Gui.this.mainFrame.repaint();
		updateInfoPanel();
	}
	
	public void setVisibleMapLayout(boolean visible){
		workspacePanel.setVisibleMapLayout(visible);
		repaint();
	}
	
	public void setVisibleMinimumSquare(boolean visible){
		workspacePanel.setVisibleMinimumSquare(visible);
		repaint();
	}
	
	public void updateInfoPanel(){
		RectangleMap rm = null;
		if (logicControl.getLogic().getFs() instanceof RuleBased){
			rm = ((RuleBased) logicControl.getLogic().getFs()).getRm();
		}
		else if (logicControl.getLogic().getFs() instanceof GeometryBased){
			rm = ((GeometryBased) logicControl.getLogic().getFs()).getRm();
		}
		else if (logicControl.getLogic().getFs() instanceof OverlapBased){
			rm = ((OverlapBased) logicControl.getLogic().getFs()).getRm();
		}
		((InfoPanel) infoPanel).update(rm);
	}

	public JLabel getSearchingLabel() {
		return searchingLabel;
	}
	
	public void updateTestSelected(){
		testFrame.setVisible(false);
		testResultFrame.setVisible(true);
	}

	public void updateResultFrame(double[] times, double[] results) {
		testResultFrame.update(times, results);
		repaint();
	}
	
	
	
	
}
