package org.example.model;

public abstract class Person {
    private String firstName  ;
    private String lastName  ;
    private String email  ;
    private String password  ;

    protected Person(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    //getters and setters methods area

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }
}
