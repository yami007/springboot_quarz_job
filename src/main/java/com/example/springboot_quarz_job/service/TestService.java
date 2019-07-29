package com.example.springboot_quarz_job.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    public void doJob(String str){
        System.out.println(str);
    }
}
