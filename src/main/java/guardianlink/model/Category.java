package guardianlink.model;
import jakarta.persistence.*;

@Entity //becomes a db table entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //primary key, auto increment
    private Long id;

    @Column(unique = true, nullable = false) //no duplicates, name cannot be null
    private String name;

    public Category(){
        //default constructor, required by jpa
        //jpa needs a way to create an object without knowing the specifics of your class's other constructors
    }
    public Category(String name) {
        this.name = name;
    }

    //getters and setters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
