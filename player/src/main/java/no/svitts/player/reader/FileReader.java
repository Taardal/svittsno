package no.svitts.player.reader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader {

    public String readFile(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        return new String(Files.readAllBytes(path));
    }

}
