package bmPictureAnalysis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import Files.OpenFileFilter;
import bmDataTypes.DT_MusicalSymbol;
import bmDataTypes.DT_Point;
import bmDataTypes.DT_PointCloud;
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
	private JLabel lbldActualAvgX;
	private JLabel lbldActualAvgY;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private int windowWidth = 0;
	private int windowHeight = 0;
	private JPanel sourcePicture;
	private JPanel resultPicture;
	private JPanel panel;
	private JPanel controlPanel;
	private JPanel picturePanel;
	private BufferedImage readImage;
	private IF_SymbolLearnerController controller;
	
	public SymbolLearner() {
		this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                instance.windowWidth = instance.getRootPane().getWidth();
                instance.windowHeight = instance.getRootPane().getHeight();
                instance.resizeComponents();
                //System.out.println("componentResized: Width: " + instance.windowWidth +" Height: "+ instance.windowHeight);
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
		
		controlPanel.setLayout(gbl_controlPanel);
		
		btnAnalyse = new JButton("Analysiere");
		GridBagConstraints gbc_btnAnalyse = new GridBagConstraints();
		btnAnalyse.setEnabled(false);
		gbc_btnAnalyse.anchor = GridBagConstraints.WEST;
		gbc_btnAnalyse.insets = new Insets(0, 0, 5, 5);
		gbc_btnAnalyse.gridwidth = 4;
		gbc_btnAnalyse.gridx = 0;
		gbc_btnAnalyse.gridy = 0;
		controlPanel.add(btnAnalyse, gbc_btnAnalyse);
		btnAnalyse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				analyzeImage();
				instance.panel.repaint();
			}
		});
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0, 0, 0));
		separator.setBackground(new Color(0, 0, 0));
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 7;
		gbc_separator.anchor = GridBagConstraints.WEST;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		controlPanel.add(separator, gbc_separator);
		
		JLabel lbltFoundSymbol = new JLabel("Gefundenes Symbol:");
		GridBagConstraints gbc_lbltFoundSymbol = new GridBagConstraints();
		gbc_lbltFoundSymbol.anchor = GridBagConstraints.WEST;
		gbc_lbltFoundSymbol.insets = new Insets(0, 0, 5, 5);
		gbc_lbltFoundSymbol.gridwidth = 7;
		gbc_lbltFoundSymbol.gridx = 0;
		gbc_lbltFoundSymbol.gridy = 3;
		controlPanel.add(lbltFoundSymbol, gbc_lbltFoundSymbol);
		
		lbldFoundSymbol = new JLabel("");
		GridBagConstraints gbc_lbldFoundSymbol = new GridBagConstraints();
		gbc_lbldFoundSymbol.anchor = GridBagConstraints.WEST;
		gbc_lbldFoundSymbol.insets = new Insets(0, 0, 5, 5);
		gbc_lbldFoundSymbol.gridx = 1;
		gbc_lbldFoundSymbol.gridy = 3;
		controlPanel.add(lbldFoundSymbol, gbc_lbldFoundSymbol);
		
		JLabel lbltAvgX = new JLabel("Durchschnitt X:");
		lbltAvgX.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lbltAvgX = new GridBagConstraints();
		gbc_lbltAvgX.anchor = GridBagConstraints.WEST;
		gbc_lbltAvgX.insets = new Insets(0, 0, 5, 5);
		gbc_lbltAvgX.gridx = 0;
		gbc_lbltAvgX.gridy = 4;
		controlPanel.add(lbltAvgX, gbc_lbltAvgX);
		
		lbldAvgX = new JLabel("");
		GridBagConstraints gbc_lbldAvgX = new GridBagConstraints();
		gbc_lbldAvgX.anchor = GridBagConstraints.WEST;
		gbc_lbldAvgX.insets = new Insets(0, 0, 5, 5);
		gbc_lbldAvgX.gridx = 1;
		gbc_lbldAvgX.gridy = 4;
		controlPanel.add(lbldAvgX, gbc_lbldAvgX);
		
		JLabel lbltAvgY = new JLabel("Durchschnitt Y:");
		GridBagConstraints gbc_lbltAvgY = new GridBagConstraints();
		gbc_lbltAvgY.anchor = GridBagConstraints.WEST;
		gbc_lbltAvgY.insets = new Insets(0, 0, 5, 5);
		gbc_lbltAvgY.gridx = 0;
		gbc_lbltAvgY.gridy = 5;
		controlPanel.add(lbltAvgY, gbc_lbltAvgY);
		
		lbldAvgY = new JLabel("");
		GridBagConstraints gbc_lbldAvgY = new GridBagConstraints();
		gbc_lbldAvgY.anchor = GridBagConstraints.WEST;
		gbc_lbldAvgY.insets = new Insets(0, 0, 5, 5);
		gbc_lbldAvgY.gridx = 1;
		gbc_lbldAvgY.gridy = 5;
		controlPanel.add(lbldAvgY, gbc_lbldAvgY);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(0, 0, 0));
		separator_1.setBackground(new Color(0, 0, 0));
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 6;
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 6;
		controlPanel.add(separator_1, gbc_separator_1);
		
		JLabel lblCorrectSymbolName = new JLabel("Richtiger Symbolname");
		GridBagConstraints gbc_lblCorrectSymbolName = new GridBagConstraints();
		gbc_lblCorrectSymbolName.anchor = GridBagConstraints.WEST;
		gbc_lblCorrectSymbolName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCorrectSymbolName.gridx = 0;
		gbc_lblCorrectSymbolName.gridy = 7;
		controlPanel.add(lblCorrectSymbolName, gbc_lblCorrectSymbolName);
		
		//cbSymbolName = new JComboBox<EN_MusicalSymbolName>(EN_MusicalSymbolName.values());
		cbSymbolName = new JComboBox(EN_MusicalSymbolName.values());
		cbSymbolName.setEnabled(false);
		GridBagConstraints gbc_cbSymbolName = new GridBagConstraints();
		gbc_cbSymbolName.insets = new Insets(0, 0, 5, 5);
		gbc_cbSymbolName.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbSymbolName.gridx = 0;
		gbc_cbSymbolName.gridy = 8;
		cbSymbolName.setEditable(false);
		controlPanel.add(cbSymbolName, gbc_cbSymbolName);
		
		JLabel lblActualAverageX = new JLabel("Richtiger Durchschnitt X:");
		GridBagConstraints gbc_lblActualAverageX = new GridBagConstraints();
		gbc_lblActualAverageX.anchor = GridBagConstraints.WEST;
		gbc_lblActualAverageX.insets = new Insets(0, 0, 5, 5);
		gbc_lblActualAverageX.gridx = 0;
		gbc_lblActualAverageX.gridy = 9;
		controlPanel.add(lblActualAverageX, gbc_lblActualAverageX);
		
		lbldActualAvgX = new JLabel("");
		GridBagConstraints gbc_lbldActualAvgX = new GridBagConstraints();
		gbc_lbldActualAvgX.anchor = GridBagConstraints.WEST;
		gbc_lbldActualAvgX.insets = new Insets(0, 0, 5, 5);
		gbc_lbldActualAvgX.gridx = 1;
		gbc_lbldActualAvgX.gridy = 9;
		controlPanel.add(lbldActualAvgX, gbc_lbldActualAvgX);
		
		JLabel lblActualAverageY = new JLabel("Richtiger Durchschnitt Y:");
		GridBagConstraints gbc_lblActualAverageY = new GridBagConstraints();
		gbc_lblActualAverageY.anchor = GridBagConstraints.WEST;
		gbc_lblActualAverageY.insets = new Insets(0, 0, 5, 5);
		gbc_lblActualAverageY.gridx = 0;
		gbc_lblActualAverageY.gridy = 10;
		controlPanel.add(lblActualAverageY, gbc_lblActualAverageY);
		
		lbldActualAvgY = new JLabel("");
		GridBagConstraints gbc_lbldActualAvgY = new GridBagConstraints();
		gbc_lbldActualAvgY.anchor = GridBagConstraints.WEST;
		gbc_lbldActualAvgY.insets = new Insets(0, 0, 5, 5);
		gbc_lbldActualAvgY.gridx = 1;
		gbc_lbldActualAvgY.gridy = 10;
		controlPanel.add(lbldActualAvgY, gbc_lbldActualAvgY);
		
		btnSaveNewSymbol = new JButton("Speichern");
		btnSaveNewSymbol.setEnabled(false);
		btnSaveNewSymbol.setToolTipText("Neues Symbol Speichern");
		GridBagConstraints gbc_btnSaveNewSymbol = new GridBagConstraints();
		gbc_btnSaveNewSymbol.anchor = GridBagConstraints.WEST;
		gbc_btnSaveNewSymbol.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveNewSymbol.gridx = 0;
		gbc_btnSaveNewSymbol.gridy = 12;
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
				instance.openFileChooser();
				instance.panel.repaint();
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
	
	private void openFileChooser(){
		File choosenFile;
		//Create a file chooser
		final JFileChooser chooser = new JFileChooser();
		//In response to a button click:
		chooser.addChoosableFileFilter(new OpenFileFilter("jpeg", "Picture in JPEG format"));
		chooser.addChoosableFileFilter(new OpenFileFilter("jpg", "Picture in JPEG format"));
		chooser.addChoosableFileFilter(new OpenFileFilter("png", "Picture in PNG format"));
		chooser.addChoosableFileFilter(new OpenFileFilter("svg", "Scalable Vector Graphic"));
		int returnVal = chooser.showOpenDialog(mntmoeffnen);
		
		if(returnVal == JFileChooser.APPROVE_OPTION){
			choosenFile = chooser.getSelectedFile();
			System.out.println(choosenFile.getName());
			try {
				readImage = ImageIO.read(choosenFile);
				btnAnalyse.setEnabled(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(instance, "Die gewählte Datei konnte nicht gefunden werden");
			}
			drawReadImage();
		}
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
	
	private void analyzeImage(){
		if(controller == null){
			controller = SymbolLearnerController.getInstance();
		}
		
		DT_MusicalSymbol foundSymbol = null;
		
		try {
			foundSymbol = controller.analyzeImage(readImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(instance, "Die Liste der Symbole konnte nicht geöffnet werden.");
			e.printStackTrace();
		}
		
		DT_PointCloud foundPoints = controller.getFoundPoints();
		drawFoundPoints(foundPoints);
		
		if(foundSymbol != null){
			lbldFoundSymbol.setText(foundSymbol.getName().name());
			lbldAvgX.setText(Integer.toString(foundSymbol.getPosition()[0]));
			lbldAvgY.setText(Integer.toString(foundSymbol.getPosition()[1]));
			
			if(foundSymbol.getName().compareTo(EN_MusicalSymbolName.unknown) == 0){
				btnAnalyse.setEnabled(false);
				btnSaveNewSymbol.setEnabled(true);
				cbSymbolName.setEnabled(true);
				lbldActualAvgX.setText(Double.toString(controller.getActualAvgX()));
				lbldActualAvgY.setText(Double.toString(controller.getActualAvgY()));
			}
		}
		
		
		
	}
	
	private void drawReadImage(){
		Graphics2D graphic;
		
		graphic = (Graphics2D)sourcePicture.getGraphics();
		
		
		int widthReadImg = readImage.getWidth();
		int heightReadImg = readImage.getHeight();
		int widthSourcePic = sourcePicture.getWidth();
		int heightSourcePic = sourcePicture.getHeight();
		
		boolean resized = resizeIfNecessary(widthSourcePic, heightSourcePic, widthReadImg, heightReadImg);
		
		int x, y;
		if(resized){
			x = 0;
			y = 0;
		}else{
			int[] xy = calcPicturePosition(widthSourcePic, heightSourcePic, widthReadImg, heightReadImg);
			x = xy[0];
			y = xy[1];
		}
		
		graphic.drawImage(readImage, x, y, null);
	}
	
	private int[] calcPicturePosition(int widthPane, int heightPane, int widthImg, int heightImg){
		int[] resultArray = new int[2];
		
		int middlePaneX = widthPane / 2;
		int middlePaneY = heightPane / 2;
		int middleImgX = widthImg / 2;
		int middleImgY = heightImg / 2;
		
		resultArray[0] = middlePaneX - middleImgX; //x-position
		resultArray[1] = middlePaneY - middleImgY; //y-position 
		
		return resultArray;
	}
	
	private void drawFoundPoints(DT_PointCloud foundPoints){
		int widthFoundImg = foundPoints.getWidth();
		int heightFoundImg = foundPoints.getHeight();
		int widthResultPane = resultPicture.getWidth();
		int heightResultPane = resultPicture.getHeight();
		
		boolean resized = resizeIfNecessary(widthFoundImg, heightFoundImg, widthResultPane, heightResultPane);
		
		int x,y;
		int minX = foundPoints.getMinX();
		int minY = foundPoints.getMinY();
		
		if(resized){
			x = 0;
			y = 0;
		}else{
			int[] xy = calcPicturePosition(widthResultPane, heightResultPane, widthFoundImg, heightFoundImg);
			x = xy[0];
			y = xy[1];
		}
		
		Graphics2D graphic = (Graphics2D)resultPicture.getGraphics();		
		
		for(int i=0; i<foundPoints.getListOfPoints().size(); i++){
			DT_Point currentPoint = foundPoints.getListOfPoints().get(i);
			int[] drawPoint = mapPointsToDrawPosition(minX, minY, x, y, currentPoint.getX(), currentPoint.getY());
			graphic.setColor(new Color(0, 0, 0));
			graphic.drawLine(drawPoint[0], drawPoint[1], drawPoint[0], drawPoint[1]);
		}
	}
	
	private boolean resizeIfNecessary(int widthPanel, int heightPanel, int widthImg, int heightImg){
		boolean resized = false;		

		//picturePanelWidth = windowWidth-windowWidth/3 
		// => factor * widthPanel = factor * ( windowWidth-windowWidth/3 ) = widthReadImg
		// widthImg = factor * windowWidth - factor * windowWidth/3 |*3
		// 3 * widthImg = 3 * factor * windowWidth - factor * windowWidth
		// 3 * widthImg = 2 * factor * windowWidth / 2*factor
		// (3 * widthImg) / (2 * factor) = windowWith 
		if(widthImg > widthPanel){
			float factor = widthImg / widthPanel;
			int newWindowWidth = (int) ((3 * widthImg) / (2*factor)); 
			instance.windowWidth = newWindowWidth;
			resized = true;
		}
		
		//picturePanelHeight = windowHeight
		if(heightImg > heightPanel){
			instance.windowHeight = heightImg;
			resized = true;
		}
		instance.getRootPane().setBounds(instance.getRootPane().getX(), instance.getRootPane().getY(), windowWidth, windowHeight);
		instance.resizeComponents();
		
		return resized;
	}
	
	private int[] mapPointsToDrawPosition(int sourceRootX, int sourceRootY, int targetRootX, int targetRootY, int sourcePointX, int sourcePointY){
		int[] targetPoint = new int[2];
		
		int differenceOfSourceRootXandTargetRootX = sourceRootX - targetRootX;
		int differenceOfSourceRootYandTargetRootY = sourceRootY - targetRootY;
		
		targetPoint[0] = sourcePointX - differenceOfSourceRootXandTargetRootX;
		targetPoint[1] = sourcePointY - differenceOfSourceRootYandTargetRootY;
		
		return targetPoint;
	}
	
}
