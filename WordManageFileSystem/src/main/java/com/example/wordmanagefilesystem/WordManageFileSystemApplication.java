package com.example.wordmanagefilesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ServletComponentScan
public class WordManageFileSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordManageFileSystemApplication.class, args);
    }

}
