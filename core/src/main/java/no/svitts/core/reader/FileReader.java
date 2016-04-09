package no.svitts.core.reader;


import org.constretto.model.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReader.class);

    public String readFile(String path) {
        return readFile(new File(path));
    }

    public String readFile(File file) {
        Path path = Paths.get(file.getPath());
        try {
             return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            LOGGER.error("Could not read file {}", file.getAbsolutePath(), e);
            return "";
        }
    }

    public String readResource(String resourceName) {
        return readResource(new ClassPathResource(resourceName));
    }

    public String readResource(ClassPathResource classPathResource) {
        InputStream inputStream = classPathResource.getInputStream();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            LOGGER.error("Could not read resource {}", classPathResource.toString(), e);
            return "";
        }
    }

}
