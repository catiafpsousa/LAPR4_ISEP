package eapli.base.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Shared class that represents CSV Reader and has a method to read one types of CSV File:
 * - does not have a header, column separation is done using ","
 *
 * @author Pedro Garrido <1182090@isep.ipp.pt>
 */

public class CSVReader {

    /**
     * Constructor of CSVReader
     */
    public CSVReader() {
    }

    /**
     * Reader of CSV Files:
     * - the other type does not have a header, column separation is done using ","
     * character.
     *
     * @param fileName Name of File
     * @return A list of Strings of all text information in the file
     * @throws IOException If the file does not exist or is corrupted
     */

    public static List<String[]> readCsvStudentFile(String fileName) throws IOException {

        Path pathToFile = Paths.get(fileName);
        List<String[]> allData = new ArrayList<>();
        BufferedReader readerFirstLine = Files.newBufferedReader(pathToFile,
                StandardCharsets.ISO_8859_1);
        String firstLine = readerFirstLine.readLine();
        readerFirstLine.close();

        if (firstLine.contains(",")) {
            BufferedReader br = Files.newBufferedReader(pathToFile,
                    StandardCharsets.ISO_8859_1);
            String data;

            while ((data = br.readLine()) != null && !data.isEmpty()) {
                String[] snsUserData = data.split(",");
                allData.add(snsUserData);
            }
            br.close();
            return allData;
        }
        return null;
    }

    /**
     * Method that tries to read a given file
     * @param fileName name of the file to read and its path
     * @return (true) if the file is found
     * @throws FileNotFoundException if the file is not found in the given path
     */
    public static boolean tryReadFile(String fileName) throws FileNotFoundException {
        File myObj = new File(fileName);
        Scanner myReader = new Scanner(myObj);
        myReader.close();
        return true;
    }
}





