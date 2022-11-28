package peaksoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import peaksoft.model.Lesson;
import peaksoft.service.LessonService;

@Controller
public class LessonController {
    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/lessons/{id}")
    public String getAllLessons(@PathVariable Long id, Model model){
        model.addAttribute("lessons", lessonService.getAllLessons(id));
        model.addAttribute("courseId", id);
        return "/lesson/lessons";
    }

    @GetMapping("/getLessonById/{id}")
    public String getLessonById(@PathVariable("id") Long id, Model model){
        model.addAttribute("lesson", lessonService.getLessonById(id));
        return "/lesson/lessons";
    }

    @GetMapping("/lessons/{id}/addLesson")
    public String addLesson(@PathVariable Long id, Model model){
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("courseId", id);
        return "/lesson/addLesson";
    }

    @PostMapping("/{id}/saveLesson")
    public String saveLesson(@ModelAttribute("lesson") Lesson lesson,
                             @PathVariable Long id){
        lessonService.addLesson(id, lesson);
        return "redirect:/lessons/" + id;
    }

    @GetMapping("/updateLesson/{id}")
    public String updateLesson(@PathVariable("id") Long id, Model model) {
        Lesson lesson = lessonService.getLessonById(id);
        model.addAttribute("lesson", lesson);
        model.addAttribute("courseId", lesson.getCourse().getId());
        return "/lesson/updateLesson";
    }

    @PostMapping("/{courseId}/{id}/saveUpdateLesson")
    public String saveUpdateLesson(@PathVariable("courseId") Long courseId,
                                   @PathVariable("id") Long id,
                                   @ModelAttribute("lesson") Lesson lesson){
        lessonService.updateLesson(lesson, id);
        return "redirect:/lessons/" + courseId;
    }

    @GetMapping("/{courseId}/{id}/deleteLesson")
    public String deleteLesson(@PathVariable("id") Long id, @PathVariable("courseId") Long courseId) {
        lessonService.deleteLesson(id);
        return "redirect:/lessons/" + courseId;
    }
}
