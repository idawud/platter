package io.turntabl.platterworker;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("crontab")
    public String crontab(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("ismaildawud96@gmail.com");
        message.setFrom("idawudinho@gmail.com");
        message.setSubject("Hello -From Platter Worker");
        message.setText("FATAL - Application crash. Save your job !! just working " + LocalDateTime.now().toString());
        System.out.println(message.toString());
        return "done";
    }

}
