package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Service.Implement.NoteImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteImpl noteImpl;


}
