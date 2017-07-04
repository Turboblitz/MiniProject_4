import java.util.Comparator;

/**
 * Vergleicht zwei Student-Objekte miteinander 
 */
public class GradeComparator implements Comparator<Student> {

	@Override
	public int compare(Student s, Student t) {
		double s_average = s.getAverageGrade();
		double t_average = t.getAverageGrade();

		if (s_average-t_average == 0) return 0;
		else if (s_average - t_average < 0) return -1;
		else return 1;
	}
}
