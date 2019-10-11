package lt.bit.java2.mappers;

import lt.bit.java2.entities.Grade;
import lt.bit.java2.entities.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class StudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setFirstName(rs.getString("vardas"));
        student.setLastName(rs.getString("pavarde"));
        student.setEmail(rs.getString("el_pastas"));

//        int pId = rs.getInt("p_id");
//        if (pId != 0) {
//            Grade grade = new Grade();
//            grade.setId(pId);
//            grade.setStudentId(student.getId());
//            grade.setDate(rs.getDate("data").toLocalDate());
//            grade.setGrade(rs.getInt("pazymys"));
//
//            student.setGrades(new ArrayList<>());
//            student.getGrades().add(grade);
//        }

        return student;
    }
}
