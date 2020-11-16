package hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class PropertyValueFindController {

    @Autowired
    private Environment env;

    @RequestMapping("/property")
    public String index(@RequestParam String name) {
        String value = env.getProperty(name);
        return "Property value of " + name + " = " + value;
    }

}