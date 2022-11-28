package peaksoft.repository.repositoryImpl;

import org.springframework.stereotype.Repository;
import peaksoft.model.Course;
import peaksoft.model.Group;
import peaksoft.model.Instructor;
import peaksoft.model.Student;
import peaksoft.repository.StudentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Repository
@Transactional
public class StudentRepositoryImpl implements StudentRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Student> getAllListStudent() {
        return manager.createQuery("from Student", Student.class).getResultList();
    }

    @Override
    public List<Student> getAllStudents(Long id) {
        return manager.createQuery("select s from Student s where s.groups.id = :id", Student.class).setParameter("id", id).getResultList();
    }

    @Override
    public void addStudent(Long id, Student student) {
        Group group = manager.find(Group.class, id);
        group.addStudent(student);
        student.setGroups(group);
        manager.merge(student);
        for (Course c:student.getGroups().getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.plus();
            }
        }
    }

    @Override
    public Student getStudentById(Long id) {
        return manager.find(Student.class, id);
    }

    @Override
    public void updateStudent(Student student, Long id) {
        Student student1 = manager.find(Student.class, id);
        student1.setFirstName(student.getFirstName());
        student1.setLastName(student.getLastName());
        student1.setPhoneNumber(student.getPhoneNumber());
        student1.setEmail(student.getEmail());
        student1.setStudyFormat(student.getStudyFormat());
        manager.merge(student1);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = manager.find(Student.class, id);
        student.getGroups().getCompany().minusStudent();
        for (Course c:student.getGroups().getCourses()) {
            for (Instructor i:c.getInstructors()) {
                i.minus();
            }
        }
        student.setGroups(null);
        manager.remove(student);
    }

    @Override
    public void assignStudent(Long groupId, Long studentId) throws IOException {
        Student student = manager.find(Student.class, studentId);
        Group group = manager.find(Group.class, groupId);
        if (group.getStudents()!=null){
            for (Student g : group.getStudents()) {
                if (g.getId() == studentId) {
                    throw new IOException("This student already exists!");
                }
            }
        }
        for (Course c: student.getGroups().getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.minus();
            }
        }
        for (Course c: group.getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.plus();
            }
        }
        student.getGroups().getStudents().remove(student);
        group.assignStudent(student);
        student.setGroups(group);
        manager.merge(student);
    }
}
