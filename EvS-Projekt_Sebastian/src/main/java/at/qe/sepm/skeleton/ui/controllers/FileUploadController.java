package at.qe.sepm.skeleton.ui.controllers;


import java.util.EnumSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.UserRole;
import at.qe.sepm.skeleton.repositories.UserRepository;
import at.qe.sepm.skeleton.services.UserService;

/**
 * Handles requests for the application file upload requests
 *  @author Elias Jochum <elias.jochum@student.uibk.ac.at>
 */
@Controller
public class FileUploadController {

	@Autowired
	private UserService userService;
	
	@Autowired 
	private UserRepository userRepository;
	
	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/admin/uploadFile", method = RequestMethod.POST)
	public String uploadFileHandler(@RequestParam("file") MultipartFile file) {

		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				String str = new String(bytes);
				EnumSet<UserRole> enumset = EnumSet.of(UserRole.STUDENT);
				Set<UserRole> set = enumset;
				
				String[] lines = str.split("\n");
				
				for(String tmp : lines) {
					String[] column = tmp.split(";");
					User user = userService.createUser(column[0], column[1], column[2], column[3]);
					user = userService.loadUser(column[0]);
					user.setEnabled(true);
					userRepository.save(user);
					user.setRoles(set);
					userRepository.save(user);	
				}
				
			    return "redirect:users.xhtml";
			} catch (Exception e) {
				return "redirect:users.xhtml";
			}
		} else {
			return "redirect:users.xhtml";
		}
	}
}