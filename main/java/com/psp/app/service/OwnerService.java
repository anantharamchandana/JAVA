package com.psp.app.service;

import java.util.List;

import com.psp.app.model.Pet;

public interface OwnerService {

	void savePet(Pet pet);
	
	Pet getPet();
	
	List<Pet> getAllPets();
	
	void deletePet(Long id);
	
	Pet getPetById(Long id);
	
	void updatePet(Pet pet);

}
