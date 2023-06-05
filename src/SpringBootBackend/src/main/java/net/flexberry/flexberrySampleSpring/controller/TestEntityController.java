package net.flexberry.flexberrySampleSpring.controller;

import net.flexberry.flexberrySampleSpring.model.TestEntity;
import net.flexberry.flexberrySampleSpring.service.TestEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TestEntityController {
    @Autowired
    TestEntityService service;

    @GetMapping("/testentities/{primaryKey}")
    public TestEntity getTestEntity(@PathVariable("primaryKey") UUID primaryKey) {
        return service.getTestEntity(primaryKey);
    }

    @GetMapping("/testentities/enabled")
    public List<TestEntity> getEnabledTestEntities() {
        return service.getEnabledTestEntities();
    }

    @DeleteMapping("/testentities/{primaryKey}")
    public void deleteTestEntity(@PathVariable("primaryKey") UUID primaryKey) {
        service.deleteTestEntityByPrimaryKey(primaryKey);
    }

    @PostMapping("/testentities")
    public void addTestEntity(@RequestBody TestEntity entity) {
        service.saveOrUpdateTestEntity(entity);
    }

    @PutMapping("/testentities")
    public void updateTestEntity(@RequestBody TestEntity entity) {
        service.saveOrUpdateTestEntity(entity);
    }
}
