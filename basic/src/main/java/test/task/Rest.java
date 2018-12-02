package test.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@RestController
public class Rest {
    @Value("${data.login}")
    private String dataLogin;
    @Value("${data.password}")
    private String dataPassword;
    @Value("${data.host}")
    private String dataHost;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void login(){
        System.out.println("LOGIN");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void logout(){
        System.out.println("LOGOUT");
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String grades(@PathVariable("userId") String userId){
        System.out.println("GRADES");
        System.out.println("USER ID " + userId);

        RestTemplate restTemplate = new RestTemplate();
        final String dataPath
                = "http://" + dataHost + "/data/" + userId;
        ResponseEntity<String> response = null;
        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(null, Rest.fillHeaders(dataLogin, dataPassword));
            response = restTemplate.postForEntity( dataPath, request , String.class );

            if(response.getBody() == null){
                throw new NotFound();
            }

            return String.valueOf(response.getBody());
        }catch (ResourceAccessException e){
            throw new ServiceUnavailable();
        }catch (HttpClientErrorException.Forbidden e){
            throw new InternalServerError();
        }
    }

    public static String[] getCredentials(String authorization){
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            return credentials.split(":", 2);
        }

        return null;
    }

    public static HttpHeaders fillHeaders(String login, String password){
        String cred = login + ":" + password;
        System.out.println("CREED " + cred);
        String base64Credentials = new String(org.apache.commons.codec.binary.Base64.encodeBase64(cred.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
