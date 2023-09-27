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

import java.util.*;

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


}
