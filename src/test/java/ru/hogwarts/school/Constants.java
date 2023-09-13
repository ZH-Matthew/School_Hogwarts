package ru.hogwarts.school;

import net.minidev.json.JSONObject;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

public class Constants {
    public static final Long ID = 1L;
    public static final String NAME = "Бригадный подряд";
    public static final String COLOR = "Чёрный";
    public static final JSONObject FACULTY_OBJECT = new JSONObject();
    public static final Faculty FACULTY = new Faculty();

    public static final Long ID_S = 1L;
    public static final String NAME_S = "Anna";
    public static final int AGE_S = 20;
    public static final JSONObject STUDENT_OBJECT = new JSONObject();
    public static final Student STUDENT = new Student();

    static {
        STUDENT_OBJECT.put("name", NAME_S);
        STUDENT_OBJECT.put("age", AGE_S);
        STUDENT.setId(ID_S);
        STUDENT.setName(NAME_S);
        STUDENT.setAge(AGE_S);
        FACULTY_OBJECT.put("name", NAME);
        FACULTY_OBJECT.put("color", COLOR);
        FACULTY.setId(ID);
        FACULTY.setName(NAME);
        FACULTY.setColor(COLOR);
        STUDENT_OBJECT.put("faculty",FACULTY);
        STUDENT.setFaculty(FACULTY);
    }
}
