package guardianlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // = @configuration + @EnableAutoConfiguration +@ComponentScan
public class GuardianLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuardianLinkApplication.class, args);
    }
}
//entry point of springboot application
//spring scans com.saim.guardianlink.* and finds : controller, service, beans, web server (tomcat)
//tells springboot that this is the main application, and scan all packages below this