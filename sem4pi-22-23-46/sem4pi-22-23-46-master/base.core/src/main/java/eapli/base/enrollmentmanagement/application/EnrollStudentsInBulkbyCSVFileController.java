package eapli.base.enrollmentmanagement.application;


import eapli.base.coursemanagement.application.CourseManagementService;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.csv.CSVReader;
import eapli.base.infrastructure.authz.application.AuthzRegistry;


import java.io.IOException;
import java.util.*;


/**
 *
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

public class EnrollStudentsInBulkbyCSVFileController {

    private final EnrollmentManagementService enrollmentSvc = AuthzRegistry.enrollmentService();
    private final CourseManagementService courseSvc = AuthzRegistry.courseService();
    private List<Course> allCourses = (List<Course>) courseSvc.allCourses();

    /**
     * Calls the readCsvUsersFile method passing the filename
     *
     * @param filename name of the file that will be read
     * @throws IOException If the file does not exist or is corrupted
     */
    public List<String[]> readCsvFile(String filename) throws IOException {
        CSVReader.tryReadFile(filename);
        List<String[]> enrollmentList = CSVReader.readCsvStudentFile(filename);
        return enrollmentList;
    }

    /**
     * Reads and records in the system all information relevant to SNS Users
     *
     * @param
     */
    public void createEnrollmentFromList(List<String[]> allData) {
        int line = 1;
        for (String[] userInfo : Objects.requireNonNull(allData)) {
            enrollmentSvc.createEnrollmentfromParameters(userInfo, line, this.allCourses);
            line++;
        }
    }
}
