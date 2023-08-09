package eapli.base.exammanagement.application.antlr;

import eapli.base.exammanagement.AnswersBaseVisitor;
import eapli.base.exammanagement.AnswersParser;

import java.util.Iterator;
import java.util.LinkedList;

public class GradeAnsweredExamVisitor extends AnswersBaseVisitor<Double> {

    private String currentSolution;
    private Iterator<String> solutionIterator;
    private double thisQuestionScore=0.0;
    private final LinkedList<String> correctionPrint = new LinkedList<>();
    private boolean alreadyEnteredThisQuestionFlag = false;


    public void setExamSolution (LinkedList<String> examSolutions){
        solutionIterator = examSolutions.iterator();
    }

    public LinkedList<String> getCorrectionPrint() {
        return correctionPrint;
    }

    @Override
    public Double visitScore(AnswersParser.ScoreContext ctx) {
        thisQuestionScore = Double.parseDouble(ctx.number().getText());
        return 0.;
    }

    //----SHORT_ANSWER----
    @Override
    public Double visitSaAnswer(AnswersParser.SaAnswerContext ctx) {
        String answer = (ctx.FREE_TEXT() != null) ? ctx.FREE_TEXT().getText() : "";
        return commonGradeValidation(answer);
    }

    private Double commonGradeValidation(String answer){
        currentSolution = solutionIterator.next();
        saveCorrectionPrint(thisQuestionScore, currentSolution, answer);
        return (currentSolution.equals(answer)) ?  thisQuestionScore : 0.;
    }

    @Override
    public Double visitSaQuestion(AnswersParser.SaQuestionContext ctx) {
        correctionPrint.add("Question: " + ctx.FREE_TEXT().getText());
        return super.visitSaQuestion(ctx);
    }

    //----MATCHING----
    @Override
    public Double visitMatchAnswerText(AnswersParser.MatchAnswerTextContext ctx) {
        if (currentSolution.equals("*") && alreadyEnteredThisQuestionFlag) return 0.; //se a questão antes desta também usa * para validar o numero de questões inserida, a flag vai estar a false para que posssa executar o .next do iterador e entrar nas soluções da próxima pergunta
        alreadyEnteredThisQuestionFlag = true;
        currentSolution = solutionIterator.next();
        if (currentSolution.equals("*")) return 0.; //assumindo que a solução tem 4 linhas: da 3 para a 4 linha vai fazer o .next(), a partir daí não se pode deixar que .next() seja executado mais vezes senão o iterador ia entrar em respostas de perguntas seguintes
        String answer = ctx.getText();
        saveCorrectionPrint(thisQuestionScore, currentSolution, answer);
        return (currentSolution.equals(answer)) ?  thisQuestionScore : 0.;
    }

    @Override
    public Double visitMatchAnswer(AnswersParser.MatchAnswerContext ctx) {
        if (ctx.matchAnswerOptions() == null) return 0.;
        return super.visitMatchAnswer(ctx);
    }

    @Override
    public Double visitMatchQuestion(AnswersParser.MatchQuestionContext ctx) {
        correctionPrint.add("Question: " + ctx.FREE_TEXT().getText());
        //Se, por exemplo, a solução tiver 4 linhas e o estudante apenas tiver respondido com 1, o siclo while vai
        //incrementar (3 vezes) o iterador com as soluções até encontrar o asterisco e aí avançar para a proxima questão
        Double result = super.visitMatchQuestion(ctx);
        if (currentSolution.equals("*") && !alreadyEnteredThisQuestionFlag){
            currentSolution = solutionIterator.next(); //se não houver respostas ao matching, é necessário incrementar o iterador se ele estiver num astrisco de uma questão anterior
            saveCorrectionPrint(thisQuestionScore, currentSolution, "");
        }
        if (!currentSolution.equals("*")){
            while (!(currentSolution = solutionIterator.next()).equals("*")){
                saveCorrectionPrint(thisQuestionScore, currentSolution, "");
            }
        }
        alreadyEnteredThisQuestionFlag = false;
        return result;
    }

    //----MULTIPLE_CHOICE----
    @Override
    public Double visitMcAnswer(AnswersParser.McAnswerContext ctx) {
        double scoreOfQuestion = 0.;
        if (ctx.mcLine() == null){
            currentSolution = solutionIterator.next();
            return 0.;
        }
        for (AnswersParser.McLineContext lineCtx : ctx.mcLine()) {
            currentSolution = solutionIterator.next();
            if (currentSolution.equals("*")) return scoreOfQuestion; //assumindo que a solução tem 4 linhas: da 3 para a 4 linha vai fazer o .next(), a partir daí não se pode deixar que .next() seja executado mais vezes senão o iterador ia entrar em respostas de perguntas seguintes
            String answer = "";
            if (lineCtx.FREE_TEXT() != null) answer = lineCtx.FREE_TEXT().getText();
            saveCorrectionPrint(thisQuestionScore, currentSolution, answer);
            if (currentSolution.equals(answer)) {
                scoreOfQuestion += thisQuestionScore;
            }
        }
        //    System.out.println(scoreOfQuestion);
        return scoreOfQuestion;
    }

    @Override
    public Double visitMcQuestion(AnswersParser.McQuestionContext ctx) {
        correctionPrint.add("Question: " + ctx.FREE_TEXT().getText());
        //Se, por exemplo, a solução tiver 4 linhas e o estudante apenas tiver respondido com 1, o siclo while vai
        //incrementar (3 vezes) o iterador com as soluções até encontrar o asterisco e aí avançar para a proxima questão
        Double result = super.visitMcQuestion(ctx);
        if (!currentSolution.equals("*")){
            while (!(currentSolution = solutionIterator.next()).equals("*")){
                saveCorrectionPrint(thisQuestionScore, currentSolution, "");
            }
        }
        alreadyEnteredThisQuestionFlag = false;
        return result;
    }

    //----NUMERICAL----
    @Override
    public Double visitNAnswer(AnswersParser.NAnswerContext ctx) {
        String answer = (ctx.number() != null) ? ctx.number().getText() : "";
        return commonGradeValidation(answer);
    }

    @Override
    public Double visitNumQuestion(AnswersParser.NumQuestionContext ctx) {
        correctionPrint.add("Question: " + ctx.FREE_TEXT().getText());
        return super.visitNumQuestion(ctx);
    }

    //----SELECT_MISSING_WORD----
    @Override
    public Double visitMwAnswer(AnswersParser.MwAnswerContext ctx) {
        String answer = (ctx.oneWordOrNumber() != null) ? ctx.oneWordOrNumber().getText() : "";
        return commonGradeValidation(answer);
    }

    @Override
    public Double visitSelectMissingWord(AnswersParser.SelectMissingWordContext ctx) {
        correctionPrint.add("Question: Select Missing Word");
        return super.visitSelectMissingWord(ctx);
    }

    //----TRUE_OR_FALSE----
    @Override
    public Double visitTfAnswer(AnswersParser.TfAnswerContext ctx) {
        String answer = (ctx.trueOrFalseAnswer() != null) ? ctx.trueOrFalseAnswer().getText() : "";
        return commonGradeValidation(answer);
    }

    @Override
    public Double visitTfQuestion(AnswersParser.TfQuestionContext ctx) {
        correctionPrint.add("Question: " + ctx.FREE_TEXT().getText());
        return super.visitTfQuestion(ctx);
    }

    protected Double aggregateResult(Double aggregate, Double nextResult) {
        if (aggregate == null && nextResult == null) {
            return 0.;
        } else if (aggregate != null && nextResult != null) {
            return aggregate + nextResult;
        } else if (aggregate == null){
            return nextResult;
        }
        return aggregate;
    }

    private void saveCorrectionPrint(double questionScore, String solution, String answer) {
        correctionPrint.add("Score: " + questionScore);
        correctionPrint.add("Correct answer: " + solution);
        correctionPrint.add("Student answer: " + answer + "\n");
    }
}
