package br.diastecnologia.shopmaquinas.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Property;
import br.com.caelum.vraptor.observer.upload.UploadedFile;

public class FileUtils {

	@Inject
	@Property("currentForder")
	private String currentForder;
	
	@Inject
	@Property("file.defaultFolder")
	private String defaultFolder;
	
	@Inject
	@Property("file.webDefaultFolder")
	private String webDefaultFolder;
	
	 
	public String saveFile( UploadedFile file ) throws IOException{
		
		int now = LocalDateTime.now().getSecond() + LocalDateTime.now().getNano();
		
		String newFileName = now + "." + file.getContentType().split("/")[1];
		String newWebFileName = webDefaultFolder + "/" + now + "." + file.getContentType().split("/")[1];
		
		Path newFilePath = Paths.get(currentForder, defaultFolder, newFileName);
		Files.copy(file.getFile(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
		
		ImageUtils.creatingMiniimage(currentForder +"\\" + defaultFolder, newFileName);
		ImageUtils.applyOverlay(currentForder +"\\" + defaultFolder, newFileName, false);
		
		return newWebFileName;
	}
	
}
