package ru.hogwarts.school;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.hogwarts.school.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    @Autowired
    ObjectMapper objectMapper; // класс который помогает преобразовывать обьекты в JSON и обратно.

    @Test
    void postStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setAge(20L);
        student.setName("ivan");

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .content(objectMapper.writeValueAsString(student)) //преобразовывает объект в строку JSON
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.age").value("20"))
                .andExpect(jsonPath("$.name").value("ivan"));
    }


    @Test
    public void getStudentTest() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));

        mockMvc.perform(get("/student/" + ID_S)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_S))
                .andExpect(jsonPath("$.name").value(NAME_S));
    }
    @Test
    public void getStudentExceptionTest() throws Exception { // нужно посмотреть что возвращает метод, у нас он возвращает именно пустой Optional , значит нам нужно тоже вернуть пустой Optional для выброса исключения
        Optional<Student> empty = Optional.empty();

        when(studentRepository.findById(100L)).thenReturn(empty);

        mockMvc.perform(get("/student/getFacultyByStudentId/" + 100)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void putStudentTest() throws Exception {
        when(studentRepository.save(any(Student.class))).thenReturn(STUDENT);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(put("/student")
                        .content(STUDENT_OBJECT.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_S))
                .andExpect(jsonPath("$.name").value(NAME_S));
    }
    @Test
    public void deleteStudentTest() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(delete("/student/1")
                        .content(STUDENT_OBJECT.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getFacultyByStudentId() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        faculty.setId(2L);

        Student student = new Student();
        student.setId(1L);
        student.setAge(20L);
        student.setName("ivan");
        student.setFaculty(faculty);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/getFacultyByStudentId/" + 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(faculty));
    }

    @Test
    public void getStudentsByFacultyTest() throws Exception {
        when(studentRepository.findStudentsByFaculty_Id(any(Long.class))).thenReturn(List.of(STUDENT));
        mockMvc.perform(get("/student/allByFaculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(ID_S))
                .andExpect(jsonPath("$[0].name").value(NAME_S));
    }
    @Test
    public void getStudentByAgeBetweenTest() throws Exception {
        when(studentRepository.findByAgeBetween(10L,30L)).thenReturn(List.of(STUDENT));
        mockMvc.perform(get("/student?min=10&max=30")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(ID_S))
                .andExpect(jsonPath("$[0].name").value(NAME_S));
    }
}