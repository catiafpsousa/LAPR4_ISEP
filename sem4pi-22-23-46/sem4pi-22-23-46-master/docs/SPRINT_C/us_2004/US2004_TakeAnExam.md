# US 2004 - As Student, I want to take an exam

## 1. Context

*This is the first time this functionality is being developed. It is included in Sprint C of the project eCourse.*

## 2. Requirements

### 2.1 User Story Description

**US 2004** As Student, I want to take an exam

### 2.2 Customer Specifications and Clarifications ###

**From the specifications Document:**

FRE04 - Take Exam A Student takes an exam and answer its questions. At the end of
the exam, the system should display the feedback and result (i.e., grade) of the exam. The
feedback and grade of the exam should be automatically calculated by a parser based on
the grammar defined for exams structure.

**From the client clarifications:**

> *Question_1* (Wednesday, 24 de May de 2023 às 22:45) --> A propósito das US's 2004 e 2009, de que forma é que os
> exames serão apresentados ao aluno para o mesmo os realizar? Será semelhante às boards, em que será criada uma página em
> Java Script?
>
> *Answer_1* --> Documento de especificação, página 11, Figura 4.1. É apresentada uma visão da arquitetura da solução.
> Apenas a aplicação "shared board app" implementa um servidor http para servir o "board viewer". Todas as outras
> funcionalidades da solução devem estar distribuídas pelas outras "apps", que devem ser java console applications. Ou
> seja, a "user interface" para a funcionalidade dos exames deve ser implementada como uma console application. A
> referencia aos "quiz" do moodle é apenas para ilustrar quais as funcionalidades pretendidas. Mas a sua implementação não
> necessita de ser realizada em HTML (ou seja, não é esperado que o façam).
> Pensava que isso estava claro no documento da especificação.


> *Question_2* (Wednesday, 31 de May de 2023 às 16:16) --> Our group has a following question: when a student finished
> taking exam, will he want at some point review the exam and see his answers? Do we need to save the exam with the
> answers of a certain student to be able to show it later (if needed), or should we just calculate the final grade and
> show feedback for each question (if applicable)?
> Another question is about types of feedback: on-submission means on submission of a question or the whole exam?
>
>
> *Answer_2* -->  when a student finished taking exam, will he want at some point review the exam and see his answers?
> This is not required.
> Do we need to save the exam with the answers of a certain student to be able to show it later (if needed), or should
> we just calculate the final grade and show feedback for each question (if applicable)?
> Just calculate the final grade and show feedback for each question. Unless saving of the answers is required as a
> technical solution for some other aspect of your solution you do not need to save the answers.
> Another question is about types of feedback: on-submission means on submission of a question or the whole exam?
> On submission of the whole exam.
 
 
> *Question_3* (Wednesday, 24 de May de 2023 às 15:57) --> After discussing these questions with the OT teacher, we
> would like to know your opinion about the grades.
> First we would like to know if you wish that the grades are saved in the program database. Second we would like for
> you to clarify the expected flow of both feedback and grade types
>
>
> *Answer_3* --> Regarding the first question, if you do not save the grades how do you implement the functionalities of
> FRE05 and FRE06?
> Regarding the second question, the ideia is to have something very similar to the Moodle platform. According to the
> specification "The system must also support the automatic production of feedback and grading for the answers given by
> students when they take the exam. Usually this is done at the end of the exam." So, the grade and the feedback should be
> provided only and the end of the exam. At the end of the exam, the system should display to the student the resulting
> grade of the exam as well as the grade and feedback for each question/answer.
> You may find a simple workflow on how to create moodle tests(quiz) in https://youtu.be/dCDPS7ufGuQ
> Regarding grades, each question will have points when the answer is correct. If you sum all the points form all the
> answers you will have the grade of the exam.
> Please consider only the question types that are presented in the specification document. For each question type you
> will find further details on the specifics of the grading logic.

> *Question_4* (Thursday, 30 de March de 2023 às 09:54) --> Is an exam available for all students of a certain course, or do the students of the course need to sign up to take an exam previously.
>
>
> *Answer_4* --> I think there is no "sign up" for exams. After the exam is created it should be available to all the students of that course, and all the students can/should take that exam.

> *Question_5* (Saturday, 3 de June de 2023 às 17:34) --> Relativamente à funcionalidade de realizar um exame, mais
> concretamente à parte de mostrar a nota/feedback com a propriedade "after-closing" (referenciado na especificação do
> sistema), é esperado o aluno ser notificado mal a data de fecho do exame seja atingida ou pretende-se apenas que a nota
> possa ser visualizada a partir da funcionalidade correspondente à US2005/2006.
> Caso pretenda que o aluno seja notificado, pergunto-lhe se isto deve ser feito através de uma notificação do sistema
> ou através de um serviço, por exemplo, email.
>
>
> *Answer_5* --> Não consigo encontrar nenhuma referência a uma notificação depois da data de fecho do exame. O que vejo
> é que o sistema deve mostrar o resultado e o feedback no final do exame.
> A questão que se pode colocar é quando é que acontece o "fim do exame". Podemos entender que é quando o aluno submete
> o seu exame ou quando atingimos a data de fecho do exame ("close date"). Como cliente posso indicar que aceito a
> primeira interpretação, ou seja, assim que o aluno submete o seu exame, este recebe o feedback e nota (de forma
> sincrona). Penso que em termos de solução será a situação mais simples.

> *Question_6* (Monday, 5 de June de 2023 às 15:52) --> Tendo em conta que aceita primeira interpretação, qual será a diferença entre on-submission e after-closing? A minha dúvida é principalmente acerca do funcionamento da propriedade after-closing.
>
>
> *Answer_6* --> A minha indicação anterior é a de que o aluno recebe o feedback e a nota quando submete o exame. Em principio o aluno tem de submeter o exame antes do tempo de fecho do exame. Se o aluno tentar submeter um exame depois do seu fecho o sistema deve simplesmente não aceitar a entrega/submissão.

> *Question_7* (Sunday, 4 de June de 2023 às 11:32) --> When a student takes an exam, should we assume that he has only one attempt, or the number of attempts should be specified in the exam grammar?
>
>
> *Answer_7* --> There is no mention regarding attempts in the specification, therefore there is no need to support that functionality.


### 2.3. Acceptance Criteria ###

A Student takes an exam and answer its questions.
At the end of the exam, the system should display the feedback and result (i.e., grade) of the exam.
The feedback and grade of the exam should be automatically calculated by a parser based on the grammar defined for exams structure.

### 2.4. Dependencies ###

* **US2008** - As Teacher, I want to create/update  exams

## 3. Analysis

### 3.1 Relevant Domain Model Excerpt

![DM excerpt](US2004_DM.svg "A Domain Model Excerpt")

### 3.2 System Sequence Diagram (SSD)

![SSD](./us2004_SSD.svg "A System Sequence Diagram")

## 4. Design

### 4.1. Realization (Sequence Diagram - SD)

![SD](./us2004_SD.svg "A Sequence Diagram")

### 4.2. Class Diagram (CD)

![a class diagram](./us2004_CD.svg "A Class Diagram")

### 4.3. Applied Patterns

* ENTITY as root of AGGREGATE
* VALUE OBJECT caractherizes OBJECTS
* SERVICE
* REPOSITORY
* SINGLE RESPONSABILITY

### 4.4. Tests

NA

## 5. Implementation


## 6. Integration/Demonstration

### Demonstration of the implemented functionality
![demo](TAKE_AN_EXAM.png "Demonstration")

## 7. Observations
NA