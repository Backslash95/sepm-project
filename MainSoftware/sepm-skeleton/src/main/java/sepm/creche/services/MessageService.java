package sepm.creche.services;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.Message;
import sepm.creche.repositories.MessageRepository;
import sepm.creche.ui.beans.PickListViewUser;
import sepm.creche.ui.beans.SessionInfoBean;

/**
 * Handles Messages.
 * 
 * @author ASPIR
 *
 */

@Component
@Scope("session")
public class MessageService
{
	public MessageService()
	{
		dialogEnabled = false;
		msg = "";
		msgIDtoDisplay = -1;
		disableFrontEnd = false;
	}

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private SessionInfoBean sessionInfoBean;

	@Autowired
	private PickListViewUser pickListViewUser;

	private String msg;

	private int msgIDtoDisplay;

	boolean disableFrontEnd;

	public boolean isDisableFrontEnd()
	{
		return disableFrontEnd;
	}

	public void setDisableFrontEnd(boolean disableFrontEnd)
	{
		this.disableFrontEnd = disableFrontEnd;
	}

	public int getMsgIDtoDisplay()
	{

		return msgIDtoDisplay;
	}

	public void setMsgIDtoDisplay(int msgIDtoDisplay)
	{
		this.msgIDtoDisplay = msgIDtoDisplay;
	}

	public void selectMsg(int msgIDtoDisplay)
	{
		Message nmsg = messageRepository.findByMsgID(msgIDtoDisplay);
		nmsg.setAlreadyRead(true);
		messageRepository.save(nmsg);
		setMsgIDtoDisplay(msgIDtoDisplay);

	}

	public Message getMsgToDisplay()
	{
		return messageRepository.findByMsgID(msgIDtoDisplay);

	}

	private String selectedUser;

	private boolean dialogEnabled;

	public void buttonClicked(String user)
	{
		dialogEnabled = true;
		selectedUser = user;
	}

	public void addMessage(String summary)
	{
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public boolean isDialogEnabled()
	{
		return dialogEnabled;
	}

	public void setDialogEnabled(boolean dialogEnabled)
	{
		this.dialogEnabled = dialogEnabled;
	}

	public String getSelectedUser()
	{
		return selectedUser;
	}

	public void setSelectedUser(String selectedUser)
	{
		this.selectedUser = selectedUser;
	}

	public void sendNewMsg(String fromUser, String toUser)
	{
		Message newMsg = new Message();
		newMsg.setMessage(msg);
		dialogEnabled = false;
		newMsg.setUserIDFrom(fromUser);
		newMsg.setUserIDTo(toUser);
		newMsg.setDate(new Date());
		newMsg.setAlreadyRead(false);
		messageRepository.save(newMsg);
		msg = "";
		if (!disableFrontEnd)
			addMessage("Nachricht wurde verschickt!");
	}

	public void sendToMultipleUsers()
	{
		pickListViewUser.addUsers();
		List<String> userList = pickListViewUser.getChosenUsernames();
		String temp;

		for (String username : userList)
		{
			temp = msg;
			sendNewMsg(sessionInfoBean.getCurrentUserName(), pickListViewUser.extractUserName(username));
			msg = temp;
		}
		msg = "";
		pickListViewUser.reinit();

	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public List<Message> getAllMsgs()
	{

		return messageRepository.findAll();
	}

	public List<Message> getMyMsgs()
	{

		return messageRepository.findByUserIDTo(sessionInfoBean.getCurrentUser().getUsername());
	}

	public List<Message> getMySentMsgs()
	{

		return messageRepository.findByUserIDFrom(sessionInfoBean.getCurrentUser().getUsername());
	}

	public List<Message> getUnreadMsgs()
	{

		return messageRepository.findUnreadMsgs(sessionInfoBean.getCurrentUser().getUsername());
	}

	public void tabChange()
	{

		msgIDtoDisplay = -1;
	}

	public void windowClosed()
	{
		dialogEnabled = false;
	}

}
