package shenzhen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shenzhen.teamway.acsproxy.AscProxy;
import shenzhen.teamway.acsproxy.ThreadService;

@SpringBootApplication
public class AcsproxyApplication implements CommandLineRunner {
    private Logger log = LoggerFactory.getLogger(AcsproxyApplication.class);
    @Autowired
    private AscProxy ascProxy;
    @Autowired
    ThreadService threadService;

    public static void main(String[] args) {
        System.out.println("dsfsdf");
        SpringApplication.run(AcsproxyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

     // threadService.aa();
        ascProxy.init();
        ascProxy.sendDeviceStatus();
        ascProxy.sendDeviceEvent();
    }


}
