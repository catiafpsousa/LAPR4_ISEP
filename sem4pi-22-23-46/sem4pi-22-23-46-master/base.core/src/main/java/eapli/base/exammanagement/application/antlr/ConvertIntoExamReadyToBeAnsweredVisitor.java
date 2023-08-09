package eapli.base.exammanagement.application.antlr;

import eapli.base.exammanagement.ExamBaseVisitor;
import eapli.base.exammanagement.ExamParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Objects;

public class ConvertIntoExamReadyToBeAnsweredVisitor extends ExamBaseVisitor<String> {

    //----MATCHING----
    @Override
    public String visitMatchOptions(ExamParser.MatchOptionsContext ctx) {
        return visitChildren(ctx) + "(Answer like this example: \"Left column option\"--\"Right column option\")\n";
    }

    @Override
    public String visitMatchSolution(ExamParser.MatchSolutionContext ctx) {
//        StringBuilder numOfAwnserReturn = new StringBuilder();
//        for(ExamParser.MatchAnswerContext maList : ctx.matchAnswer()){
//            numOfAwnserReturn.append("Answer: \n");
//        }
//        return numOfAwnserReturn.toString(); //remove "Solution:" text from the exam
        return "Answer: \n";
    }

//    @Override
//    public String visitMatchAnswer(ExamParser.MatchAnswerContext ctx) {
//        return "Answer: \n";
//    }


    //----MULTIPLE_CHOICE----
    @Override
    public String visitMcRightAnswer(ExamParser.McRightAnswerContext ctx) {
        return ctx.getText().substring(1);
    }

    @Override
    public String visitMultipleChoice(ExamParser.MultipleChoiceContext ctx) {
        return visitChildren(ctx) + "(If you chose more than one option write each one in a new line) \nAnswer: \n";
    }


    //----SHORT_ANSWER----
    @Override
    public String visitSaCorrectAnswer(ExamParser.SaCorrectAnswerContext ctx) {
        return "Answer: \n";
    }

    //----NUMERICAL----
    @Override
    public String visitNumAnswer(ExamParser.NumAnswerContext ctx) {
        return "Answer: \n";
    }


    //----TRUE_OR_FALSE----
    @Override
    public String visitTfAnswer(ExamParser.TfAnswerContext ctx) {
        return "Answer: \n";
    }


    //----SELECT_MISSING_WORD----
    @Override
    public String visitMwAnswer(ExamParser.MwAnswerContext ctx) {
        return " Answer:  ";
    }


    //----General functions----
    protected String aggregateResult(String aggregate, String nextResult) {
        if (aggregate == null){
            return Objects.requireNonNullElse(nextResult, "");
        } else  if (nextResult != null){
            return aggregate + nextResult;
        }
        return aggregate;
    }

    @Override
    public String visitTerminal(TerminalNode node) {
        return node.getText();
    }
}
