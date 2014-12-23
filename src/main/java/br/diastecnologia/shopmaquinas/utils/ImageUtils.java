package br.diastecnologia.shopmaquinas.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {

	public static void main(String[] args) throws IOException{
		String base = "C:\\Projetos\\diastecnologia\\Shop Maquinas\\design\\logos\\";
		creatingMiniimage(base, "anuncio2.png");
		applyOverlay(base, "anuncio2.png", false);
	}
	
	public static void creatingMiniimage(String basePath, String imagename ) throws IOException{
		File path = new File(basePath);
		File original = new File(path, imagename);
		File mini = new File(path, "mini-" + imagename);
		
		BufferedImage miniBackground = ImageIO.read(new File(path, "mini-background.png"));
		BufferedImage largeImage = ImageIO.read(original);
		
		int width = 0;
		int height = 0;
		
		if( ((double)(miniBackground.getWidth() / (double)miniBackground.getHeight())) <= ((double)(largeImage.getWidth() / (double)largeImage.getHeight()))){
			width = miniBackground.getWidth();
			height = (int)((width / (double)largeImage.getWidth()) * largeImage.getHeight()); 
		}else{
			height = miniBackground.getHeight();
			width = (int)((height / (double)largeImage.getHeight()) * largeImage.getWidth());
		}
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		img.createGraphics().drawImage(largeImage.getScaledInstance(width, height, Image.SCALE_SMOOTH),0,0,null);
		
		ImageIO.write(img, "jpg", mini);
		
		applyOverlay(basePath, "mini-" + imagename, true);
	}
	
	public static void applyOverlay(String basePath, String imagename, boolean mini) throws IOException{
		File path = new File(basePath);

		// load source images
		BufferedImage image = null;
		BufferedImage overlay = null;
		if( !mini ){
			image = ImageIO.read(new File(path, imagename));
			overlay = ImageIO.read(new File(path, "logo-pequeno.png"));
		}else{
			overlay = ImageIO.read(new File(path, imagename));
			image = ImageIO.read(new File(path, "mini-background.png"));
		}

		if( mini || (image.getWidth() / 4 > overlay.getWidth() && image.getHeight() / 4 > overlay.getHeight() )){
		
			// create the new image, canvas size is the max. of both image sizes
			int w = image.getWidth();
			int h = image.getHeight();
			
			BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	
			// paint both images, preserving the alpha channels
			Graphics g = combined.getGraphics();
			g.drawImage(image, 0, 0, null);
			
			int x = 20;
			int y = h - overlay.getHeight() - 20;
			
			if( mini ){
				x = (int)((image.getWidth() - overlay.getWidth()) / (double)2);
				y = (int)((image.getHeight() - overlay.getHeight()) / (double)2);
			}
			
			g.drawImage(overlay, x, y, null);
	
			// Save as new image
			ImageIO.write(combined, "PNG", new File(path, imagename));
		}
	}
	
	
}
