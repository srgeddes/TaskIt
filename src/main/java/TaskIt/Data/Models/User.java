package TaskIt.Data.Models;

import java.security.PublicKey;

public class User {
    
    public int Id;
    public String Username;
    public String Password;
    
    public User(int id, String username, String password) {
        this.Username = username;
        this.Password = password;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}
