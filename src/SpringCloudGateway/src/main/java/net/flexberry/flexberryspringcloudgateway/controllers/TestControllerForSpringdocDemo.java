package net.flexberry.flexberryspringcloudgateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TestControllerForSpringdocDemo {
    @GetMapping("/testentities/{primaryKey}")
    public String getTestEntity(@PathVariable("primaryKey") UUID primaryKey) {
        return "TestControllerForSpringdocDemo";
    }
}
