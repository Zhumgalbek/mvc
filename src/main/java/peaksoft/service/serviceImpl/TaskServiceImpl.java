package peaksoft.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import peaksoft.model.Task;
import peaksoft.repository.TaskRepository;
import peaksoft.service.TaskService;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskService) {
        this.taskService = taskService;
    }

    @Override
    public List<Task> getAllTasks(Long id) {
        return taskService.getAllTasks(id);
    }

    @Override
    public void addTask(Long id, Task task) {
        taskService.addTask(id,task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskService.getTaskById(id);
    }

    @Override
    public void updateTask(Task task, Long id) {
        taskService.updateTask(task,id);
    }

    @Override
    public void deleteTask(Long id) {
        taskService.deleteTask(id);
    }
}
