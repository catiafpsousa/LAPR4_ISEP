package eapli.base.exammanagement.application.antlr;

import eapli.base.exammanagement.ExamBaseListener;
import eapli.base.exammanagement.ExamParser;

import java.util.LinkedList;

public class ConvertExamIntoSolutionsListener extends ExamBaseListener {

    private final LinkedList<String> examAnswers = new LinkedList<>();

    private double maxGrade = 0.0;

    private double currentScore = 0.0;

    public LinkedList<String> getExamAnswers(){
        return examAnswers;
    }

    public double getMaxGrade() {
        return maxGrade;
    }

    @Override
    public void enterScore(ExamParser.ScoreContext ctx) {
        currentScore = Double.parseDouble(ctx.number().getText());
    }

    //----SHORT_ANSWER----
    @Override
    public void enterSaCorrectAnswer(ExamParser.SaCorrectAnswerContext ctx) {
        maxGrade += currentScore;
        examAnswers.add(ctx.getText().replaceAll(System.lineSeparator(),""));
    }

    //----MATCHING----
    @Override
    public void enterMatchAnswer(ExamParser.MatchAnswerContext ctx) {
        maxGrade += currentScore;
        examAnswers.add(ctx.getText().replaceAll(System.lineSeparator(),""));
    }

    @Override
    public void exitMatching(ExamParser.MatchingContext ctx) {
        examAnswers.add("*");
    }

    //----MULTIPLE_CHOICE----
    @Override
    public void enterMcRightAnswer(ExamParser.McRightAnswerContext ctx) {
        maxGrade += currentScore;
        examAnswers.add(ctx.FREE_TEXT().getText());
    }

    @Override
    public void exitMultipleChoice(ExamParser.MultipleChoiceContext ctx) {
        examAnswers.add("*");
    }

    //----NUMERICAL----
    @Override
    public void enterNumAnswer(ExamParser.NumAnswerContext ctx) {
        maxGrade += currentScore;
        examAnswers.add(ctx.getText().replaceAll(System.lineSeparator(),""));
    }

    //----SELECT_MISSING_WORD----
    @Override
    public void enterMwAnswer(ExamParser.MwAnswerContext ctx) {
        maxGrade += currentScore;
        examAnswers.add(ctx.oneWordOrNumber().getText().replaceAll(System.lineSeparator(),""));
    }

    //----TRUE_OR_FALSE----
    @Override
    public void enterTfAnswer(ExamParser.TfAnswerContext ctx) {
        maxGrade += currentScore;
        examAnswers.add(ctx.getText().replaceAll(System.lineSeparator(),""));
    }
}
