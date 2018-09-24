package unitTests;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import org.junit.Test;

import bmPictureAnalysis.EN_direction;
import bmPictureAnalysis.IF_PixelAnalyzer;
import bmPictureAnalysis.PixelAnalyzer;

public class PixelAnalyzerTest {

	@Test
	public void testGetInstance() {
		IF_PixelAnalyzer pixelAnalyzer = PixelAnalyzer.getInstance();
		
		if(pixelAnalyzer == null){
			fail("No instance was retrieved (calculator)");
		}
		
		IF_PixelAnalyzer pixelAnalyzer2 = PixelAnalyzer.getInstance();
		
		if(pixelAnalyzer2 == null){
			fail("No instance was retrieved (calculator2)");
		}
		
		if(pixelAnalyzer != pixelAnalyzer2){
			fail("Two instances where retrieved but singleton should retrieve only one instance");
		}
	}

	@Test
	public void testPixelColorChanges() {
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File("./src/unitTests/testResources/test3x3_blackPixelInTheMiddle.jpg"));
		}catch(IOException e){
			fail("Reading of test image failed: "+e.getMessage());
		}
		
		IF_PixelAnalyzer pixelAnalyzer = PixelAnalyzer.getInstance();
		if(pixelAnalyzer.pixelColorChanges(img, 1, 1, EN_direction.East) == false){
			fail("A change of color should have been detected.");
		}
		
	}

	@Test
	public void testPixelColorIsEqual() {
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File("./src/unitTests/testResources/test7x11_simpleFive_0-76471_0-4118.jpg"));
		}catch(IOException e){
			fail("Reading of test image failed: "+e.getMessage());
		}
		IF_PixelAnalyzer pixlAnalyzer = PixelAnalyzer.getInstance();
		if(pixlAnalyzer.pixelColorIsEqual(img, 1, 1, EN_direction.East));
	}

}
