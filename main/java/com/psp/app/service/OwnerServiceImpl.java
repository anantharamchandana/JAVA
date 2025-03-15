package com.psp.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psp.app.dao.PetRepo;
import com.psp.app.model.Pet;

@Service
public class OwnerServiceImpl implements OwnerService{
	
	@Autowired 
	private PetRepo petRepo;
	
	public void savePet(Pet pet) {
		
		Pet savedPet = petRepo.save(pet);
		
	}

	public Pet getPet() {
		// TODO Auto-generated method stub
		return petRepo.findAll().get(0);
	}

	public List<Pet> getAllPets() {
		// TODO Auto-generated method stub
		List<Pet> products = petRepo.findAll();
		return products;
	}

	public void deletePet(Long id) {
		
		petRepo.deleteById(id);
	
	}

	public Pet getPetById(Long id) {
		// TODO Auto-generated method stub
		
		Pet pet = petRepo.findPetById(id);

		return pet;
	}

	
	public void updatePet(Pet pet) {

		
		Pet savedPet = petRepo.save(pet);

	}

	

}
