package com.psp.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.psp.app.model.Customer;
import com.psp.app.model.Pet;
import com.psp.app.service.OwnerService;
import com.psp.app.service.OwnerServiceImpl;



@Controller
public class StoreOwnerController {
	
	@Autowired
	private OwnerService ownerService;
	
	@GetMapping("/owner")
	public String getOwnerWelcomePage(@ModelAttribute("customer") Customer customer, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
      
		return "owner/welcomestoreowner";
	}
	
	@GetMapping("/addPet")
	public String addPet(Model model, HttpSession session) {
		
		Pet pet = new Pet();
		model.addAttribute("pet", pet);
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

        if (messages == null) {
            messages = new ArrayList<>();
        }
        model.addAttribute("sessionMessages", messages);
       
		return "owner/addpet";
	}
	
	@PostMapping("/savePet")
	public String savePet(@ModelAttribute("pet") Pet pet, Model model, HttpSession session)
	{
		System.out.println("product created");
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        
		
		ownerService.savePet(pet);
		
		return "redirect:/owner";
	}
	
	@GetMapping("/viewPets")
	public String viewPets(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		List<Pet> pets = ownerService.getAllPets();
		model.addAttribute("pets", pets);
		
		return "owner/displaypets";
	}
	
	@GetMapping("/viewSinglePet/{id}")
	public String viewSinglePet(Model model, HttpSession session, @PathVariable(name="id") Long id) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Pet pet = ownerService.getPetById(id);
		
		
		model.addAttribute("pet", pet);
	
		
		return "owner/displaysinglepet";
	}
	
	@GetMapping("/editPet/{id}")
	public String editProduct(Model model, HttpSession session, @PathVariable(name="id") Long id) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Pet pet = ownerService.getPetById(id);
		
		
		model.addAttribute("pet", pet);
		

		return "owner/updatepet";
	}
	
	@PostMapping("/updateProduct")
	public String updatePet(@ModelAttribute("pet") Pet pet, Model model, HttpSession session)
	{
		System.out.println("pet updated");
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		
		
		ownerService.updatePet(pet);
		
		
		return "redirect:/owner";
	}
	
	@PostMapping("/deletePet/{id}")
	public String deletePet(@PathVariable(name="id") Long id)
	{
		ownerService.deletePet(id);
		
		return "redirect:/owner";
	}

}
