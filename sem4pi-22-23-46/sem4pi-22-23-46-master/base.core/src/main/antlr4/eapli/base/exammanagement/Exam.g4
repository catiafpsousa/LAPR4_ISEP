grammar Exam;
prog_exam: examTitle;

prog_question: questionRepo;

examTitle: 'Exam Title-' FREE_TEXT NEWLINE examDescription;

examDescription: 'Exam Description-' FREE_TEXT NEWLINE openDate ;

openDate: 'Open Date-' DATE NEWLINE closeDate ;

closeDate: 'Close Date-' DATE NEWLINE examHeader ;

examHeader: '[Exam Header]' NEWLINE headerDescription ;

headerDescription: 'Header Description-' FREE_TEXT NEWLINE feedbackType ;

feedbackType: 'FeedbackType-' type=(FEEDBACK_NONE | FEEDBACK_ONSUBMISSION | FEEDBACK_AFTERCLOSING) NEWLINE gradeType ;

gradeType: 'GradeType-' type=(FEEDBACK_NONE | FEEDBACK_ONSUBMISSION | FEEDBACK_AFTERCLOSING) NEWLINE sectionsGroup ;

sectionsGroup: '[Sections]' NEWLINE section+ '[/Sections]' NEWLINE courseCode ;

section: 'Section ' INT '-' FREE_TEXT NEWLINE examSectionDescription;

examSectionDescription: 'Section Description-' FREE_TEXT NEWLINE questions+;

questionRepo: '[Questions_Repo]' NEWLINE questions+ '[/Questions_Repo]';

questions:  shortAnswers
            | matching
            | multipleChoice
            | numerical
            | trueOrFalse
            | selectMissingWord
            ;

shortAnswers: SHORT_ANSWER NEWLINE score NEWLINE saQuestion saCorrectAnswer;
saQuestion: 'Question-' FREE_TEXT NEWLINE ;
saCorrectAnswer:  FREE_TEXT NEWLINE ;

matching: MATCHING NEWLINE score NEWLINE matchQuestion ;
matchQuestion: 'Question-' FREE_TEXT NEWLINE matchOptions matchSolution;
matchOptions: matchLeftColumn matchRightColumn;
matchLeftColumn: 'Left_column:' NEWLINE matchOption+;
matchRightColumn: 'Right_column:' NEWLINE matchOption+;
matchOption: FREE_TEXT NEWLINE;
matchSolution: 'Solution:' NEWLINE matchAnswer+;
matchAnswer: FREE_TEXT '--' FREE_TEXT NEWLINE;

multipleChoice: MULTIPLE_CHOICE NEWLINE score NEWLINE mcQuestion;
mcQuestion: 'Question-' FREE_TEXT NEWLINE options;
options: mcWrongAnswer* mcRightAnswer (mcWrongAnswer | mcRightAnswer)+;
mcWrongAnswer: FREE_TEXT NEWLINE;
mcRightAnswer: '*' FREE_TEXT NEWLINE;

numerical: NUMERICAL NEWLINE score NEWLINE numQuestion;
numQuestion: 'Question-' FREE_TEXT NEWLINE numAnswer;
numAnswer: number NEWLINE;

//Retirado de: https://docs.moodle.org/401/en/Select_missing_words_question_type
// - All gaps are weighted identically
// - At runtime when the hints are exhausted the question will finish and the student
// will be given the >>>general feedback<<< and the >>>question score will be calculated<<<.
selectMissingWord: SELECT_MISSING_WORD NEWLINE score NEWLINE mwLine+;
mwLine: mwStartWithAnswer | mwStartWithText; //Validações a baixo foram criadas para validar que: -Existe pelo menos uma missing word. -A frase pode acabar ou com texto ou com uma missing word. -Não pode haver dois free text seguidos nem duas missing words seguidas.
mwStartWithAnswer: mwAnswer mwContentReadyText;
mwStartWithText: FREE_TEXT mwContentReadyAnswer;
mwContentReadyText: FREE_TEXT (NEWLINE | mwContentReadyAnswer)?;
mwContentReadyAnswer: mwAnswer (NEWLINE | mwContentReadyText)?;
mwAnswer: '$' oneWordOrNumber '$'; //Formato da missing word

trueOrFalse: TRUE_OR_FALSE NEWLINE score NEWLINE tfQuestion+;
tfQuestion: 'Question-' FREE_TEXT NEWLINE tfAnswer;
tfAnswer: (TRUE_ANSWER | FALSE_ANSWER) NEWLINE;

courseCode: 'Course code-' FREE_TEXT;

score: 'score-' number;
number: (INT | DOUBLE);
oneWordOrNumber: (INT | DOUBLE | WORD);

SHORT_ANSWER:'**SHORT_ANSWER**';
MATCHING:'**MATCHING**';
MULTIPLE_CHOICE:'**MULTIPLE_CHOICE**';
NUMERICAL:'**NUMERICAL**';
SELECT_MISSING_WORD:'**SELECT_MISSING_WORD**';
TRUE_OR_FALSE:'**TRUE_OR_FALSE**';

TRUE_ANSWER:'TRUE';
FALSE_ANSWER:'FALSE';

DATE: ([1-9]|'0'[1-9]|[12][0-9]|'3'[01])'/'([1-9]|'0'[1-9]|'1'[012])'/'('19'|'20')[0-9][0-9];
INT: [0-9]+;
DOUBLE: ('0'|([1-9][0-9]*))('.'[0-9]+)? ;
NEWLINE: [\r\n]+ ;

FEEDBACK_NONE: '$NONE$';
FEEDBACK_ONSUBMISSION: '$ONSUBMISSION$';
FEEDBACK_AFTERCLOSING: '$AFTERCLOSING$';
//FREE_TEXT:'"' ('a'..'z' | ' ' | 'A'..'Z'| '.'| ','| INT |'?'|'/'|'!'|'’'|'"'|'+'|';'|'<'|'>'|'('|')'|'=')+ '"';
FREE_TEXT:'"' ~[\r\n"]* '"';
WORD: [a-zA-Z\u00C0-\u00ff]+;
