package com.kgwalker.vg.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Map;

@RestController
public class VideoUploadController {
    @PostMapping("upload")
    public Map<String, Object> uploadVideo(@RequestPart("file") MultipartFile video) throws IOException {
        URL classPath = this.getClass().getClassLoader().getResource("");
        assert classPath != null;
        String clazz = classPath.getPath().replaceFirst("/", "");
        Path path = Paths.get(clazz, "static");
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        path = Paths.get(clazz, "static", "upload.mp4");
        // 如果存在则删除
        if (Files.exists(path)) {
            Files.delete(path);
        }
        video.transferTo(path);
        return Map.of("code", 200, "data", "/upload.mp4");
    }

    @PostMapping("real")
    public Map<String, Object> realVideo(Integer begin, Integer end) {
        // 调用系统生成视频帧
        String url = "";
        try {
            URL resource = this.getClass().getClassLoader().getResource("");
            assert resource != null;
            String clazz = resource.getPath().replaceFirst("/", "");
            Path path = Paths.get(clazz, "static");
            System.setProperty("user.dir", path.toString());
            Process pro = Runtime.getRuntime().exec(
                    "ffmpeg -i %s -ss 00:%d -to 00:%d -c:v libx264 -crf 30 -strict -2 upload_real.mp4".formatted("upload.mp4", begin, end)
            );
            // 等待子线程执行结束，返回路径即可
            pro.waitFor();
            url = "/upload_real.mp4";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Map.of("code", 200, "data", url);
    }
}
