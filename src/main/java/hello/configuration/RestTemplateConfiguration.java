package hello.configuration;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfiguration {

    private static final Logger LOG =
            LoggerFactory.getLogger(RestTemplateConfiguration.class);

    final CloseableHttpClient httpClient;
    final HttpConnProperties httpConnProperties;

    public RestTemplateConfiguration(CloseableHttpClient httpClient, HttpConnProperties httpConnProperties) {
        this.httpClient = httpClient;
        this.httpConnProperties = httpConnProperties;
    }

    @Bean("nonPooledRestTemplate")
    public RestTemplate getNonPooledRestTemplate() {
        return createRestTemplate(createHttpComponentsClientHttpRequestFactory(false));
    }

    @SuppressWarnings("rawtypes")
    @Bean("pooledRestTemplate")
    public RestTemplate getPooledRestTemplate() {
        return createRestTemplate(createHttpComponentsClientHttpRequestFactory(true));
    }

    private RestTemplate createRestTemplate(ClientHttpRequestFactory factory){
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

    private HttpComponentsClientHttpRequestFactory createHttpComponentsClientHttpRequestFactory(boolean isPoolingEnabled){
        var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(httpConnProperties.getSocketTimeout());
        factory.setConnectionRequestTimeout(httpConnProperties.getConnectRequestTimeout());
        factory.setConnectTimeout(httpConnProperties.getConnectTimeout());

        LOG.info("httpPoolingEnabled: {}", isPoolingEnabled);
        if (isPoolingEnabled){
            factory.setHttpClient(httpClient);
        }
        return factory;
    }

    @Bean("okHttpRestTemplate")
    public RestTemplate okhttp3Template() {
        // create the okhttp client builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool okHttpConnectionPool = new ConnectionPool(50,
                httpConnProperties.getKeepAliveTime(), TimeUnit.MILLISECONDS);
        builder.connectionPool(okHttpConnectionPool);
        builder.connectTimeout(httpConnProperties.getConnectTimeout(), TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(false);
        return createRestTemplate(new OkHttp3ClientHttpRequestFactory(builder.build()));
    }
}
