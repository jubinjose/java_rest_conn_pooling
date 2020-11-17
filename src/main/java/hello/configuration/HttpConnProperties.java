package hello.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpConnProperties {

    // Determines the timeout in milliseconds until a connection is established.
    @Value("${http.connect.timeout:30000}")
    private int connectTimeout;

    // The timeout when requesting a connection from the connection manager.
    @Value("${http.connect.request.timeout:30000}")
    private int connectRequestTimeout;

    // The timeout for waiting for data
    @Value("${http.socket.timeout:30000}")
    private int socketTimeout;

    @Value("${http.pooling.maxTotal:200}")
    private int maxTotal;

    @Value("${http.pooling.defaultMaxPerRoute:100}")
    private int defaultMaxPerRoute;

    @Value("${http.connection.keepalive:20000}")
    private int keepAliveTime;

    @Value("${http.connection.closeIdleWait:30000}")
    private int closeIdleConnectionWait;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getConnectRequestTimeout() {
        return connectRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public int getCloseIdleConnectionWait() {
        return closeIdleConnectionWait;
    }

}
