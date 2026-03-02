package guardianlink.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users") //user is reserved word in some dbs, so avoid conflicts
//@JsonIgnoreProperties({"helpRequests"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User name must not be empty")
    @Size(min = 2, message = "User name must be atleast 2 characters")
    @Column(nullable=false)
    private String name;

    @NotBlank(message = "Email must not be empty")
    @Email(message="Invalid email format")
    @Column(nullable=false, unique=true)
    private String email;

    @NotBlank(message = "Password must not be empty")
    @JsonIgnore //password never sent to frontend
    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; //"USER" or "VOLUNTEER"

    @OneToMany(mappedBy = "user") //one user many helprequest
    @JsonIgnore
    private List<HelpRequest> helpRequests;

    public User() {
    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public List<HelpRequest> getHelpRequests() { return helpRequests; }

    public void setHelpRequests(List<HelpRequest> helpRequests) {
        this.helpRequests = helpRequests;
    }

}
