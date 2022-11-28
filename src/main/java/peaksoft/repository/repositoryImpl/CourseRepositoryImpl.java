package peaksoft.repository.repositoryImpl;

import org.springframework.stereotype.Repository;
import peaksoft.model.Company;
import peaksoft.model.Course;
import peaksoft.model.Group;
import peaksoft.model.Instructor;
import peaksoft.repository.CourseRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Repository
@Transactional
public class CourseRepositoryImpl implements CourseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Course> getAllCourses(Long id) {
        return entityManager.createQuery("select c from Course c where c.company.id = :id",
                Course.class).setParameter("id", id).getResultList();
    }

    @Override
    public void addCourse(Long id, Course course) throws IOException {
        Company company=entityManager.find(Company.class,id);
        company.addCourse(course);
        course.setCompany(company);
        entityManager.merge(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return entityManager.find(Course.class, id);
    }

    @Override
    public void updateCourse(Course course, Long id) throws IOException {
        Course course1 = entityManager.find(Course.class,id);
        course1.setCourseName(course.getCourseName());
        course1.setDescription(course.getDescription());
        course1.setDuration(course.getDuration());
        entityManager.merge(course1);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = entityManager.find(Course.class, id);
        for (Group g: course.getGroups()) {
            g.minusCount();
            g.getCourses().remove(course);
        }
        course.setCompany(null);
        entityManager.remove(course);
    }
}
