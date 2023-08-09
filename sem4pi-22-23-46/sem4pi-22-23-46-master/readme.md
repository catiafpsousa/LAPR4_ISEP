# Project eCourse

            _____                               
           / ____|                              
      ___ | |      ___   _   _  _ __  ___   ___
     / _ \| |     / _ \ | | | || '__|/ __| / _ \
    |  __/| |____| (_) || |_| || |   \__ \|  __/
     \___| \_____|\___/  \__,_||_|   |___/ \___|

## 1. Description of the Project

This project consists of developing a new remote learning platform called eCourse, using a minimum viable product (MVP)
approach.
The project enhances the integration and application of knowledge, skills and competencies of all course units taught
through the fourth semester of Degree in Informatics Engineering of ISEP in 2022/2023 academic year: 
Applications Engineering (EAPLI), Laboratory and Project IV (LAPR4),
Languages and Programming (LPROG), Computer Networks (RCOMP) and Computer Systems
(SCOMP).


## 2. Planning and Technical Documentation

Planning and Technical Documentation can be found in the /docs folder. 

## 3. How to Build

Make sure Maven is installed and on the PATH.

The java source is Java 1.8+ so any JDK 1.8 or later will work. However, in order to generate the javadoc and UML diagrams the JDK version must be *strictly 1.8*.

If using an Oracle database, you will need to change your maven settings for
downloading the Oracle drivers. see <https://blogs.oracle.com/dev2dev/entry/how_to_get_oracle_jdbc#settings> for more information.

run script

    rebuild-all.bat

## 4. How to Run

make sure a JRE is installed and on the PATH

to execute  **MANAGER** related functionalities run script:

    run-backoffice

to execute **TEACHER** related functionalities run script:

    run-teacher

to execute **STUDENT** related functionalities run script:

    run-student

## 5. How to Install/Deploy into Another Machine (or Virtual Machine)

Ensure Java is installed on the target machine: You will need to have Java Runtime Environment (JRE) installed on the target machine in order to run the .jar file. The JRE version should be compatible with the Java version used to compile the .jar file. If Java is not installed, you can download it from the official Oracle website or install it using the package manager if it's a Linux machine.

Preferably, transfer the whole application folder to the target machine. Alternatively, transfer the content of the /target folder of each application [BackOffice, Student and Teacher] to separate folders on the target machine. 

Follow previous point [4. How to Run] steps.



