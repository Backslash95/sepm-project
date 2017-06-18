package sepm.creche.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class FileUploader
{

	private UploadedFile uploadedFile;

	/**
	 * This method handles the fileuploader for the photo.
	 * 
	 * @param event
	 *            an event to get the uploaded file from
	 * @throws IOException
	 * 
	 */
	public FileUploader()
	{
		uploadedFile = new DefaultUploadedFile();
	}

	public String handleFileUpload(FileUploadEvent event) throws IOException
	{
		uploadedFile = event.getFile();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

		String tempFileName = uploadedFile.getFileName();
		String filenameWithoutType[] = tempFileName.split("\\.");

		Path folder = Paths.get(ec.getRealPath("/"));
		Path file = Files.createTempFile(folder, filenameWithoutType[0] + "_", ".jpg");
		InputStream input = uploadedFile.getInputstream();
		Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
		System.out.println("Uploaded file successfully saved in " + file);
		return file.getFileName().toString();
	}

	// getters and setters
	public UploadedFile getUploadedFile()
	{
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile)
	{
		this.uploadedFile = uploadedFile;
	}

}
