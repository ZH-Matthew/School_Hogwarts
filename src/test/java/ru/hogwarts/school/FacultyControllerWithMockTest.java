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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
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

@WebMvcTest
class FacultyControllerWithMockTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private AvatarService avatarService;
    @MockBean
    private StudentService studentService;
    @SpyBean
    private FacultyService facultyService;
    @InjectMocks
    private FacultyController facultyController;


    @Test
    public void getFacultyTest() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME));
    }

    @Test
    public void postFacultyTest() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(FACULTY_OBJECT.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME));
    }

    @Test
    public void putFacultyTest() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(FACULTY_OBJECT.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME));
    }
    @Test
    public void deleteFacultyTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1")
                        .content(FACULTY_OBJECT.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getFacultyByNameOrColorTest() throws Exception {
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase("Faculty", null)).thenReturn(List.of(FACULTY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?name=Faculty")
                        .content(FACULTY_OBJECT.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(NAME));
    }

}