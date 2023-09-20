package artefact.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    private final int port;


    public InfoController(@Value("${server.port}") int port){
        this.port = port;
    }

    @GetMapping("getPort")
    public int getPort() {
        return port;
    }
}
