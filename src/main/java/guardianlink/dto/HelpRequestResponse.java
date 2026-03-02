package guardianlink.dto;

public class HelpRequestResponse {
        private Long id;
        private String name;
        private String status;
        private String categoryName;
        private String requestedBy;
        private String assignedVolunteer;

        public HelpRequestResponse(Long id,
                                   String name,
                                   String status,
                                   String categoryName,
                                   String requestedBy,
                                   String assignedVolunteer) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.categoryName = categoryName;
            this.requestedBy = requestedBy;
            this.assignedVolunteer = assignedVolunteer;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getRequestedBy() {
            return requestedBy;
        }

        public void setRequestedBy(String requestedBy) {
            this.requestedBy = requestedBy;
        }

        public String getAssignedVolunteer() {
            return assignedVolunteer;
        }

        public void setAssignedVolunteer(String assignedVolunteer) {
            this.assignedVolunteer = assignedVolunteer;
        }

}
