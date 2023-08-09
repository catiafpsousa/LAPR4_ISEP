package eapli.base.utils;

import eapli.base.exammanagement.ExamLexer;
import eapli.base.exammanagement.ExamParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class CommonAntlrFunctions {

    public static ExamParser getExamParserFilePath(String filePath) throws IOException {
        ExamLexer lexer = new ExamLexer(CharStreams.fromFileName(filePath));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new ExamParser(tokens);
    }

    public static ExamParser getExamParserString(String stringExam) {
        ExamLexer lexer = new ExamLexer(CharStreams.fromString(stringExam));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new ExamParser(tokens);
    }

}
