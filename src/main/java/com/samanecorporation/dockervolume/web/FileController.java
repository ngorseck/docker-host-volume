package com.samanecorporation.dockervolume.web;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("create")
public class FileController {

    private String directory_name ;

    public FileController(@Value(("${app.data.folder}"))String directory_name) {
        this.directory_name = directory_name;
    }

    @GetMapping("/{fileName}")
    public String create(@PathVariable("fileName") String fileName) throws IOException {
        Path newFilePath = Paths.get(directory_name + fileName + ".txt");
        Files.createFile(newFilePath);
        return "File created";
    }
}
