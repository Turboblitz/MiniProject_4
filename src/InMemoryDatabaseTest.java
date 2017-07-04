import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryDatabaseTest {

    InMemoryDatabase database = new InMemoryDatabase();
    public static final Course TEST_COURSE = new Course("NIS", Term.SUMMER_TERM);

    @Test
    void insertStudent(){
        Student testStudent = new Student("A", "B", 1994, 10, 1);
        database.getStudents().clear();
        database.insertStudent(testStudent);
        assertTrue(database.getStudents().contains(testStudent));
    }

    @Test void insertStudents(){
        List<Student> testList = createTestListAndFill();
        assertEquals(testList, database.getStudents());
        for (Student s : database.getStudents()){
            database.insertStudent(s);
        }
        assertEquals(testList, database.getStudents());
    }

    private List<Student> createTestListAndFill() {
        List<Student> testList = new ArrayList<>();
        testList.add(new Student("A", "B", 1987, 10, 3));
        testList.add(new Student("C", "D", 1992, 4, 18));
        testList.add(new Student("E", "F", 1995, 3, 27));
        database.getStudents().clear();
        database.insertStudents(testList);
        return testList;
    }

    @Test void countStudents(){
        List<Student> testList = createTestListAndFill();
        assertEquals(testList.size(), database.countStudents());
    }

    @Test void getStudents(){
        List<Student> testList = createTestListAndFill();
        assertEquals(testList, database.getStudents());
    }

    @Test void getStudentsBornAfter(){
        List<Student> testList = createTestListAndFill();
        Calendar testdate = Calendar.getInstance();
        testdate.set(1000, 1, 1);
        List<Student> bornafter1000 = database.getStudentsBornAfter(testdate);
        assertEquals(testList, bornafter1000);
        testdate.set(1992, 4, 17);
        testList.remove(0);
        assertEquals(testList, database.getStudentsBornAfter(testdate));
        testList.remove(0);
        testdate.set(1992, 4, 19);
        assertEquals(testList, database.getStudentsBornAfter(testdate));
    }

    @Test void getGoodStudents(){
        List<Student> testList = createTestListAndFill();
        createStudentGrades(testList);
    }

    private void createStudentGrades(List<Student> testList) {
        Course testCourse = new Course("Kurs1", Term.SUMMER_TERM);
        Student test1 = testList.get(0);
        test1.setGrade(testCourse, 1.8);
        Student test2 = testList.get(1);
        test2.setGrade(testCourse, 2.4);
        Student test3 = testList.get(2);
        test3.setGrade(testCourse, 3.6);

        List<Student> betterThan24 = new ArrayList<>();
        List<Student> betterThan10 = new ArrayList<>();
        List<Student> betterThan28 = new ArrayList<>();
        betterThan24.add(test1);
        betterThan28.add(test1);
        betterThan28.add(test2);

        assertEquals(betterThan10, database.getGoodStudents(1.0));
        assertEquals(betterThan24, database.getGoodStudents(2.4));
        assertEquals(betterThan28, database.getGoodStudents(2.8));
    }

    @Test void getGoodStudentsOrderedByGrade(){
        List<Student> testList = createTestListAndFill();
        Course testCourse = new Course("Proggen", Term.WINTER_TERM);
        Student test1 = testList.get(0);
        test1.setGrade(testCourse, 2.4);
        Student test2 = testList.get(1);
        test2.setGrade(testCourse, 3.6);
        Student test3 = testList.get(2);
        test3.setGrade(testCourse, 1.8);

        List<Student> betterThan24 = new ArrayList<>();
        List<Student> betterThan10 = new ArrayList<>();
        List<Student> betterThan28 = new ArrayList<>();
        betterThan24.add(test3);
        betterThan28.add(test3);
        betterThan28.add(test1);

        assertEquals(betterThan10, database.getGoodStudentsOrderedByGrade(1.0));
        assertEquals(betterThan24, database.getGoodStudentsOrderedByGrade(2.4));
        assertEquals(betterThan28, database.getGoodStudentsOrderedByGrade(2.8));
    }

    @Test void getStudentsAttendingCourse(){
        List<Student> testList = createTestListAndFill();
        Course testCourse = new Course("SEP", Term.SUMMER_TERM);
        assertTrue(database.getStudentsAttendingCourse(testCourse).size() == 0);

        for (Student s : testList){
                s.setGrade(testCourse, 1.0);
        }
        assertEquals(testList, database.getStudentsAttendingCourse(testCourse));
        testList.get(0).getAttendedCourses().remove(testCourse);
        assertTrue(database.getStudentsAttendingCourse(testCourse).size() == 2);
    }

    @Test void insertCourse(){

        assertTrue(database.getCourses().size() == 0);
        database.insertCourse(TEST_COURSE);
        assertTrue(database.getCourses().size() == 1);
        database.insertCourse(TEST_COURSE);
        assertTrue(database.getCourses().size() == 1);
        database.removeCourse(TEST_COURSE);
    }

    @Test void getCourses(){
        List<Course> testList1 = database.getCourses();
        List<Course> testList2 = database.getCourses();
        assertEquals(testList1, testList2);
        assertTrue(testList1 != testList2);
    }

    @Test void getCoursesInTerm(){
        List<Course> courses = createCourseList();

        for (Course c : courses){
            database.insertCourse(c);
        }
        assertTrue(database.getCoursesInTerm(Term.WINTER_TERM).size() == 3);
        assertTrue(database.getCoursesInTerm(Term.SUMMER_TERM).size() == 2);
        List<Course> listWinterTerm = new ArrayList<>();
        List<Course> listSummerTerm = new ArrayList<>();
        for (Course c : courses){
            if (c.getTerm() == Term.SUMMER_TERM) listSummerTerm.add(c);
            else listWinterTerm.add(c);
        }

        assertEquals(listWinterTerm, database.getCoursesInTerm(Term.WINTER_TERM));
        assertEquals(listSummerTerm, database.getCoursesInTerm(Term.SUMMER_TERM));

        database.removeAllCourses();
    }


    @Test void countCourses(){
        List<Course> courses = createCourseList();
        database.removeAllCourses();
        assertEquals(0, database.countStudents());
        for (Course c : courses){
            database.insertCourse(c);
        }
        assertEquals(courses.size(), database.countCourses());
    }


    List<Course> createCourseList(){

        List<Course> courseList = new ArrayList<>();
        Course course1 = new Course("Kurs 1", Term.WINTER_TERM);
        Course course2 = new Course("Kurs 2", Term.SUMMER_TERM);
        Course course3 = new Course("Kurs 3", Term.WINTER_TERM);
        Course course4 = new Course("Kurs 4", Term.SUMMER_TERM);
        Course course5 = new Course("Kurs 5", Term.WINTER_TERM);

        courseList.add(course1);
        courseList.add(course2);
        courseList.add(course3);
        courseList.add(course4);
        courseList.add(course5);

        return courseList;
    }


}
