package guardianlink.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users") //user is reserved word in some dbs, so avoid conflicts
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User name must not be empty")
    @Size(min = 2, message = "User name must be atleast 2 characters")
    private String name;

    @NotBlank(message = "Email must not be empty")
    private String email;

    @NotBlank(message = "Password must not be empty")
    @com.fasterxml.jackson.annotation.JsonIgnore //password never sent to frontend
    private String password;

    @OneToMany(mappedBy = "user") //one user many helprequest
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<HelpRequest> helpRequests;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String pass) { password = pass; }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<HelpRequest> getHelpRequests() {
        return helpRequests;
    }
    public void setHelpRequests(List<HelpRequest> helpRequest) {
        this.helpRequests = helpRequests;
    }
}
