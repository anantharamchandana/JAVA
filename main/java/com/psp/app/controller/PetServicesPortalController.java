package com.psp.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.psp.app.model.Customer;
import com.psp.app.model.Email;
import com.psp.app.service.CustomerService;
import com.psp.app.service.CustomerServiceImpl;
import com.psp.app.service.MessageService;
import com.psp.app.service.MessageServiceImpl;

@Controller
public class PetServicesPortalController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private MessageService messageService;
	
	@GetMapping("/")
	public String getHome(Model model) {
		return "index";
	}
	
	@GetMapping("/register")
	public String getRegister(Model model) {

		Customer user = new Customer();
		model.addAttribute("user", user);
		return "home/register";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute("customer") Customer customer,Model model) {
		System.out.println("save===user");

		Customer existingUser = customerService.findUser(customer.getEmail());

		if (existingUser != null) {
			model.addAttribute("errormsg", "Email already exists");
			return "home/error";
		}

		Customer existingUsername = customerService.findUserByUsername(customer.getUsername());

		if (existingUsername != null) {
			model.addAttribute("errormsg", "Username already exists");
			return "home/error";
		}

		int output = customerService.saveUser(customer);
		
		if (output > 0) {
			return "redirect:/login";
		} else {
			model.addAttribute("errormsg", "Account creation failed");
			return "home/error";
		}
	}
	
	@GetMapping("/login")
	public String getLoginPage(Model model,  HttpSession session, HttpServletRequest request)
	{	
		request.getSession().invalidate();
		Customer usermodel = new Customer();
		model.addAttribute("user", usermodel);
		return "home/login";
	}
	
	@PostMapping("/authenticateLogin")
	public String loginUser(@ModelAttribute("user") Customer customer,RedirectAttributes attributes,HttpServletRequest request,HttpServletResponse response, Model model)
	{
		System.out.println("login**************************************** ");
		int result = customerService.authenticateUser(customer);
		String username="";
		String useremail="";
		System.out.println("output=== "+result);
		if(result != 0)
		{
			@SuppressWarnings("unchecked")
			List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
			if (messages == null) {
				messages = new ArrayList<>();
				request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
			}
			if(result == 2) {
				username=customer.getEmail().split("@")[0].toString().toUpperCase();
				useremail=customer.getEmail();
				messages.add(useremail);
				request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
				return "redirect:/owner";
			} 
			else {
				username=customer.getEmail().split("@")[0].toString().toUpperCase();
				useremail=customer.getEmail();
				messages.add(useremail);
				request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
				return "redirect:/customer";
			}
		}
		else {
			model.addAttribute("errormsg", "Login Failed. Invalid Credentials. Please try again.");
			return "home/error";
		}
		
		
	}
	
	@GetMapping("/forgotUsername")
	public String getForgotUsernamePage(Model model)
	{
		Customer customermodel = new Customer();
		model.addAttribute("customer", customermodel);
		return "home/forgotusername";
	}
	
	@GetMapping("/forgotPassword")
	public String getForgotPasswordPage(Model model)
	{
		Customer customermodel = new Customer();
		model.addAttribute("customer", customermodel);
		return "home/forgotpassword";
	}
	
	
	@PostMapping("/sendMail")
	public String sendMail(@ModelAttribute("customer") Customer customer, Model model)
	{	
		int output = 0;
		System.out.println("save===usernew password");
		System.out.println("userModel#########"+customer.toString());
		Customer customerModel=customerService.findUser(customer.getEmail());
		
		if(customerModel == null) {
			model.addAttribute("errormsg", "Email Id doesnot exist in our database");
			return "home/error";
		}
		
		Email emailmodel = new Email();
		emailmodel.setMsgBody("Your Username is "+ customerModel.getUsername());
		emailmodel.setRecipient(customerModel.getEmail());
		emailmodel.setSubject("Username Recovery from Pet Services Portal");
		System.out.println("------------------body"+ emailmodel.getMsgBody()+"======="+ emailmodel.getRecipient());
		output = messageService.sendSimpleMail(emailmodel);
		
		System.out.println("------------------"+ output);
		if(output !=1) {
			model.addAttribute("errmsg", "User Email address not found.");
		}
		return "redirect:/login";
	}
	
	@PostMapping("/validateForgotPassword")
	public String validatePassword(@ModelAttribute("customer") Customer customer, @RequestParam("securityQuestion") String securityQuestion,
			 @RequestParam("securityAnswer") String securityAnswer,
			Model model,RedirectAttributes redirectAttrs)
	{
		System.out.println("forgot password**************************************** ");
		int output = customerService.validatePassword(customer, securityQuestion, securityAnswer);
		
		if(output == 1)
		{
			
			return "home/changepassword";
		}
		else if(output == 0) {
			model.addAttribute("errormsg", "Invalid User Email");
			return "home/error";
		}
		else if(output == 2) {
			model.addAttribute("errormsg", "Invalid Security Question or Answer");
			return "home/error";
		}
		else {
			model.addAttribute("errormsg", "Password cannot be changed. Invalid credentials.");
			return "home/error";
		}
		
		
	}
	
	@GetMapping("/changePassword")
	public String getChangePasswordPage(Model model)
	{
		Customer customermodel = new Customer();
		model.addAttribute("customer", customermodel);
		return "home/changepassword";
	}
	
	@PostMapping("/saveNewPassword")
	public String saveNewPassword(@ModelAttribute("customer") Customer customer, HttpServletRequest request, @Param("confirmPassword") String confirmPassword, Model model)
	{
		if(confirmPassword.equals(customer.getPassword())) {
			
			customerService.saveNewPassword(customer);
		}
		else {
			model.addAttribute("errormsg", "Passwords donot match");
			return "home/error";
		}
		System.out.println("save===usernew password");
		System.out.println("userModel#########"+customer.toString());
		 request.getSession().invalidate();
		return "redirect:/login";
	}
	
	@RequestMapping("/destroy")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }
	
	@RequestMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer customer = customerService.findUser(messages.get(0));
		
		model.addAttribute("customer", customer);
        return "home/profile";
    }
	
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute("customer") Customer customer, Model model)
	{
		System.out.println("save===user");
		int output =customerService.saveUser(customer);
		if(output>0) {
			return "redirect:/profile";
		}
		
		else {
			model.addAttribute("errormsg", "Operation failed. Please try again");
			return "home/error";
		}
		
	}
	
	@PostMapping("/deleteProfile/{id}")
	public String deleteProfile(@PathVariable(name="id") Long id,HttpServletRequest request, Model model)
	{
		customerService.deleteUser(id);
		 request.getSession().invalidate();
		 model.addAttribute("errormsg", "Your Account Deleted Successfully");
			return "home/error";
	}
	
	@GetMapping("/resetPassword")
	public String resetPassword(Model model, HttpSession session) {
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		Customer userdata = customerService.findUser(messages.get(0));
		
		model.addAttribute("user", userdata);
		
		return "home/resetpassword";
		
		
		
	}

}
