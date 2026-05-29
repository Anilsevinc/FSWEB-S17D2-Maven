package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.workintech.s17d2.model.Experience;
import com.workintech.s17d2.model.JuniorDeveloper;
import com.workintech.s17d2.model.MidDeveloper;
import com.workintech.s17d2.model.SeniorDeveloper;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private final Taxable taxable;

    private Map<Integer, Developer> developers;

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable Integer id) {
        return developers.get(id);
    }

    @PostMapping
    public Developer addDeveloper(@RequestBody Developer developer) {

        Developer newDeveloper;

        if (developer.getExperience() == Experience.JUNIOR) {

            double salary =
                    developer.getSalary()
                            - (developer.getSalary() * taxable.getSimpleTaxRate() / 100);

            newDeveloper =
                    new JuniorDeveloper(
                            developer.getId(),
                            developer.getName(),
                            salary,
                            developer.getExperience());

        } else if (developer.getExperience() == Experience.MID) {

            double salary =
                    developer.getSalary()
                            - (developer.getSalary() * taxable.getMiddleTaxRate() / 100);

            newDeveloper =
                    new MidDeveloper(
                            developer.getId(),
                            developer.getName(),
                            salary,
                            developer.getExperience());

        } else {

            double salary =
                    developer.getSalary()
                            - (developer.getSalary() * taxable.getUpperTaxRate() / 100);

            newDeveloper =
                    new SeniorDeveloper(
                            developer.getId(),
                            developer.getName(),
                            salary,
                            developer.getExperience());
        }

        developers.put(newDeveloper.getId(), newDeveloper);

        return newDeveloper;
    }
    @PutMapping("/{id}")
    public Developer updateDeveloper(
            @PathVariable Integer id,
            @RequestBody Developer developer) {

        developers.put(id, developer);

        return developer;
    }

    @DeleteMapping("/{id}")
    public Developer deleteDeveloper(@PathVariable Integer id) {

        return developers.remove(id);
    }
}
