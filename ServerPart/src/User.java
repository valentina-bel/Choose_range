public class User extends Human {
    private int userId;
    private String login;
    private String password;
    private String role;

    public User() {
        super();
        this.userId = 1;
        this.login = "";
        this.password = "";
        this.role = "user";
    }

    public User(String str) {
        String [] attributes = str.split("\\|");
        for(int i = 0; i < attributes.length; i++) {
            attributes[i] = attributes[i].trim();
        }
        this.setLastName(attributes[0]);
        this.setFirstName(attributes[1]);
        this.userId = Integer.parseInt(attributes[2]);
        this.login = attributes[3];
        this.password = ServerPart.decrypt(attributes[4]);
        this.role = attributes[5];
    }

    public int getUserId() {
        return this.userId;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return role;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLogin(String login) {
        this.login =login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(User obj) {
        return (this.getLogin().equals(obj.getLogin()) && this.getPassword().equals(obj.getPassword()));
    }

    @Override
    public String toString() {
        String role;
        if (this.role.equals("admin"))
            role = "Администратор";
        else
            role = "Пользователь";
        String str = String.format("|%3d|%10s|%10s|%10s|%10s|%14s|", this.userId, getLastName(), getFirstName(), this.login, this.password, role);
        return str;
    }

    public String stringFile() {
        String str = getLastName() + '|' + getFirstName() + '|' + Integer.toString(this.userId) + '|' + this.login + '|' + ServerPart.encrypt(this.password) + '|' + this.role;
        return str;
    }
}
