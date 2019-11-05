package com.persons;

import com.javapractice.practice2.model.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@Service
public class PersonService {

    private final PersonRepository personRepository;

//    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    public Person getPersonById(int id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            return person.get();
        } else {
            throw new EntityNotFoundException("Person with ID " + id + " not found.");
        }
    }

    private Person getPersonByIdOrNull(int id){
        return personRepository.findById(id).orElse(null);
    }

    public List<Person> getPersonsByName(String name){
        return personRepository.findPersonsByName(name);
    }

    public Person insertPerson(Person person){
        return personRepository.save(person);
    }

    public void deletePerson(Person person){
        personRepository.delete(person);
    }

    public Person updatePerson(Person person, String nameReplace){
        if (getPersonByIdOrNull(person.getId()) != null){
            return personRepository.save(new Person(person.getId(), nameReplace));
        } else {
            throw new EntityNotFoundException("Person with ID " + person.getId() + " not found.");
        }

    }

}
