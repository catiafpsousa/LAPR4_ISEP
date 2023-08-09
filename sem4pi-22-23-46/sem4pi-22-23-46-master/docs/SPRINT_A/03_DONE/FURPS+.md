# Supplementary Specification (FURPS+)

## FUNCTIONALITY

### Auditing
- _Generate **automatic grades and feedbacks** to evaluate the Student performance._
- _Generate **reports** to analyze data from the courses, meetings and classes._
- _**Overview Dashboards** should be automatically created._

###  Authentication
- _All those who wish to use the applications must be **authenticated** with a password._
- _Application access is secured by authentication mechanism (these credentials are implemented by the manager/administrator)._ 
- _Then this authentication component leads to the respective user's authorization._

###  Communication
- _Interoperability of 4 different applications (Admin, Teacher, Student and Shared Board Apps) plus complementing APIs (SQL + Shared Board Custom) and Web Browser._
- _External business implementation of online conference/meeting software._
- _Working accordingly to the National **Education** system._

###  Error management
- _In case of synchronization error the application should ask the user to input again all the required information._
- _The manager/administrator should have major control and permission to solve errors and then restart the normal system function if necessary._
- _Conflicts must not result in data loss._

###  Event management
- _Generally, all the transactions should be sensitive to ensure events can be recovered at any step in the scenario._

###  Licensing
- _These solution **eCourse** consists of an exploratory development that will be of high value and the basis for
  how the company will evaluate the viability of the project as a possible commercial solution.
  The company set the duration of this Minimum Value Product (MVP) project as 3 months._
- _It is licensed under the rights of **Learning Systems (LS)**._
- _There is no licensing arrangement with any technology but there should be used the ones that are specified in the Design section of **+**._

###  Localisation
- _The application must support **English** language. May have possibilty to embrace **Portuguese** interface._

###  Online help
- _In case of software approval after testing, should be provided to the company the User Manual._
- _When representing a commercial solution, team support must be provided._

###  Persistence

- _The application should use Object Relational Mapping (ORM) to ensure **data persistence** betwen two runs of the apliccation. It will be used a SQL database, with Java Persistence API (JPA)._

###  Printing
- _The console user interface should print the menus and interaction with the system._
- _Success/Error messages should be printed to alert the user about the program flux._

###  Reporting

- _Perform the checking of the board/meeting maximum capacity_
- _Allow the issuance of automatic grades for questions and exams (consequently for the student average for the course)_
- _Possibly perform notifications by email_
- _Allow viewing the Board and also allow consulting the Board History_
- _Allow checking the availability of all users that are invited to a meeting_
- _Generate **statistics** and **charts** to evaluate the student/course performance - DASHBOARD_
- _Approve or reject the enlistment in a course_
- _List several business data: boards, courses, exams, grades and meeting participants_


###  Scheduling
- _Allow the scheduling of a meeting._
- _Allow the scheduling of a class._
- _It is important to notice that to allow the schedule of the class the Teacher must be available for the class period_
- _In the same way, the system must check if all the participants are available to send the invitations for the meeting_



###  Security
- _All those who wish to use the application must be **authenticated**_
- _Only the **Manager/Administator** can **configure** and manage the core information_
- _According to the Authentication that leads to respective Authorization, the user can perform only the allowed functionalities_

### Suitability

_The capability of the software product to provide an appropriate set of functions for specified tasks and user objectives._

_Functional Requirements:_

**--> USER - GENERAL**
- _Create Board_                                                           
- _Share Board_                                                           
- _View Board_                                                             
- _Create Post-it_                                                         
- _Change Post-it_                                                         
- _Undo Post-it Change_                                                   
- _View Board History_
- _Archive Board_
- _Schedule a Meeting_
- _Cancel Meeting_
- _Accept/Reject Meeting_
- _List Participants_

**--> MANAGER/ADMINISTRATOR**
- _Management of Users_
- _Create Course_
- _Open/Close Enrollments in Course_ 
- _Open/Close Course_
- _Set Course Teachers_
- _Bulk Enroll Students in Course_
- _Approve/Reject Enlistment in Course_

**--> TEACHER**
- _Schedule of Class_
- _Schedule of Extra Class_
- _Update Schedule of Class_
- _Create Exam_
- _List Course Exams_ 
- _List Course Grades_                                                     
     
 **--> STUDENT**
- _List Exams_
- _Take Exam_
- _List Grades_
- _Request Enrollment in Course_



###  Transaction management
- _Generally, all the transactions should be sensitive to ensure events can be recovered at any step in the scenario._
- _In case of synchronization error the application should ask the user to input again all the required information._
###  Workflow
- _Login_

loop:
- _Select Functionality_
- _Input Data + Confirm Data_
- _Consult Data_

___________________________________________________________________________

## USABILITY

Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards.


### Accessibility
- _The application must have a console user interface._
- _To facilitate the access to list data the application must **allow sorting** by some criteria (FIFO, ascending, descending, etc...)_
- _Graphical Board API_

### Aesthetics
- _Simple and intuitive UI_

### Consistency
- _Menus must follow the same structure to facilitate workflow and learning curve_

### Help and documentation
- _In case of software approval after testing, should be provided to the company the User Manual._
- _Development team should design all the functional requirements to assist maintenance._
- _Functionalities and workflow must be documented._

___________________________________________________________________________

## RELIABILITY

Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures.

### Accuracy
- _N/A._

### Availability
- _N/A._

### Recoverability
- _N/A._


## PERFORMANCE

Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

### Recovery time
- _N/A._

### Response time
- _N/A._

### Shutdown time
- _N/A._

### Start-up time
- _N/A._

### Throughput
- _N/A._




## SUPPORTABILITY

### Adaptability
- _N/A._
### Auditability
- _N/A._
### Compatibility
- _The application must be adaptable to different environments and operating systems without compatibility problems nor limitations in its installability._
### Configurability
- _The program must be **well modularized** to allow specified changes and stable to avoid unexpected effects from the modifications._
### Installability
- _The application should run on all platforms for which there exists a **Java Virtual Machine** and a Web Browser._
### Localizability
- _N/A._
### Maintainability
- _It’s important the process of analyzing the product to **diagnose deficiencies** in the code or in the execution of some functionalities. The identification of the parts to be modified should be efficient and simple._
- _The program must be **well modularized** to allow specified changes and stable to avoid unexpected effects from the modifications._
### Scalability
- _Designed to easily support operating in large educational institution requiring a massive ammount of events and reports_
- _The software application should be conceived having in mind that it can be further commercialized._
### Testability
- _The development team must **implement unit tests** for all methods, except for methods that implement Input/Output operations. Unit tests are crucial to validate modifications and to ensure the accuracy of the system._
- _All the **tests and documentation** will provide tools to allow the easy understanding of the application. Therefore it can be modified by every future developer with pertinent support._

## +

### Design Requirements

Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc.

- _The application must be developed in **Java**._
- _Adopt best practices for identifying requirements and for OO software analysis and design._
- _Test Driven Design should be the development approach._
- _Adopt recognized coding standards (e.g., CamelCase)._
- _Use **Javadoc** to generate useful documentation for Java code._
- _The units tests should be implemented using the **JUnit 4 framework**._
- _The **JaCoCo plugin** should be used to generate the coverage report._
- _All the images/figures produced during the software development process should be recorded in **SVG format**._
- _JPA framework allows to persist the objects to the database._
- _Use of AJAX Custom Webpage to create and define the interface to Shared Boards._

### Implementation Requirements

Specifies or constraints the code or construction of a system 
such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system.

#### 3rd party components
- _H2 database_
- _AJAX custom webpage_

#### Implementation languages
- _The application must be developed in **Java** using JPA to persist data in H2 database._

#### Platform support
- _The application should run on all platforms for which there exists a **Java Virtual Machine**._
- _Network use and concurrent acess to data._

#### Resource limits
- _N/A._

#### Standards-compliance
- _Adopt recognized coding standards (e.g., CamelCase)._
- _OOD (Object-Oriented Design) must be implemented._
- _DDD (Domain-Driven Design) must be implemented._
- _Test Driven Development (TDD)._
- _Bootstrap to load the initial run of the application._

### Interface Requirements

#### External systems
- _N/A._
- 
#### Interface formats
- _N/A._

### Physical Requirements

#### Shape
- _N/A._
#### Size
- _N/A._
#### Weight
- _N/A._
