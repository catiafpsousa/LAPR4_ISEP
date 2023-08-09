grammar Answers;

sectionsGroup: '[Sections]' NEWLINE section+ '[/Sections]' NEWLINE courseCode ;

section: 'Section ' INT '-' FREE_TEXT NEWLINE examSectionDescription;

examSectionDescription: 'Section Description-' FREE_TEXT NEWLINE questions+;

questions:  shortAnswers
            | matching
            | multipleChoice
            | numerical
            | trueOrFalse
            | selectMissingWord
            ;


shortAnswers: SHORT_ANSWER NEWLINE score NEWLINE saQuestion saAnswer;
saQuestion: 'Question-' FREE_TEXT NEWLINE;
saAnswer: 'Answer:' SPACES? NEWLINE (FREE_TEXT NEWLINE)?;


matching: MATCHING NEWLINE score NEWLINE matchQuestion ;
matchQuestion: 'Question-' FREE_TEXT NEWLINE matchOptions answerInstructions matchAnswer;
matchOptions: matchLeftColumn matchRightColumn;
matchLeftColumn: 'Left_column:' NEWLINE matchOption+;
matchRightColumn: 'Right_column:' NEWLINE matchOption+;
matchOption: FREE_TEXT NEWLINE;
matchAnswer: 'Answer:' SPACES? NEWLINE matchAnswerOptions*;
matchAnswerOptions: matchAnswerText NEWLINE;
matchAnswerText: FREE_TEXT '--' FREE_TEXT;


multipleChoice: MULTIPLE_CHOICE NEWLINE score NEWLINE mcQuestion;
mcQuestion: 'Question-' FREE_TEXT NEWLINE mcOptions answerInstructions mcAnswer;
mcOptions: mcLine+;
mcAnswer: 'Answer:' SPACES? NEWLINE mcLine*;
mcLine: FREE_TEXT NEWLINE;


numerical:NUMERICAL NEWLINE score NEWLINE numQuestion nAnswer;
numQuestion: 'Question-' FREE_TEXT NEWLINE ;
nAnswer:'Answer:' SPACES? NEWLINE (number NEWLINE)?;


//Retirado de: https://docs.moodle.org/401/en/Select_missing_words_question_type
// - All gaps are weighted identically
// - At runtime when the hints are exhausted the question will finish and the student
// will be given the >>>general feedback<<< and the >>>question score will be calculated<<<.
selectMissingWord:SELECT_MISSING_WORD NEWLINE score NEWLINE mwLine+;
mwLine: mwStartWithAnswer | mwStartWithText; //Validações a baixo foram criadas para validar que: -Existe pelo menos uma missing word. -A frase pode acabar ou com texto ou com uma missing word. -Não pode haver dois free text seguidos nem duas missing words seguidas.
mwStartWithAnswer: mwAnswer mwContentReadyText;
mwStartWithText: FREE_TEXT mwContentReadyAnswer;
mwContentReadyText: FREE_TEXT (NEWLINE | mwContentReadyAnswer)?;
mwContentReadyAnswer: mwAnswer (NEWLINE | mwContentReadyText)?;
mwAnswer: SPACES? 'Answer:' SPACES? (oneWordOrNumber SPACES?)?; //Formato da missing word


trueOrFalse:TRUE_OR_FALSE NEWLINE score NEWLINE tfQuestion+;
tfQuestion: 'Question-' FREE_TEXT NEWLINE tfAnswer;
tfAnswer: 'Answer:' SPACES? NEWLINE (trueOrFalseAnswer NEWLINE)?;
trueOrFalseAnswer: (TRUE_ANSWER | FALSE_ANSWER);

courseCode: 'Course code-' FREE_TEXT;

score: 'score-' number;
number: (INT | DOUBLE);
oneWordOrNumber: (INT | DOUBLE | WORD);
answerInstructions: ANSWER_INSTRUCTIONS SPACES? NEWLINE;

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
SPACES: [ \t]+;

FEEDBACK_NONE: '$NONE$';
FEEDBACK_ONSUBMISSION: '$ONSUBMISSION$';
FEEDBACK_AFTERCLOSING: '$AFTERCLOSING$';
//FREE_TEXT:'"' ('a'..'z' | ' ' | 'A'..'Z'| '.'| ','| INT |'?'|'/'|'!'|'’'|'"'|'+'|';'|'<'|'>'|'('|')'|'=')+ '"';
FREE_TEXT:'"' ~[\r\n"]* '"';
ANSWER_INSTRUCTIONS: '(' ~[)]* ')';
WORD: [a-zA-Z\u00C0-\u00ff]+;
