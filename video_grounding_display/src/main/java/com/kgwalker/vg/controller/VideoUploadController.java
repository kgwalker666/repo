package com.kgwalker.vg.controller;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
public class VideoUploadController {
    @PostMapping("upload")
    public Map<String, Object> uploadVideo(@RequestPart("file") MultipartFile video) throws IOException {
        String filename = video.getOriginalFilename();
        if (filename == null) {
            filename = "upload.mp4";
        }
        Path path = Paths.get(String.valueOf(this.getClass().getClassLoader().getResource("")), filename);
        video.transferTo(path);
        return Map.of("code", 200, "data", path.getFileName());
    }
}
