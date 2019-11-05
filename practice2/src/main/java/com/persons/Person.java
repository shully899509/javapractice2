package com.persons;

import javax.persistence.*;
import java.util.Objects;

//@Entity
//@Table(name = "persons")
//@Inheritance(
   //     strategy = InheritanceType.TABLE_PER_CLASS
//)
public class Person {
    //@Id
    //@GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    private String name;

    public Person(){
    }

    public Person(String name){
        this.name = name;
    }

    public Person(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id &&
                Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public void setName(String name) {
        this.name = name;
    }
}
