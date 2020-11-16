package hello.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class RestTemplateConfiguration {

    private static final Logger LOG =
            LoggerFactory.getLogger(RestTemplateConfiguration.class);

    @Autowired
    CloseableHttpClient httpClient;

    @Value("${http.pooling.enabled:true}")
    private boolean httpPoolingEnabled;

    @Value("${read.timeout:30000}")
    private int readTimeout;

    @Value("${connect.timeout:10000}")
    private int connectTimeout;

    @Value("${connect.request.timeout:30000}")
    private int connectionRequestTimeout;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("poolScheduler");
        scheduler.setPoolSize(50);
        return scheduler;
    }

    @Bean("nonPooledRestTemplate")
    public RestTemplate getNonPooledRestTemplate() {
        HttpComponentsClientHttpRequestFactory componentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        componentsClientHttpRequestFactory.setConnectTimeout(connectTimeout);
        componentsClientHttpRequestFactory.setReadTimeout(readTimeout);
        componentsClientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        RestTemplate restTemplate = new RestTemplate(componentsClientHttpRequestFactory);
        restTemplate.setMessageConverters(
                Arrays.asList(
                        //new ByteArrayHttpMessageConverter(), //
                        new StringHttpMessageConverter(), //
                        //new ResourceHttpMessageConverter(), //
                        //new SourceHttpMessageConverter(), //
                        //new AllEncompassingFormHttpMessageConverter(), //
                        //new MappingJackson2XmlHttpMessageConverter(), //
                        new MappingJackson2HttpMessageConverter()));
        return restTemplate;
    }

    @SuppressWarnings("rawtypes")
    @Bean("pooledRestTemplate")
    public RestTemplate getPooledRestTemplate() {

        var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(readTimeout);
        factory.setConnectionRequestTimeout(connectionRequestTimeout);
        factory.setConnectTimeout(connectTimeout);

        LOG.info("httpPoolingEnabled: {}", httpPoolingEnabled);
        if (httpPoolingEnabled){
            factory.setHttpClient(httpClient);
        }

        RestTemplate restTemplate = new RestTemplate(factory);

        restTemplate.setMessageConverters(
                Arrays.asList(
                        //new ByteArrayHttpMessageConverter(), //
                        new StringHttpMessageConverter(), //
                        //new ResourceHttpMessageConverter(), //
                        //new SourceHttpMessageConverter(), //
                        //new AllEncompassingFormHttpMessageConverter(), //
                        //new MappingJackson2XmlHttpMessageConverter(), //
                        new MappingJackson2HttpMessageConverter()));
        return restTemplate;
    }
}
