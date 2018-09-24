package bmPictureAnalysis;

import java.awt.*;

import javax.swing.*;

public class SymbolLearner extends JFrame implements IF_AnalyzerUI {
	
	/**
	 * 
	 */
	private static SymbolLearner instance = null;
	private static final long serialVersionUID = 8069219107269691496L;
	private JButton bScan;
	private JLabel lDetectedSymbol = new JLabel("Erkanntes Symbol:");
	private JLabel olDetectedSymbol;
	private JLabel lAvgX = new JLabel("Durchschnitt X des Symbols:");
	private JLabel olAvgX;
	private JLabel lAvgY = new JLabel("Durchschnitt Y des Symbols:");
	private JLabel olAvgY;
	private JLabel lRightSymbolDetected = new JLabel("Wurde das richtige Symbol erkannt?");
	private JButton correctSymbol;
	private JButton incorrectSymbol;
	private JPanel sourcePicturePanel;
	private JPanel foundPixelPanel;
	private JMenuBar menu;
	private JMenu menuDatei;
	private JMenuItem menuItemOeffnen;
	
	public static IF_AnalyzerUI getInstance(){
		if(instance == null){
			instance = new SymbolLearner();
			instance.init();
		}
		return instance;
	}
	
	@Override
	public JFrame getAnalyzerUI() {
		// TODO Auto-generated method stub
		return instance;
	}
	
	private void init(){
		menu = new JMenuBar();
		menuDatei = new JMenu("Datei");
		menu.add(menuDatei);
		menuItemOeffnen = new JMenuItem("Öffnen");
		menuDatei.add(menuItemOeffnen);
		
		this.setJMenuBar(menu);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		setSize((width/2), (height/2));
		setLocation((width/2 - width/4), (height/2 - height/4));
		
		JPanel rootPanel = new JPanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(width/2/4);
		JButton bScan = new JButton("Scan Picture");
		bScan.setEnabled(false);
		JPanel leftPanel = new JPanel(new GridBagLayout());
		leftPanel.setSize(width/2/4, height/2);
		GridBagConstraints constraints = new GridBagConstraints();
		
		JSplitPane picturePanel = new JSplitPane();
		picturePanel.setSize(width/2-width/2/4, height/2);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		leftPanel.add(bScan, constraints);
		sourcePicturePanel = new JPanel();
		sourcePicturePanel.setSize((width/2-width/2/4)/2, height/2);
		sourcePicturePanel.setBackground(Color.WHITE);
		foundPixelPanel = new JPanel();
		foundPixelPanel.setSize((width/2-width/2/4)/2, height/2);
		foundPixelPanel.setBackground(Color.lightGray);
		picturePanel.setLeftComponent(sourcePicturePanel);
		picturePanel.setRightComponent(foundPixelPanel);
		
		splitPane.add(leftPanel);
		splitPane.add(picturePanel);
		rootPanel.add(splitPane);
		add(rootPanel);
		
		
		
		
	}

}
