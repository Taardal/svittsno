package no.svitts.reader;

import no.svitts.core.reader.FileReader;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class FileReaderTest {

    private FileReader fileReader;

    @Before
    public void setup() {
        fileReader = new FileReader();
    }

    @Test
    public void readFile_ShouldReturnStringWithContent() throws IOException {
        String targetContent = "SELECT * FROM test;";
        File file = getTempFile("testfile", ".sql", targetContent);
        assertContent(fileReader.readFile(file), targetContent);
        assertTrue(file.delete());
    }

    private File getTempFile(String prefix, String suffix, String content) throws IOException {
        File file = File.createTempFile(prefix, suffix);
        writeToFile(content, file);
        return file;
    }

    private void writeToFile(String string, File file) throws IOException {
        Path path = Paths.get(file.getPath());
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write(string);
        }
    }

    private void assertContent(String readContent, String targetContent) {
        assertNotNull(readContent);
        assertFalse(readContent.isEmpty());
        assertEquals(readContent, targetContent);
    }
}
