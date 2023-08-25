package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import java.util.*;

@Service
public class FacultyService {
    private long count = 0;
    private final Map<Long, Faculty> facultyMap= new HashMap<>();

    public Faculty createFaculty(Faculty faculty){
        faculty.setId(++count);
        facultyMap.put(count,faculty);
        return faculty;
    }

    public Faculty findFaculty(long id){
        return facultyMap.get(id);
    }

    public Faculty editFaculty(Faculty faculty){
        if(facultyMap.containsKey(faculty.getId())) {
            facultyMap.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public Faculty deleteFaculty(long id){
        return facultyMap.remove(id);
    }

    public Collection<Faculty> getFacultyByColor(String id) {
        List<Faculty> listFacultyByColor = new ArrayList<>();
        for (Map.Entry<Long, Faculty> entry: facultyMap.entrySet()) {
            if(entry.getValue().getColor().equals(id)){
                listFacultyByColor.add(entry.getValue());
            }
        }
        return listFacultyByColor;
    }
}
