grammar AutomaticExam;

examTitle: 'Exam Title-' FREE_TEXT NEWLINE examDescription ;

examDescription: 'Exam Description-' FREE_TEXT NEWLINE examHeader ;

examHeader: '[Exam Header]'  NEWLINE headerDescription ;

headerDescription: 'Header Description-' FREE_TEXT NEWLINE sectionsGroup ;

sectionsGroup: '[Sections]' NEWLINE section+ '[/Sections]' NEWLINE courseCode;

section: 'Section ' INT '-' FREE_TEXT NEWLINE examSectionDescription NEWLINE questionTypes ;

examSectionDescription: 'Section Description-' FREE_TEXT ;

questionTypes: questionType+ ;

questionType: qType=( SHORT_ANSWER
             |  MATCHING
             |  MULTIPLE_CHOICE
             |  NUMERICAL
             |  SELECT_MISSING_WORD
             |  TRUE_OR_FALSE
             ) NEWLINE;

courseCode: 'Course code-' FREE_TEXT;

INT: [0-9]+ ;
NEWLINE: [\r\n]+;
SHORT_ANSWER: '$SHORT_ANSWER$';
MATCHING: '$MATCHING$';
MULTIPLE_CHOICE: '$MULTIPLE_CHOICE$';
NUMERICAL: '$NUMERICAL$';
SELECT_MISSING_WORD: '$SELECT_MISSING_WORD$';
TRUE_OR_FALSE: '$TRUE_OR_FALSE$';

FREE_TEXT:'"' ~[\r\n"]* '"';