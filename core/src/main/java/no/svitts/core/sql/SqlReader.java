package no.svitts.core.sql;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class SqlReader {

    private static final Logger LOGGER = Logger.getLogger(SqlReader.class.getName());

    public List<String> readSqlFile(String path) {
        BufferedReader bufferedReader = getBufferedReader(path);
        List<String> sqlQueries = getSqlQueries(bufferedReader);

        Stream<String> fileStream = getFileStream(path);
        List<String> queries = new ArrayList<>();
        if (fileStream != null) {

        }
        return sqlQueries;
    }

    private List<String> getSqlQueries(BufferedReader bufferedReader) {
        List<String> queries = new ArrayList<>();
        String query = "";
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                query += line;
                if (line.contains(";")) {
                    //Split query at ";" ??
                    queries.add(query);
                    query = "";
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not get SQL queries");
        }
        return queries;
    }

    private Stream<String> getFileStream(String path) {
        try {
            return Files.lines(Paths.get(path));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not read file " + path);
        }
        return null;
    }

    private BufferedReader getBufferedReader(String path) {
        FileReader fileReader = getFileReader(path);
        if (fileReader != null) {
            return new BufferedReader(fileReader);
        }
        return null;
    }

    private FileReader getFileReader(String path) {
        try {
            return new FileReader(new File(path));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Could not find file " + path);
        }
        return null;
    }

}
