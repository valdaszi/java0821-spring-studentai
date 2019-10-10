package lt.bit.java2.mappers;

import lt.bit.java2.entities.Grade;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class GradeMapper implements RowMapper<Grade> {
    @Override
    public Grade mapRow(ResultSet rs, int rowNum) throws SQLException {
        Grade grade = new Grade();
        grade.setId(rs.getInt("id"));
        grade.setStudentId(rs.getInt("student_id"));
        grade.setDate(rs.getDate("data").toLocalDate());
        grade.setGrade(rs.getInt("pazymys"));
        return grade;
    }
}
