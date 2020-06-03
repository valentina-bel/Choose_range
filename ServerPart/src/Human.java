public class Human {
    private String lastName;
    private String firstName;

    public Human() {
        this.lastName = "";
        this.firstName = "";
    }

    public Human(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean equals(User obj) {
        return (this.lastName.equals(obj.getLastName()) && this.firstName.equals(obj.getFirstName()));
    }
}
