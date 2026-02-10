package guardianlink.model;


//single help reuqest by a user
// has id, name, helptype, status

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //tells jpa to map class to a dtbs table
public class HelpRequest {

    @Id //primary key of table
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment id
    private Long id;

    private String name;
    private String helpType;
    private String status;

    // REQUIRED by jpa: default constructor
    public HelpRequest() {
    }

    public HelpRequest(Long id, String name, String helpType, String status) {
        this.id = id;
        this.name = name;
        this.helpType = helpType;
        this.status = status;
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

    public String getHelpType() {
        return helpType;
    }

    public void setHelpType(String helpType) {
        this.helpType = helpType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
