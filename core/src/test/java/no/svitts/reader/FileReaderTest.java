package no.svitts.reader;

import no.svitts.core.reader.FileReader;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class FileReaderTest {

    @Test
    public void readFile_ShouldReturnStringWithContent() throws IOException {
        String targetContent = "SELECT * FROM test;";
        File file = getTempFile("testfile", ".sql", targetContent);
        assertContent(targetContent, new FileReader().readFile(file));
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

    private void assertContent(String targetContent, String readContent) {
        assertNotNull(readContent);
        assertFalse(readContent.isEmpty());
        assertEquals(targetContent, readContent);
    }
}
