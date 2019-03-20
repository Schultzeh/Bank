package app.Entities;


import app.annotations.Column;

public class User {
    @Column
    private long id;
    @Column("social_number")
    private String socialNumber;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column
    private String password;
    @Column
    private String mail;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", socialNumber='" + socialNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }
}
