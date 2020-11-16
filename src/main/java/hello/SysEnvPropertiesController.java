package hello;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
public class SysEnvPropertiesController {

    @RequestMapping("/sysenv")
    public String index() {

        Map<String, String> sysProps = new HashMap<>();
        
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> sysProps.put(k.toString(), v.toString()));

        Map<String, String> env = System.getenv();
        
        Map<String, Map> combined = new HashMap<>();
        combined.put("system_properties", sysProps);
        combined.put("environment_properties", env);

        Gson gson = new Gson();
        return gson.toJson(combined);
    }

}