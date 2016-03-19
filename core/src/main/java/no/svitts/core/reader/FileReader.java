package no.svitts.core.reader;

import org.constretto.model.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FileReader {

    private static final Logger LOGGER = Logger.getLogger(FileReader.class.getName());

    public String readFile(String path) {
        return readFile(new File(path));
    }

    public String readFile(File file) {
        Path path = Paths.get(file.getPath());
        try {
             return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not read file", e);
        }
        return "";
    }

    public String readResource(String resourceName) {
        return readResource(new ClassPathResource(resourceName));
    }

    public String readResource(ClassPathResource classPathResource) {
        InputStream inputStream = classPathResource.getInputStream();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not read resource", e);
        }
        return "";
    }

}
