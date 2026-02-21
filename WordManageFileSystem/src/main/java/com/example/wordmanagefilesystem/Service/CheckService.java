package com.example.wordmanagefilesystem.Service;

import com.example.wordmanagefilesystem.Pojo.Word;

public interface CheckService {

    public Word[] getRandomWordsByPageSize(Integer pageSize);
}
