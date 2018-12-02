package test.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class AuthService {
    @Value("${auth.login}")
    private String authLogin;
    @Value("${auth.password}")
    private String authPassword;
    private final int LOGIN = 0;
    private final int PASSWORD = 1;
    @Value("${auth.host}")
    private String authHost;
    
    public void auth(String login, String password){
        final String authPath = "http://" + authHost + "/auth";
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("login", login);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, Rest.fillHeaders(authLogin, authPassword));

        ResponseEntity<String> response = restTemplate.postForEntity( authPath, request , String.class );
    }
}
