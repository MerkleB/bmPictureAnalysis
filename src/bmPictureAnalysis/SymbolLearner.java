package bmPictureAnalysis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

import bmDataTypes.EN_MusicalSymbolName;

public class SymbolLearner extends JFrame implements IF_AnalyzerUI {
	
	/**
	 * 
	 */
	private static SymbolLearner instance = null;
	private static final long serialVersionUID = 8069219107269691496L;
	private JButton btnAnalyse;
	//private JComboBox<EN_MusicalSymbolName> cbSymbolName;
	private JComboBox cbSymbolName;
	private JMenuBar menuBar;
	private JMenu mnDatei;
	private JButton btnSaveNewSymbol;
	private JMenuItem mntmoeffnen;
	private JLabel lbldAvgY;
	private JLabel lbldAvgX;
	private JLabel lbldFoundSymbol;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private int windowWidth = 0;
	private int windowHeight = 0;
	private JPanel sourcePicture;
	private JPanel resultPicture;
	private JPanel panel;
	private JPanel controlPanel;
	private JPanel picturePanel;
	
	public SymbolLearner() {
		this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                instance.windowWidth = instance.getRootPane().getWidth();
                instance.windowHeight = instance.getRootPane().getHeight();
                instance.resizeComponents();
                System.out.println("componentResized: Width: " + instance.windowWidth +" Height: "+ instance.windowHeight);
            }
        });
		
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		this.screenWidth = gd.getDisplayMode().getWidth();
		this.screenHeight = gd.getDisplayMode().getHeight();
		windowWidth = screenWidth/2;
		windowHeight = screenHeight/2;
		setSize(windowWidth, windowHeight);
		setLocation(windowWidth-screenWidth/4, windowHeight-screenHeight/4);
		this.setMinimumSize(new Dimension(windowWidth, windowHeight));
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, windowWidth, windowHeight);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		controlPanel = new JPanel();
		controlPanel.setBounds(0, 0, windowWidth-(2*windowWidth/3 + 20), windowHeight);
		panel.add(controlPanel);
		GridBagLayout gbl_controlPanel = new GridBagLayout();
		gbl_controlPanel.columnWidths = new int[]{86, 3, 1, 1, 3, 1, 0};
		gbl_controlPanel.rowHeights = new int[]{30, 0, 14, 14, 14, 0, 0, 0, 0, 0};
		gbl_controlPanel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_controlPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		controlPanel.setLayout(gbl_controlPanel);
		
		btnAnalyse = new JButton("Analysiere");
		GridBagConstraints gbc_btnAnalyse = new GridBagConstraints();
		btnAnalyse.setEnabled(false);
		gbc_btnAnalyse.anchor = GridBagConstraints.WEST;
		gbc_btnAnalyse.insets = new Insets(0, 0, 5, 5);
		gbc_btnAnalyse.gridwidth = 2;
		gbc_btnAnalyse.gridx = 0;
		gbc_btnAnalyse.gridy = 0;
		controlPanel.add(btnAnalyse, gbc_btnAnalyse);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0, 0, 0));
		separator.setBackground(new Color(0, 0, 0));
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 5;
		gbc_separator.anchor = GridBagConstraints.WEST;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		controlPanel.add(separator, gbc_separator);
		
		JLabel lbltFoundSymbol = new JLabel("Gefundenes Symbol:");
		GridBagConstraints gbc_lbltFoundSymbol = new GridBagConstraints();
		gbc_lbltFoundSymbol.anchor = GridBagConstraints.WEST;
		gbc_lbltFoundSymbol.insets = new Insets(0, 0, 0, 0);
		gbc_lbltFoundSymbol.gridwidth = 5;
		gbc_lbltFoundSymbol.gridx = 0;
		gbc_lbltFoundSymbol.gridy = 2;
		controlPanel.add(lbltFoundSymbol, gbc_lbltFoundSymbol);
		
		lbldFoundSymbol = new JLabel("");
		GridBagConstraints gbc_lbldFoundSymbol = new GridBagConstraints();
		gbc_lbldFoundSymbol.anchor = GridBagConstraints.WEST;
		gbc_lbldFoundSymbol.insets = new Insets(0, 0, 0, 0);
		gbc_lbldFoundSymbol.gridx = 1;
		gbc_lbldFoundSymbol.gridy = 2;
		controlPanel.add(lbldFoundSymbol, gbc_lbldFoundSymbol);
		
		JLabel lbltAvgX = new JLabel("Durchschnitt X:");
		lbltAvgX.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lbltAvgX = new GridBagConstraints();
		gbc_lbltAvgX.anchor = GridBagConstraints.WEST;
		gbc_lbltAvgX.insets = new Insets(0, 0, 0, 0);
		gbc_lbltAvgX.gridx = 0;
		gbc_lbltAvgX.gridy = 3;
		controlPanel.add(lbltAvgX, gbc_lbltAvgX);
		
		lbldAvgX = new JLabel("");
		GridBagConstraints gbc_lbldAvgX = new GridBagConstraints();
		gbc_lbldAvgX.anchor = GridBagConstraints.WEST;
		gbc_lbldAvgX.insets = new Insets(0, 0, 5, 5);
		gbc_lbldAvgX.gridx = 2;
		gbc_lbldAvgX.gridy = 3;
		controlPanel.add(lbldAvgX, gbc_lbldAvgX);
		
		JLabel lbltAvgY = new JLabel("Durchschnitt Y:");
		GridBagConstraints gbc_lbltAvgY = new GridBagConstraints();
		gbc_lbltAvgY.anchor = GridBagConstraints.WEST;
		gbc_lbltAvgY.insets = new Insets(0, 0, 0, 0);
		gbc_lbltAvgY.gridx = 0;
		gbc_lbltAvgY.gridy = 4;
		controlPanel.add(lbltAvgY, gbc_lbltAvgY);
		
		lbldAvgY = new JLabel("");
		GridBagConstraints gbc_lbldAvgY = new GridBagConstraints();
		gbc_lbldAvgY.anchor = GridBagConstraints.WEST;
		gbc_lbldAvgY.insets = new Insets(0, 0, 5, 5);
		gbc_lbldAvgY.gridx = 1;
		gbc_lbldAvgY.gridy = 4;
		controlPanel.add(lbldAvgY, gbc_lbldAvgY);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(0, 0, 0));
		separator_1.setBackground(new Color(0, 0, 0));
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 4;
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 5;
		controlPanel.add(separator_1, gbc_separator_1);
		
		JLabel lblCorrectSymbolName = new JLabel("Richtiger Symbolname");
		GridBagConstraints gbc_lblCorrectSymbolName = new GridBagConstraints();
		gbc_lblCorrectSymbolName.anchor = GridBagConstraints.WEST;
		gbc_lblCorrectSymbolName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCorrectSymbolName.gridx = 0;
		gbc_lblCorrectSymbolName.gridy = 6;
		controlPanel.add(lblCorrectSymbolName, gbc_lblCorrectSymbolName);
		
		//cbSymbolName = new JComboBox<EN_MusicalSymbolName>(EN_MusicalSymbolName.values());
		cbSymbolName = new JComboBox(EN_MusicalSymbolName.values());
		cbSymbolName.setEnabled(false);
		GridBagConstraints gbc_cbSymbolName = new GridBagConstraints();
		gbc_cbSymbolName.insets = new Insets(0, 0, 5, 5);
		gbc_cbSymbolName.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbSymbolName.gridx = 0;
		gbc_cbSymbolName.gridy = 7;
		cbSymbolName.setEditable(false);
		controlPanel.add(cbSymbolName, gbc_cbSymbolName);
		
		btnSaveNewSymbol = new JButton("Speichern");
		btnSaveNewSymbol.setEnabled(false);
		btnSaveNewSymbol.setToolTipText("Neues Symbol Speichern");
		GridBagConstraints gbc_btnSaveNewSymbol = new GridBagConstraints();
		gbc_btnSaveNewSymbol.anchor = GridBagConstraints.WEST;
		gbc_btnSaveNewSymbol.insets = new Insets(0, 0, 0, 5);
		gbc_btnSaveNewSymbol.gridx = 0;
		gbc_btnSaveNewSymbol.gridy = 8;
		controlPanel.add(btnSaveNewSymbol, gbc_btnSaveNewSymbol);
		
		int picturePanelWidth = windowWidth-windowWidth/3;
		picturePanel = new JPanel();
		picturePanel.setBounds(windowWidth/3 - 20, 0, picturePanelWidth, windowHeight);
		panel.add(picturePanel);
		picturePanel.setLayout(null);
		
		sourcePicture = new JPanel();
		sourcePicture.setBackground(new Color(255, 255, 255));
		sourcePicture.setBounds(0, 0, picturePanelWidth/2 - 1, windowHeight);
		picturePanel.add(sourcePicture);
		
		resultPicture = new JPanel();
		resultPicture.setBackground(new Color(255, 255, 255));
		resultPicture.setBounds(picturePanelWidth/2 + 1, 0, picturePanelWidth/2-1, windowHeight);
		picturePanel.add(resultPicture);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		mntmoeffnen = new JMenuItem("\u00D6ffnen");
		mntmoeffnen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Create a file chooser
				final JFileChooser fc = new JFileChooser();
				//In response to a button click:
				int returnVal = fc.showOpenDialog(mntmoeffnen);				
			}
		});
		mnDatei.add(mntmoeffnen);
	}
	
	public static IF_AnalyzerUI getInstance(){
		if(instance == null){
			instance = new SymbolLearner();
		}
		return instance;
	}
	
	@Override
	public JFrame getAnalyzerUI() {
		// TODO Auto-generated method stub
		return instance;
	}
	
	private void resizeComponents(){
		panel.setBounds(0, 0, windowWidth, windowHeight);
		controlPanel.setBounds(0, 0, windowWidth-(2*windowWidth/3 + 20), windowHeight);
		
		int picturePanelWidth = windowWidth-windowWidth/3;
		picturePanel.setBounds(windowWidth/3 - 20, 0, picturePanelWidth, windowHeight);
		sourcePicture.setBounds(0, 0, picturePanelWidth/2 - 1, windowHeight);
		resultPicture.setBounds(picturePanelWidth/2 + 1, 0, picturePanelWidth/2-1, windowHeight);
		
		panel.repaint();
	}
	
}
