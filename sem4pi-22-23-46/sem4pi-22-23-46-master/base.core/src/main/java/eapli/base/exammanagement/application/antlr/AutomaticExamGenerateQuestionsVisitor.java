package eapli.base.exammanagement.application.antlr;

import eapli.base.exammanagement.AutomaticExamBaseVisitor;
import eapli.base.exammanagement.AutomaticExamParser;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.questionmanagement.application.QuestionManagementService;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AutomaticExamGenerateQuestionsVisitor extends AutomaticExamBaseVisitor<String> {

    private final QuestionManagementService questionSvc = AuthzRegistry.questionService();

    public String visitQuestionType(AutomaticExamParser.QuestionTypeContext ctx) {
        String questionToReplace;
        switch (ctx.qType.getType()) {
            case AutomaticExamParser.SHORT_ANSWER:
                questionToReplace = questionSvc.findQuestionByType("SHORT_ANSWER").get().questionContent();
                //"Query para ir buscar um SHORT_AWNSER\n";
                break;
            case AutomaticExamParser.MATCHING:
                questionToReplace = questionSvc.findQuestionByType("MATCHING").get().questionContent();
                // "Query para ir buscar um MATCHING\n";
                break;
            case AutomaticExamParser.MULTIPLE_CHOICE:
                questionToReplace = questionSvc.findQuestionByType("MULTIPLE_CHOICE").get().questionContent();
                // "Query para ir buscar um MULTIPLE_CHOICE\n";
                break;
            case AutomaticExamParser.NUMERICAL:
                questionToReplace = questionSvc.findQuestionByType("NUMERICAL").get().questionContent();
                // "Query para ir buscar um NUMERICAL\n";
                break;
            case AutomaticExamParser.SELECT_MISSING_WORD:
                questionToReplace = questionSvc.findQuestionByType("SELECT_MISSING_WORD").get().questionContent();
                // "Query para ir buscar um SELECT_MISSING_WORD\n";
                break;
            case AutomaticExamParser.TRUE_OR_FALSE:
                questionToReplace = questionSvc.findQuestionByType("TRUE_OR_FALSE").get().questionContent();
                // "Query para ir buscar um TRUE_FALSE\n";
                break;
            default:
                throw new IllegalArgumentException("Unknown operator type: " + ctx.qType.getText());
        }
        return questionToReplace;
    }

    protected String aggregateResult(String aggregate, String nextResult) {
        return (aggregate == null) ? nextResult : aggregate + nextResult;
    }

    @Override
    public String visitTerminal(TerminalNode node) {
        return node.getText();
    }
}