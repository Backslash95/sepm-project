package sepm.creche.ui.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.GalleryPic;
import sepm.creche.services.GalleryPicService;

@Component
@Scope("session")
public class GalleryView
{
	private GalleryPic galleryPic;

	private List<String> images;

	String filter;

	@Autowired
	GalleryPicService galleryPicService;

	public String getFilter()
	{
		return filter;
	}

	public void setFilter(String filter)
	{
		this.filter = filter;
	}

	private Scanner scanner;

	public GalleryView()
	{
		setImages(new ArrayList<String>());
		filter = "";
	}

	public void load(String picID)
	{
		Integer a = new Integer(0);
		scanner = new Scanner(picID);
		while (scanner.hasNext())
		{
			if (scanner.hasNextBigInteger())
			{
				a = scanner.nextInt();
			}
		}
		setGalleryPic(galleryPicService.loadProfilePicByID(a.intValue()));
	}

	public GalleryPic getGalleryPic()
	{
		return galleryPic;
	}

	public void setGalleryPic(GalleryPic galleryPic)
	{
		this.galleryPic = galleryPic;
	}

	public List<String> getImages()
	{
		images.clear();

		for (GalleryPic gp : galleryPicService.loadAllPics())
		{

			if (filter.isEmpty()
					|| Pattern.compile(Pattern.quote(filter), Pattern.CASE_INSENSITIVE)
							.matcher(gp.getPictureReference()).find()
					|| (gp.getDescription() != null && Pattern.compile(Pattern.quote(filter), Pattern.CASE_INSENSITIVE)
							.matcher(gp.getDescription()).find()))
				images.add(gp.getPictureReference());
		}
		return images;
	}

	public void setImages(List<String> images)
	{
		this.images = images;
	}

}
