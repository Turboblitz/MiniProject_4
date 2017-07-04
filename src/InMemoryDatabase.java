import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementierung des Database-Interfaces, die Objekte über Listen des Java
 * Collection-Framework direkt im Speicher verwaltet
 */
public class InMemoryDatabase implements Database {

    private List<Student> students = new LinkedList<>();
    private List<Course> courses = new LinkedList<>();

    @Override
    public void insertStudent(Student student) {
        if (students.contains(student)) return;
        this.students.add(student);
    }

    @Override
    public void insertStudents(List<Student> students) {
        this.students.addAll(students);
    }

    @Override
    public int countStudents() {

        return this.students.size();
    }

    @Override
    public List<Student> getStudents() {
        List<Student> returnList = new ArrayList<>();
        for (Student s : students){
            returnList.add(s);
        }
        return returnList;
    }

    @Override
    public List<Student> getStudentsBornAfter(Calendar date) {
        List<Student> returnList = new ArrayList<>();
        for (Student s : this.students){
            if (s.getDateOfBirth().compareTo(date) > 0) returnList.add(s);
        }
        return returnList;
    }

    @Override
    public List<Student> getGoodStudents(double gradeThreshold) {
        List<Student> returnList = new ArrayList<>();
        for (Student s : this.students){
            if (s.getAverageGrade() < gradeThreshold) returnList.add(s);
        }
        return returnList;
    }

    @Override
    public List<Student> getGoodStudentsOrderedByGrade(double gradeThreshold) {
        List<Student> returnList = this.getGoodStudents(gradeThreshold);
        returnList.sort(new GradeComparator());
        return returnList;
    }

    @Override
    public List<Student> getStudentsAttendingCourse(Course course) {
        List<Student> returnList = new ArrayList<>();
        for (Student s : this.students){
            if (s.getAttendedCourses().contains(course)) returnList.add(s);
        }
        return returnList;
    }

    @Override
    public void insertCourse(Course course) {

        if(courses.contains(course)) return;
        this.courses.add(course);
    }

    @Override
    public List<Course> getCourses() {
        List<Course> newList = new ArrayList<>(courses);
        return newList;
    }

    @Override
    public List<Course> getCoursesInTerm(Term term) {
        List<Course> returnList = new ArrayList<>();
        for (Course c : this.courses){
            if (c.getTerm() == term) returnList.add(c);
        }
        return returnList;
    }

    public void removeCourse(Course c){
        this.courses.remove(c);
    }
    void removeAllCourses(){
        this.courses.removeAll(courses);
    }

    @Override
    public int countCourses() {

        return this.courses.size();
    }
}
