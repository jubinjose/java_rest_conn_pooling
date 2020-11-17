package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestCallController {

    private RestTemplate pooledRestTemplate;
    private RestTemplate nonPooledRestTemplate;
    private RestTemplate okHttpRestTemplate;

    @Autowired
    public RestCallController(@Qualifier("pooledRestTemplate") RestTemplate pooledRestTemplate,
                              @Qualifier("nonPooledRestTemplate") RestTemplate nonPooledRestTemplate,
                              @Qualifier("okHttpRestTemplate") RestTemplate okHttpRestTemplate){
        this.pooledRestTemplate = pooledRestTemplate;
        this.nonPooledRestTemplate = nonPooledRestTemplate;
        this.okHttpRestTemplate = okHttpRestTemplate;
    }

    @GetMapping("/api/rest/pooled")
    public String httpGetPooled(@RequestParam String url) {
        return fetchUrlGet(url, this.pooledRestTemplate);
    }

    @PostMapping("/api/rest/pooled")
    public String httpPostPooled(@RequestParam String url) {
        return fetchUrlPost(url, this.pooledRestTemplate);
    }

    @GetMapping("/api/rest/nonpooled")
    public String httpGetNonPooled(@RequestParam String url) {
        return fetchUrlGet(url, this.nonPooledRestTemplate);
    }

    @PostMapping("/api/rest/nonpooled")
    public String httpPostNonPooled(@RequestParam String url) {
        return fetchUrlPost(url, this.nonPooledRestTemplate);
    }

    @GetMapping("/api/rest/okhttp")
    public String httpGetOkHttp(@RequestParam String url) {
        return fetchUrlGet(url, this.okHttpRestTemplate);
    }

    @PostMapping("/api/rest/okhttp")
    public String httpPostOkHttp(@RequestParam String url) {
        return fetchUrlPost(url, this.okHttpRestTemplate);
    }

    public String fetchUrlGet(String url, RestTemplate restTemplate){
        var result = restTemplate.exchange(url,
                HttpMethod.GET, getHeaders(), String.class);
        return result.getBody();
    }

    public String fetchUrlPost(String url, RestTemplate restTemplate){

        var result = restTemplate.exchange(url,
                HttpMethod.POST, getHeaders(), String.class);
        return result.getBody();
    }

    public HttpEntity<String> getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(headers);
    }
}