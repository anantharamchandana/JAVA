package com.psp.app.service;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psp.app.dao.AssistanceRepo;
import com.psp.app.dao.ContactRepo;
import com.psp.app.dao.CustomerRepo;
import com.psp.app.dao.DonationRepo;
import com.psp.app.dao.PetRepo;
import com.psp.app.dao.ReportRepo;
import com.psp.app.dao.ScheduleRepo;
import com.psp.app.dao.ServiceRepo;
import com.psp.app.model.Assistance;
import com.psp.app.model.Contact;
import com.psp.app.model.Customer;
import com.psp.app.model.Donation;
import com.psp.app.model.Pet;
import com.psp.app.model.Report;
import com.psp.app.model.Schedule;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private PetRepo petRepo;
	
	@Autowired
	private ScheduleRepo scheduleRepo;
	
	@Autowired
	private ContactRepo contactRepo;
	
	@Autowired
	private ReportRepo reportRepo;
	
	@Autowired
	private ServiceRepo serviceRepo;
	
	@Autowired
	private AssistanceRepo assistanceRepo;
	
	@Autowired
	private DonationRepo donationRepo;
	
	public int saveUser(Customer user) {
		customerRepo.save(user);
		if(customerRepo.save(user)!=null) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public Customer findUser(String email) {
		List<Customer> customer = customerRepo.findAll();
		System.out.println("----"+customer.size());
		if(customer.size() == 0) {
			return null;
		}
		List<Customer> veifiedUser = customer.stream().filter(n -> n.getEmail().equals(email)).collect(Collectors.toList());
		if(veifiedUser.size() > 0) {
			return veifiedUser.get(0);
		}
		else {
			return null;
		}
		
	}
	
	public int authenticateUser(Customer customer) {
		
		if(customer.getEmail().equals("admin@gmail.com") && customer.getPassword().equals("admin")) {
			
			
			return 2;
		}
		
		List<Customer> users = customerRepo.findAll();
		List<Customer> veifiedUser = users.stream().filter(n -> (n.getEmail().equals(customer.getEmail()) || n.getUsername().equals(customer.getEmail())) && n.getPassword().equals(customer.getPassword())).collect(Collectors.toList());
		
		if(veifiedUser.size() ==1) {
			return 1;
		}
		else {
			return 0;
		}
			
	}
	
	public Customer findUserByUsername(String username) {
		
		List<Customer> users = customerRepo.findAll();
		List<Customer> veifiedUser = users.stream().filter(n -> n.getUsername().equals(username)).collect(Collectors.toList());
		if(veifiedUser.size() > 0) {
			return veifiedUser.get(0);
		}
		else {
			return null;
		}
		
	}
	
	public int validatePassword(Customer customermodel, String securityQuestion, String securityAnswer) {
		List<Customer> users = customerRepo.findAll();
		List<Customer> verifiedUser = users.stream().filter(n -> n.getEmail().equals(customermodel.getEmail())).collect(Collectors.toList());
		if(verifiedUser.size() ==1) {
			List<Customer> userSecurities = customerRepo.findAll();
			
			List<Customer> securedUser = userSecurities.stream().filter(security -> security.getSecurityQuestion().equals(securityQuestion) && security.getSecurityAnswer().equals(securityAnswer)
					
					).collect(Collectors.toList());
			if(securedUser.size() == 1) {
				return 1;
			}
			else {
				return 2;
			}
		}
		else {
			return 0;
		}
	}
	
	public void saveNewPassword(Customer customermodel) {
			
			Customer customer = customerRepo.findbyEmail(customermodel.getEmail());
			System.out.println("user#########"+customer.toString());
			customer.setPassword(customermodel.getPassword());
			customerRepo.save(customer);
	}
	
	public void deleteUser(Long id) {
			
			customerRepo.deleteById(id);
			
	}

	public List<Pet> getAllPets() {
		// TODO Auto-generated method stub
		
		return petRepo.findAll();
	}


	public List<Pet> searchPets(String searchKey) {
		
		List<Pet> pets = petRepo.findAll();
		List<Pet> searchedPets = pets.stream().filter(pet -> pet.getName().contains(searchKey) ||
				pet.getColor().equals(searchKey) || pet.getBreed().contains(searchKey)).collect(Collectors.toList());
		return searchedPets;
		
	}
	
	public List<Pet> filterPets(String type, String color, String breed) {
		List<Pet> pets = petRepo.findAll();
		
		List<Pet> filteredPets = pets.stream().filter(pet -> pet.getType().equals(type) || pet.getColor().equals(color) || pet.getBreed().equals(breed)).collect(Collectors.toList());	
		
		
		
		return filteredPets.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(Pet::getId))),
                ArrayList::new));
		
	}

	@Override
	public void saveSlot(Schedule schedule) {
		// TODO Auto-generated method stub
		scheduleRepo.save(schedule);
		
	}

	@Override
	public void saveTicket(Contact contact) {
		// TODO Auto-generated method stub
		contactRepo.save(contact);
		
	}

	@Override
	public void saveReport(Report report) {
		// TODO Auto-generated method stub
		reportRepo.save(report);
		
	}

	@Override
	public void saveService(com.psp.app.model.Service service) {
		// TODO Auto-generated method stub
		serviceRepo.save(service);
		
	}

	@Override
	public void saveAssistance(Assistance assistance) {
		// TODO Auto-generated method stub
		assistanceRepo.save(assistance);
		
	}

	@Override
	public void saveDonation(Donation donation) {
		// TODO Auto-generated method stub
		donationRepo.save(donation);
		
	}

}
