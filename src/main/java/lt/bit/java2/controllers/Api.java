package lt.bit.java2.controllers;

import lt.bit.java2.entities.Grade;
import lt.bit.java2.entities.Student;
import lt.bit.java2.mappers.GradeMapper;
import lt.bit.java2.mappers.StudentMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class Api {

    private final JdbcTemplate jdbcTemplate;
    private final StudentMapper studentMapper;
    private final GradeMapper gradeMapper;

    public Api(JdbcTemplate jdbcTemplate, StudentMapper studentMapper, GradeMapper gradeMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentMapper = studentMapper;
        this.gradeMapper = gradeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable int id) {
        try {
//            Student student = jdbcTemplate.queryForObject(
//                    "SELECT id, vardas, pavarde, el_pastas" +
//                            " FROM studentai s" +
//                            " WHERE id = ?",
//                    studentMapper,
//                    id
//            );
//
//            List<Grade> grades = jdbcTemplate.query(
//                    "SELECT * FROM pazymiai WHERE studentas_id = ?",
//                    gradeMapper,
//                    id
//            );
//
//            student.setGrades(grades);

            final Student student = new Student();
            jdbcTemplate.query(
                    "SELECT s.id AS id, vardas, pavarde, el_pastas, " +
                            "p.id AS p_id,  p.data, p.pazymys, p.studentas_id" +
                            " FROM studentai s" +
                            " LEFT JOIN pazymiai p ON p.studentas_id = s.id" +
                            " WHERE s.id = ?",
                    new Object[]{id},
                    rs -> {
                        System.out.println("dirbame...");
                        if (student.getId() == null) {
                            student.setId(rs.getInt("id"));
                            student.setFirstName(rs.getString("vardas"));
                            student.setLastName(rs.getString("pavarde"));
                            student.setEmail(rs.getString("el_pastas"));
                        }
                        int pid = rs.getInt("p_id");
                        if (pid > 0) {
                           if (student.getGrades() == null) student.setGrades(new ArrayList<>());
                            Grade grade = new Grade();
                            grade.setId(rs.getInt("p_id"));
                            grade.setStudentId(rs.getInt("studentas_id"));
                            grade.setDate(rs.getDate("data").toLocalDate());
                            grade.setGrade(rs.getInt("pazymys"));
                            student.getGrades().add(grade);
                        }
                    }
            );

            return student.getId() == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(student);

        } catch (DataAccessException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    // 1 2 - 3 4 - 5 6
    //  1     2     3
    // offset = pageSize * (pageNo - 1)
    @GetMapping
    public ResponseEntity<List<Student>> getStudentsList(
            @RequestParam(name = "s", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "n", required = false, defaultValue = "1") int pageNo) {
        if (pageSize <= 0 || pageNo <= 0) {
            return ResponseEntity.badRequest().build();
        }
        List<Student> students = jdbcTemplate.query(
                "SELECT * FROM studentai LIMIT ? OFFSET ?",
                studentMapper,
                pageSize, pageSize * (pageNo - 1));
        return ResponseEntity.ok(students);
    }
}
