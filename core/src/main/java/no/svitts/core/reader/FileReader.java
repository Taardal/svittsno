package no.svitts.core.reader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReader {

    private static final Logger LOGGER = Logger.getLogger(FileReader.class.getName());

    public String readFile(File file) {
        Path path = Paths.get(file.getPath());
        return readFile(path);
    }

    public String readFile(String path) {
        return readFile(Paths.get(path));
    }

    public String readFile(Path path) {
        try {
             return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not read file");
        }
        return "Could not read file";
    }

}
