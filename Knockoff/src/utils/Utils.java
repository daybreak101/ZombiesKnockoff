package utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Utils {

	
	public static String loadFileAsString(String path) {
		StringBuilder builder = new StringBuilder();
		
		try {
			InputStream sr = Utils.class.getResourceAsStream(path);
			InputStreamReader is = new InputStreamReader(sr);
			BufferedReader br = new BufferedReader(is);
			String line;
			while((line = br.readLine()) != null)
				builder.append(line + "\n");
			
			br.close();
			is.close();
			sr.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static double parseDouble(String number) {
		try {
			return Double.parseDouble(number);
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static float getEuclideanDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	
	/**
	 * Converts an image to a binary one based on given threshold
	 * @param image the image to convert. Remains untouched.
	 * @param threshold the threshold in [0,255]
	 * @return a new BufferedImage instance of TYPE_BYTE_GRAY with only 0'S and 255's
	 */
	public static BufferedImage thresholdImage(BufferedImage image, int threshold) {
	    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	    result.getGraphics().drawImage(image, 0, 0, null);
	    WritableRaster raster = result.getRaster();
	    int[] pixels = new int[image.getWidth()];
	    
	    
	    for (int y = 0; y < image.getHeight(); y++) {
	        raster.getPixels(0, y, image.getWidth(), 1, pixels);
	        for (int i = 0; i < pixels.length; i++) {
	            if (pixels[i] < threshold) {
	            	pixels[i] = 0;
	            }
	            else {
	            	pixels[i] = 255;
	            }
	        }
	        raster.setPixels(0, y, image.getWidth(), 1, pixels);
	    }
	    return result;
	}
	
	public static BufferedImage blackAndWhite(BufferedImage image) {
	    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(
                image.getColorModel().getColorSpace(),
                result.getColorModel().getColorSpace(), null);
            op.filter(image, result);
		
		
		return result;
	}
	
	public static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    
	    // Draw the String
	    g.drawString(text, x, y);
	}

}
