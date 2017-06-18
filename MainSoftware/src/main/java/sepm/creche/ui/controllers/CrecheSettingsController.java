package sepm.creche.ui.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.repositories.CrecheSettingsRepository;

/**
 * Controller Class for the DayScheduler. This Class provides methods for
 * loading, saving and removing TodaysKids entities.
 *
 * @author Daniel, Fabian
 */

@Component
@Scope("session")
public class CrecheSettingsController
{

	/**
	 * Basic Constructor for CrecheSettings.
	 */
	public CrecheSettingsController()
	{
	}

	public CrecheSettingsRepository getCrecheSettingsRepository()
	{
		return crecheSettingsRepository;
	}

	public void setCrecheSettingsRepository(CrecheSettingsRepository crecheSettingsRepository)
	{
		this.crecheSettingsRepository = crecheSettingsRepository;
	}

	@Autowired
	private CrecheSettingsRepository crecheSettingsRepository;

	private double lunchPrice;
	private int deadline;

	private String beginDropOffTime;
	private String endDropOffTime;

	private String beginPickUpTime;
	private String endPickUpTime;

	public double getLunchPrice()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getLunchPrice();
	}

	public void setLunchPrice(double lunchPrice)
	{
		this.lunchPrice = lunchPrice;
	}

	public int getDeadline()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getDeadline();
	}

	public void setDeadline(int deadline)
	{
		this.deadline = deadline;
	}

	public String getBeginDropOffTime()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getBeginDropOffTime();
	}

	public void setBeginDropOffTime(String beginDropOffTime)
	{
		this.beginDropOffTime = beginDropOffTime;
	}

	public String getEndDropOffTime()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getEndDropOffTime();
	}

	public void setEndDropOffTime(String endDropOffTime)
	{
		this.endDropOffTime = endDropOffTime;
	}

	public String getBeginPickUpTime()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getBeginPickUpTime();
	}

	public void setBeginPickUpTime(String beginPickUpTime)
	{
		this.beginPickUpTime = beginPickUpTime;
	}

	public String getEndPickUpTime()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getEndPickUpTime();
	}

	public void setEndPickUpTime(String endPickUpTime)
	{
		this.endPickUpTime = endPickUpTime;
	}

	public double getLunchPrice2()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getLunchPrice();
	}

	public int getDeadline2()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getDeadline();
	}

	public String getBeginDropOffTime2()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getBeginDropOffTime();
	}

	public String getEndDropOffTime2()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getEndDropOffTime();
	}

	public String getBeginPickUpTime2()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getBeginPickUpTime();
	}

	public String getEndPickUpTime2()
	{
		return crecheSettingsRepository.findFirstBySettingsId(1).getEndPickUpTime();
	}

	@SuppressWarnings("deprecation")
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void saveNewSettings() throws ParseException
	{

		if (lunchPrice < 0 || lunchPrice > 100)
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler bei den Kosten für das Mittagessen!", "Minimalbetrag ist 0. Maximalbetrag ist 100.");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		crecheSettingsRepository.findFirstBySettingsId(1).setLunchPrice(lunchPrice);

		if (deadline < 0 || deadline > 21)
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler beim Setzen des Stichtages!",
					"Minimum ist 0. Maximum ist 21.");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		crecheSettingsRepository.findFirstBySettingsId(1).setDeadline(deadline);

		/*
		 * beginDropOffTime 08:00 endDropOffTime 10:00 beginPickUpTime
		 * endPickUpTime
		 */

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date beginDropOffTimeDate = sdf.parse(beginDropOffTime);
		Date endDropOffTimeDate = sdf.parse(endDropOffTime);
		Date beginPickUpTimeDate = sdf.parse(beginPickUpTime);
		Date endPickUpTimeDate = sdf.parse(endPickUpTime);

		String beginDrop = beginDropOffTime.substring(0, 2);
		String beginDrop1 = beginDropOffTime.substring(3, 5);

		String endDrop = endDropOffTime.substring(0, 2);
		String endDrop1 = endDropOffTime.substring(3, 5);

		int beginDropO = Integer.parseInt(beginDrop);
		int beginDropO1 = Integer.parseInt(beginDrop1);

		int endDropO = Integer.parseInt(endDrop);
		int endDropO1 = Integer.parseInt(endDrop1);

		String beginPick = beginPickUpTime.substring(0, 2);
		String beginPick1 = beginPickUpTime.substring(3, 5);

		String endPick = endPickUpTime.substring(0, 2);
		String endPick1 = endPickUpTime.substring(3, 5);

		int beginPickU = Integer.parseInt(beginPick);
		int beginPickU1 = Integer.parseInt(beginPick1);

		int endPickU = Integer.parseInt(endPick);
		int endPickU1 = Integer.parseInt(endPick1);

		Date beginDropTime = new Date();
		beginDropTime.setHours(beginDropO);
		beginDropTime.setMinutes(beginDropO1);

		Date endDropTime = new Date();
		endDropTime.setHours(endDropO);
		endDropTime.setMinutes(endDropO1);

		Date beginPickTime = new Date();
		beginPickTime.setHours(beginPickU);
		beginPickTime.setMinutes(beginPickU1);

		Date endPickTime = new Date();
		endPickTime.setHours(endPickU);
		endPickTime.setMinutes(endPickU1);

		System.out.println("beginDropTime" + beginDropTime);
		System.out.println("endDropTime" + endDropTime);
		System.out.println("beginPickTime" + beginPickTime);
		System.out.println("endPickTime" + endPickTime);

		if (beginDropTime.after(endDropTime) || beginDropTime.after(beginPickTime) || beginPickTime.after(endPickTime)
				|| endDropTime.after(beginPickTime))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fehler beim Eintragen der Bring- und Abholzeiten!", "Bringzeit muss vor der Abholzeit sein.");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}

		crecheSettingsRepository.findFirstBySettingsId(1).setBeginDropOffTime(beginDropOffTime);
		crecheSettingsRepository.findFirstBySettingsId(1).setEndDropOffTime(endDropOffTime);
		crecheSettingsRepository.findFirstBySettingsId(1).setBeginPickUpTime(beginPickUpTime);
		crecheSettingsRepository.findFirstBySettingsId(1).setEndPickUpTime(endPickUpTime);

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg!",
				"Die Einstellungen wurden erfolgreich gespeichert.");
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
}