package peaksoft.repository.repositoryImpl;

import org.springframework.stereotype.Repository;
import peaksoft.model.Group;
import peaksoft.model.Lesson;
import peaksoft.model.Task;
import peaksoft.repository.TaskRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TaskRepositoryImpl implements TaskRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Task> getAllTasks(Long id) {
        return entityManager.createQuery("select t from Task t where t.lesson.id = :id", Task.class).setParameter("id", id).getResultList();
    }

    @Override
    public void addTask(Long id, Task task) {
        Lesson lesson = entityManager.find(Lesson.class, id);
        lesson.addTask(task);
        task.setLesson(lesson);
        entityManager.merge(lesson);
    }

    @Override
    public Task getTaskById(Long id) {
        return entityManager.find(Task.class, id);
    }

    @Override
    public void updateTask(Task task, Long id) {
        Task task1 = entityManager.find(Task.class, id);
        task1.setTaskName(task.getTaskName());
        task1.setTaskText(task.getTaskText());
        task1.setDeadLine(task.getDeadLine());
        entityManager.merge(task1);
    }

    @Override
    public void deleteTask(Long id) {
        entityManager.remove(entityManager.find(Task.class, id));
    }
}
