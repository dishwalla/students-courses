package test.task;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@RestController
public class Rest {
    private Properties prop;
    
    @PostConstruct
    public void init() throws IOException {
        InputStream is = null;
            this.prop = new Properties();
            is = this.getClass().getResourceAsStream("/users.properties");
            prop.load(is);
    }
    
    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void auth(@RequestParam("login") String login, @RequestParam("password") String password){
        System.out.println("AUTH rest");
        System.out.println("LOGIN " + login + " Password " + password);
        if(!credentialsExists(login, password)){
            throw new UnauthorizedException();
        }
    }


    private boolean credentialsExists(String login, String password){
        return password.equals(prop.get(login));
    }


}
