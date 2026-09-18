package model;

public class User {

    // Encapsulation
    private int userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String dobOrAge;
    private double height;
    private double weight;

    // Constructor
    public User(
            int userId,
            String name,
            String email,
            String phone,
            String password,
            String dobOrAge,
            double height,
            double weight
    ) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.dobOrAge = dobOrAge;
        this.height = height;
        this.weight = weight;
    }

    // Getters

    public int getUserId() {
        return userId;
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

    public String getPassword() {
        return password;
    }

    public String getDobOrAge() {
        return dobOrAge;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    // Setters

    public void setUserId(int userId) {
        this.userId = userId;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDobOrAge(String dobOrAge) {
        this.dobOrAge = dobOrAge;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void displayProfile() {
        System.out.println("\n========== USER PROFILE ==========");
        System.out.println("User ID    : " + userId);
        System.out.println("Name       : " + name);
        System.out.println("Email      : " + email);
        System.out.println("Phone      : " + phone);
        System.out.println("DOB / Age  : " + dobOrAge);
        System.out.println("Height     : " + height + " m");
        System.out.println("Weight     : " + weight + " kg");
        System.out.println("==================================");
    }
}