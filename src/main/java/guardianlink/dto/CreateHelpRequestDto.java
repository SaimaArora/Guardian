package guardianlink.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

//dto - data transfer object is used here as the frontend should not send the full jpa entities, we only need name and catid
//dto keeps api clean, db model protected

public class CreateHelpRequestDto {

    @NotBlank(message = "Name must not be empty")
    @Size(min = 2, message = "Name must be atleast 2 characters long")
    private String name;

    @NotNull(message = "Category")
    private Long categoryId;

    @NotNull(message = "User is required")
    private Long userId;

    public Long getUserId(){ return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

}
