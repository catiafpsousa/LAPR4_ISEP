package eapli.base.utils;

import com.ibm.icu.impl.IllegalIcuArgumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class ExamFileManipulation {
    public static Map<String, String> getListOfQuestionsOnFile (String filePath) throws FileNotFoundException {
        Map<String, String> questionsMap = new HashMap<>();

        File questionsFile = new File(filePath);
        Scanner reader = new Scanner(questionsFile);
        String currentLine = reader.nextLine();
        if (!currentLine.equals("[Questions_Repo]")) throw new IllegalIcuArgumentException("File inserted isn't a valid questions file");

        currentLine = reader.nextLine();
        while (!Objects.equals(currentLine, "[/Questions_Repo]")){
            if (currentLine.startsWith("**")){
                String examType = currentLine.substring(2,currentLine.length() - 2);
                StringBuilder questionText = new StringBuilder();
                questionText.append(currentLine).append("\n");
                currentLine = reader.nextLine();
                while (!currentLine.startsWith("**") && !currentLine.startsWith("[/")){
                    questionText.append(currentLine).append("\n");
                    currentLine = reader.nextLine();
                }
                questionsMap.put(examType,questionText.toString());
            }
        }
        return questionsMap;
    }

    public static String findContentWithoutQuotesInContent(String content ,String contentToFind){
        int startIndex = content.indexOf(contentToFind) + contentToFind.length();
        int endIndex = content.indexOf("\n",startIndex);
        return content.substring(startIndex, endIndex); //EXAMPLE: for the line ->Open Date-01/01/2000<- it will return ->01/01/2000<-
    }

    public static void saveStringToTxtFileInPath(String content, String path) throws IOException {
        File fileWithString = new File(path);
        FileWriter writer = new FileWriter(fileWithString);
        writer.write(content);
        writer.flush();
        writer.close();
    }
}
