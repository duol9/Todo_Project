package com.example.todoapplication.controller;

import com.example.todoapplication.service.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final Service service;

    public TodoController(Service service) {
        this.service = service;
    }
}
