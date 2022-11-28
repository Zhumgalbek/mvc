package peaksoft.repository.repositoryImpl;

import org.springframework.stereotype.Repository;
import peaksoft.model.Course;
import peaksoft.model.Group;
import peaksoft.model.Instructor;
import peaksoft.model.Student;
import peaksoft.repository.InstructorRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Repository
@Transactional
public class InstructorRepositoryImpl implements InstructorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Instructor> getAllList() {
        return entityManager.createQuery("from Instructor", Instructor.class).getResultList();
    }

    @Override
    public List<Instructor> getAllInstructor(Long courseId) {
        return entityManager.createQuery("select g from Instructor g where g.course.id = :id", Instructor.class).setParameter("id", courseId).getResultList();
    }

    @Override
    public void addInstructor(Long id, Instructor instructor) throws IOException {
        Course course = entityManager.find(Course.class, id);

        if (course.getGroups()!=null){
            for (Group group : course.getGroups()) {
                for (Student student: group.getStudents()) {
                    instructor.plus();
                }
            }
        }

        course.addInstructors(instructor);
        instructor.setCourse(course);
        entityManager.merge(course);

    }

    @Override
    public Instructor getInstructorById(Long id) {
        return entityManager.find(Instructor.class, id);
    }

    @Override
    public void updateInstructor(Instructor instructor, Long id) throws IOException {
        Instructor instructor1 = entityManager.find(Instructor.class, id);
        instructor1.setFirstName(instructor.getFirstName());
        instructor1.setLastName(instructor.getLastName());
        instructor1.setEmail(instructor.getEmail());
        instructor1.setSpecialization(instructor.getSpecialization());
        instructor1.setPhoneNumber(instructor.getPhoneNumber());
        entityManager.merge(instructor1);
    }

    @Override
    public void deleteInstructor(Long id) {
        entityManager.remove(entityManager.find(Instructor.class, id));
    }

    @Override
    public void assignInstructor(Long courseId, Long instructorId) throws IOException {
        Instructor instructor = entityManager.find(Instructor.class, instructorId);
        Course course = entityManager.find(Course.class, courseId);
        if (course.getInstructors()!=null){
            for (Instructor g : course.getInstructors()) {
                if (g.getId() == instructorId) {
                    throw new IOException("This instructor already exists!");
                }
            }
        }
        for (Group g:instructor.getCourse().getGroups()) {
            for (Student s:g.getStudents()) {
                instructor.minus();
            }
        }
        for (Group g: course.getGroups()) {
            for (Student s:g.getStudents()) {
                instructor.plus();
            }
        }
        instructor.getCourse().getInstructors().remove(instructor);
        instructor.setCourse(course);
        course.addInstructors(instructor);
        entityManager.merge(instructor);
        entityManager.merge(course);
    }
}
