package no.svitts.core.reader;


import no.svitts.core.exception.FileReaderException;
import org.constretto.model.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileReader {

    public String readFile(String path) {
        return readFile(new File(path));
    }

    public String readFile(File file) {
        Path path = Paths.get(file.getPath());
        try {
             return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new FileReaderException("Could not read file [" + file.getAbsolutePath() + "]");
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
            throw new FileReaderException("Could not read resource [" + classPathResource.toString() + "]");
        }
    }

}
