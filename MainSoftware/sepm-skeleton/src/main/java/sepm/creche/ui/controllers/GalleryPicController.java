package sepm.creche.ui.controllers;

import java.io.IOException;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.GalleryPic;
import sepm.creche.services.GalleryPicService;
import sepm.creche.utils.FileUploader;

@Component
@Scope("session")
public class GalleryPicController
{
	@Autowired
	GalleryPicService galleryPicService;

	@Autowired
	private FileUploader fileUploader;

	private String desc;

	public void handleFileUpload(FileUploadEvent event) throws IOException
	{
		String newPic = fileUploader.handleFileUpload(event);
		GalleryPic gp = new GalleryPic();
		gp.setPictureReference(newPic);
		gp.setDescription(desc);
		desc = "";
		galleryPicService.saveGalleryPic(gp);
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

}
