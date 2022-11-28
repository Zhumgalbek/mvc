package peaksoft.repository.repositoryImpl;

import org.springframework.stereotype.Repository;
import peaksoft.model.*;
import peaksoft.repository.GroupRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public class GroupRepositoryImpl implements GroupRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Group> getAllGroup(Long id) {
        return entityManager.createQuery("select g from Group g where g.company.id = :id", Group.class).setParameter("id", id).getResultList();
    }

    @Override
    public List<Group> getAllGroupsByCourseId(Long courseId) {
        List<Group> groupList = entityManager.find(Course.class, courseId).getGroups();
        groupList.forEach(System.out::println);
        return groupList;
    }

    @Override
    public void addGroup(Long id, Group group) {
        Company company = entityManager.find(Company.class, id);
        company.addGroup(group);
        group.setCompany(company);
        entityManager.merge(group);
    }

    @Override
    public void addGroupByCourseId(Long id, Long courseId, Group group) {
        Company company = entityManager.find(Company.class, id);
        Course course = entityManager.find(Course.class, courseId);

        company.addGroup(group);
        group.setCompany(company);
        group.addCourse(course);
        course.addGroup(group);

        entityManager.merge(course);
        entityManager.merge(company);
    }


    @Override
    public Group getGroupById(Long id) {
        return entityManager.find(Group.class, id);
    }

    @Override
    public void updateGroup(Group group, Long id) {
        Group group1 = getGroupById(id);
        group1.setGroupName(group.getGroupName());
        group1.setDateOfStart(group.getDateOfStart());
        group1.setImage(group.getImage());
        entityManager.merge(group1);
    }

    @Override
    public void deleteGroup(Long id) {
        Group group = entityManager.find(Group.class, id);
        for (Student s: group.getStudents()) {
            group.getCompany().minusStudent();
        }

        for (Course c: group.getCourses()) {
            for (Student student: group.getStudents()) {
                for (Instructor i: c.getInstructors()) {
                    i.minus();
                }
            }
        }

        for (Course c : group.getCourses()) {
            c.getGroups().remove(group);
            group.minusCount();
        }
        group.getStudents().forEach(x -> entityManager.remove(x));
        group.setCourses(null);
        entityManager.remove(group);
    }

    @Override
    public void assignGroup(Long courseId, Long groupId) throws IOException {
        Group group = entityManager.find(Group.class, groupId);
        Course course = entityManager.find(Course.class, courseId);
        if (course.getGroups()!=null){
            for (Group g : course.getGroups()) {
                if (g.getId() == groupId) {
                    throw new IOException("This group already exists!");
                }
            }
        }

        if (course.getInstructors() != null) {
            for (Instructor i: course.getInstructors()) {
                for (Student s: group.getStudents()) {
                    i.plus();
                }
            }
        }

        group.addCourse(course);
        course.addGroup(group);
        entityManager.merge(group);
        entityManager.merge(course);
    }
}
