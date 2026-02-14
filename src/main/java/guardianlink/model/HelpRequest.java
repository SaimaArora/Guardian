package guardianlink.model;

import guardianlink.model.Category;
//single help reuqest by a user
// has id, name, helptype, status

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank; //Bean Vlaidation API(Jakarta validation)
import jakarta.validation.constraints.Size;

@Entity //tells jpa to map class to a dtbs table
public class HelpRequest {

    @Id //primary key of table
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment id
    private Long id;

    @ManyToOne //many helprequests can share one category
    @JoinColumn(name = "category_id") //in helprequest table - column categoryid will be created(will store id of category)
    private Category category;

    @NotBlank(message = "Name must not be empty") //defines rules on the fields
    @Size(min = 2, message = "Name must be atleast 2 characters long")
    private String name;

    private String status;

    // REQUIRED by jpa: default constructor
    public HelpRequest() {
    }
    public HelpRequest(Long id, String name, String status, Category category) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
