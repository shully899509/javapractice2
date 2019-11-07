package com.javapractice.practice2;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.persons.Person;
import com.persons.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.TYPE)
@Transactional
@Rollback
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PersonServiceTest {
    private final PersonService personService;

    @Autowired
    public PersonServiceTest(PersonService personService) {
        this.personService = personService;
    }

    @Test
    public void countPersonsTest() {
        List<Person> persons = personService.getPersons();
        assertEquals(7, persons.size());
    }

    @Test
    public void countPersonsByNameTest() {
        List<Person> persons = personService.getPersonsByName("Luca");
        assertEquals(2, persons.size());
    }

    @Test
    public void personByIdFoundTest() {
        Person person = new Person(123, "Luca");
        assertEquals(personService.getPersonById(123), person);
    }

    @Test
    public void personByIdNotFoundExceptionTest() {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> personService.getPersonById(-1));
        assertEquals("Person with ID -1 not found.", exception.getMessage());
    }

    @Test
    public void insertPersonTest() {
        Person insertedPerson = personService.insertPerson(new Person("Gigi"));
        assertEquals(new Person(insertedPerson.getId(), "Gigi"), personService.getPersonById(insertedPerson.getId()));
    }

    @Test
    public void deletePersonTest() {
        Person insertedPerson = personService.insertPerson(new Person("Petrica"));
        personService.deletePerson(insertedPerson);

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> personService.getPersonById(insertedPerson.getId()));
        assertEquals("Person with ID " + insertedPerson.getId() + " not found.", exception.getMessage());
    }

    @Test
    public void updatePersonTest() {
        Person insertedPerson = personService.insertPerson(new Person("Brusli"));
        Person updatedPerson = personService.updatePerson(insertedPerson, "Vandam");

        assertEquals(updatedPerson.getName(), personService.getPersonById(insertedPerson.getId()).getName());
    }

}
