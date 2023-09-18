package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("avatar")
public class AvatarController {
    AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/from-db/{id}")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/from-file/{id}")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException{
        Avatar avatar = avatarService.findAvatar(id);
        Path path = Path.of(avatar.getFilePath());
        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("/getAvatarsByPage")
    public ResponseEntity<byte[]> getAvatarsByPage(@RequestParam("page") Integer pageNumber,@RequestParam("size") Integer pageSize) {
        List<Avatar> avatars = avatarService.getAvatarsByPage(pageNumber,pageSize);
        /*Вот тут встрял, вроде код в репозитории и сервисе написан корректно, я же хочу вернуть именно страницу аватарок
        в виде листа, значит по логике вот до этого места ^  я написал все +/- корректно, но как передать именно лист ?
        ведь методы вызываются конкретно у каждой аватарки, а не у листа.
        Подумал что можно пройтись фор ич по листу и сформировать у каждой аватарки респонс, но как это теперь все вернуть, я хз.
         */

        for(Avatar av : avatars){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(av.getMediaType()));
            headers.setContentLength(av.getData().length);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(av.getData());
        }
        return null; //заглушка
    }
}
