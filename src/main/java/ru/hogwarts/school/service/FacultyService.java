package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FacultyService {
    Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create Faculty");
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> findFaculty(long id) {
        logger.info("Was invoked method for find Faculty");
        logger.debug("Requesting Faculty by id: {}",id);
        return facultyRepository.findById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit Faculty");
        logger.debug("Change data in the faculty : {}",faculty);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete Faculty");
        facultyRepository.deleteById(id);
    }


    public List<Faculty> findByNameOrColor(String name, String color) {
        logger.info("Was invoked method for requesting Faculty by name or color");
        logger.debug("Requesting Faculty by name: {} , or color: {}",name,color);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public String getLongestDepartmentName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElse("Not Found");
    }


    public Integer getValueFromParallelStream() {
        Instant start = Instant.now();
        Integer sum = Stream.iterate(1, a -> a +1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        System.out.println("Прошло времени, мс: " + elapsed);
        return sum;
    }

    public Integer getValueWithoutParallelStream() { //вариант без parallel в среднем оказался быстрее
        Instant start = Instant.now();
        Integer sum = Stream.iterate(1, a -> a +1)
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        System.out.println("Прошло времени, мс: " + elapsed);
        return sum;
    }
}
