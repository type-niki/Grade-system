package com.chuka.gradesystem.model;

import java.io.Serializable;

/**
 * Person - Abstract Base Class
 *
 * This class demonstrates ABSTRACTION and ENCAPSULATION in OOP.
 *
 * Key OOP Concepts:
 * 1. ABSTRACTION - This is an abstract class that cannot be instantiated directly
 * 2. ENCAPSULATION - All fields are private with public getters/setters
 * 3. INHERITANCE - This class will be extended by Student class
 *
 * @author Charles David
 * @course ACSC 223 - Object Oriented Programming (Java 1)
 * @institution Chuka University
 */
public abstract class Person implements Serializable {
    // Private fields demonstrating ENCAPSULATION
    // Data hiding - fields cannot be accessed directly from outside
    private String id;
    private String name;
    private String email;
    private String phone;

    /**
     * Default Constructor
     */
    public Person() {
    }

    /**
     * Parameterized Constructor
     * @param id Unique identifier
     * @param name Full name
     * @param email Email address
     * @param phone Phone number
     */
    public Person(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // GETTERS - Provide controlled read access to private fields
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    // SETTERS - Provide controlled write access to private fields
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Abstract Method - ABSTRACTION
     *
     * This method MUST be implemented by any concrete class that extends Person.
     * This demonstrates abstraction - we define WHAT should be done,
     * but not HOW it should be done.
     *
     * @return Detailed information about the person
     */
    public abstract String getDetails();

    /**
     * toString method for string representation
     * @return String representation of Person
     */
    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}