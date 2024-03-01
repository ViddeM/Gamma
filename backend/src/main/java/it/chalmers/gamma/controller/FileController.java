package it.chalmers.gamma.controller;

import it.chalmers.gamma.response.FileNotFoundResponse;
import it.chalmers.gamma.response.GetFileResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
public class FileController {
    @GetMapping("/{id}.{type}")
    public GetFileResponse getFile(@PathVariable("id") String fileName, @PathVariable("type") String type) {
        String filePath = String.format("uploads/%s.%s", fileName, type);
        try {
            byte[] data = StreamUtils.copyToByteArray(Files.newInputStream(Paths.get(filePath)));
            return new GetFileResponse(data);
        } catch (IOException e) {
            throw new FileNotFoundResponse();
        }
    }
}
