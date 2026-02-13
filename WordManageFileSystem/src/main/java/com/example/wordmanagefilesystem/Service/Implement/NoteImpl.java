package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteImpl {

    @Autowired
    private NoteMapper noteMapper;


}
