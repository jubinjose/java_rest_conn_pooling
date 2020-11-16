package hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@RestController
public class DelayController {

    @GetMapping("/api/delay")
    public String delayGet(@RequestParam long delayMillis) throws InterruptedException {
        return delay(delayMillis);
    }

    @PostMapping("/api/delay")
    public String delayPost(@RequestParam long delayMillis) throws InterruptedException {
        return delay(delayMillis);
    }

    private String delay(long delayMillis) throws InterruptedException {
        Thread.sleep(delayMillis);
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(ZonedDateTime.now());
    }
}
