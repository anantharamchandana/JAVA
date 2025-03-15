package com.psp.app.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.psp.app.model.Assistance;
import com.psp.app.model.Contact;
import com.psp.app.model.Customer;
import com.psp.app.model.Donation;
import com.psp.app.model.Email;
import com.psp.app.model.Pet;
import com.psp.app.model.Report;
import com.psp.app.model.Schedule;
import com.psp.app.model.Service;
import com.psp.app.service.CustomerService;
import com.psp.app.service.CustomerServiceImpl;
import com.psp.app.service.MessageService;
import com.psp.app.service.OwnerServiceImpl;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private MessageService messageService;
	
	
	@GetMapping("/customer")
	public String getCustomerWelcomePage(@ModelAttribute("customer") Customer customer, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Customer userdata = customerService.findUser(messages.get(0));
        List<Pet> pets = customerService.getAllPets();
        model.addAttribute("pets", pets);
		return "customer/welcomecustomer";
	}
	
	@PostMapping("/searchPets")
	public String searchPets(Model model, HttpSession session, @RequestParam("searchKey") String searchKey ) {
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer customer = customerService.findUser(messages.get(0));
        model.addAttribute("sessionMessages", messages);
        List<Pet> pets = customerService.searchPets(searchKey);
        model.addAttribute("pets", pets);
		return "customer/welcomecustomer";
	}
	
	@PostMapping("/applyFilters")
	public String applyFilters(Model model, HttpSession session, @RequestParam("type") String type,
			 @RequestParam("color") String color, @RequestParam("breed") String breed) {
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer customer = customerService.findUser(messages.get(0));
        model.addAttribute("sessionMessages", messages);
        List<Pet> pets = customerService.filterPets(type,color, breed);
        model.addAttribute("pets", pets);
		return "customer/welcomecustomer";
	}
	

	@GetMapping("/schedule")
	public String schedule(Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Customer customer = customerService.findUser(messages.get(0));
       
        Schedule schedule = new Schedule();
        model.addAttribute("schedule", schedule);
		return "customer/schedule";
	}
	
	@PostMapping("/bookSlot")
	public String bookSlot(@ModelAttribute("schedule") Schedule schedule, Model model, HttpSession session) {
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Customer customer = customerService.findUser(messages.get(0));
        
        schedule.setUserMail(customer.getEmail());
        
        customerService.saveSlot(schedule);
        return "redirect:/customer";
        
	}
	
	@GetMapping("/refer")
	public String referral(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer userdata = customerService.findUser(messages.get(0));
	
		return "customer/refer";
	}
	

	@PostMapping("/refernow")
	public String refer(@RequestParam("email") String email, Model model, HttpSession session)
	{
		int output = 0;
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer customer = customerService.findUser(messages.get(0));
		Email emailmodel = new Email();
		emailmodel.setMsgBody("You have been referred by "+ customer.getEmail());
		emailmodel.setRecipient(email);
		emailmodel.setSubject("Referral from Pet Services Portal");
		
		System.out.println("------------------body"+ emailmodel.getMsgBody()+"======="+ emailmodel.getRecipient());
		output = messageService.sendSimpleMail(emailmodel);
		
		System.out.println("------------------"+ output);
		if(output !=1) {
			model.addAttribute("errmsg", "User Email address not found.");
		}
		
		return "redirect:/customer";
	}
	

	@GetMapping("/contact")
	public String contact(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer userdata = customerService.findUser(messages.get(0));
		Contact contact = new Contact();
		model.addAttribute("contact", contact);
		return "customer/contact";
	}
	

	@PostMapping("/raiseTicket")
	public String raiseTicket(@ModelAttribute("contact") Contact contact, Model model, HttpSession session) {
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Customer customer = customerService.findUser(messages.get(0));
        
        contact.setUserMail(customer.getEmail());
        
        customerService.saveTicket(contact);
        return "redirect:/customer";
        
	}
	
	@GetMapping("/report")
	public String report(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer userdata = customerService.findUser(messages.get(0));
		Report report = new Report();
		model.addAttribute("report", report);
		return "customer/report";
	}
	

	@PostMapping("/reportNow")
	public String reportNow(@ModelAttribute("report") Report report, Model model, HttpSession session) {
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Customer customer = customerService.findUser(messages.get(0));
        
        report.setUserMail(customer.getEmail());
        
        customerService.saveReport(report);
        return "redirect:/customer";
        
	}
	
	@GetMapping("/services")
	public String services(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer userdata = customerService.findUser(messages.get(0));
		Service service = new Service();
		model.addAttribute("service", service);
		return "customer/petservices";
	}
	

	@PostMapping("/requestService")
	public String requestService(@ModelAttribute("service") Service service, Model model, HttpSession session) {
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Customer customer = customerService.findUser(messages.get(0));
        
        service.setUserMail(customer.getEmail());
        
        customerService.saveService(service);
        return "redirect:/customer";
        
	}
	
	@GetMapping("/assistance")
	public String petassistance(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer userdata = customerService.findUser(messages.get(0));
		Assistance assistance = new Assistance();
		model.addAttribute("assistance", assistance);
		return "customer/assistance";
	}
	

	@PostMapping("/requestAssistance")
	public String requestAssistance(@ModelAttribute("assistance") Assistance assistance, Model model, HttpSession session) {
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Customer customer = customerService.findUser(messages.get(0));
        
        assistance.setUserMail(customer.getEmail());
        
        customerService.saveAssistance(assistance);
        return "redirect:/customer";
        
	}
	
	@GetMapping("/donate")
	public String donate(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Customer userdata = customerService.findUser(messages.get(0));
		Donation donation = new Donation();
		model.addAttribute("donation", donation);
		return "customer/donate";
	}
	

	@PostMapping("/donateNow")
	public String donateNow(@ModelAttribute("donation") Donation donation, Model model, HttpSession session) {
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Customer customer = customerService.findUser(messages.get(0));
        
        donation.setUserMail(customer.getEmail());
        
        customerService.saveDonation(donation);
        return "redirect:/customer";
        
	}


}
