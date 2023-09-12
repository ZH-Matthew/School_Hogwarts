package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.hogwarts.school.Constants.*;

@WebMvcTest
class StudentControllerWithMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private AvatarService avatarService;

    @MockBean
    private FacultyService facultyService;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;


    @Test
    public void getStudentTest() throws Exception {
        Constants.initializationStudent();
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + ID_S)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_S))
                .andExpect(jsonPath("$.name").value(NAME_S));
    }

    @Test
    public void postStudentTest() throws Exception {
        Constants.initializationStudent();
        when(studentRepository.save(any(Student.class))).thenReturn(STUDENT);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(STUDENT_OBJECT.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_S))
                .andExpect(jsonPath("$.name").value(NAME_S));

    }
    @Test
    public void putStudentTest() throws Exception {
        Constants.initializationStudent();
        when(studentRepository.save(any(Student.class))).thenReturn(STUDENT);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(STUDENT_OBJECT.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_S))
                .andExpect(jsonPath("$.name").value(NAME_S));

    }
    @Test
    public void deleteStudentTest() throws Exception {
        Constants.initializationStudent();
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1")
                        .content(STUDENT_OBJECT.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getIdFacultyByStudentNameTest() throws Exception {
        Constants.initializationStudent();
        Constants.initializationFaculty();
        Constants.addFaculty();
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/getFaculty/" + ID_S)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}