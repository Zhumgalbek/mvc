package peaksoft.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import peaksoft.model.Lesson;
import peaksoft.repository.LessonRepository;
import peaksoft.service.LessonService;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonService;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonService) {
        this.lessonService = lessonService;
    }

    @Override
    public List<Lesson> getAllLessons(Long id) {
        return lessonService.getAllLessons(id);
    }

    @Override
    public void addLesson(Long id, Lesson lesson) {
        lessonService.addLesson(id,lesson);
    }

    @Override
    public Lesson getLessonById(Long id) {
        return lessonService.getLessonById(id);
    }

    @Override
    public void updateLesson(Lesson lesson, Long id) {
        lessonService.updateLesson(lesson,id);
    }

    @Override
    public void deleteLesson(Long id) {
        lessonService.deleteLesson(id);
    }
}
