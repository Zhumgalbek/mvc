package peaksoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import peaksoft.model.Instructor;
import peaksoft.service.CourseService;
import peaksoft.service.InstructorService;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class InstructorController {

    private final InstructorService instructorService;
    private final CourseService courseService;

    @Autowired
    public InstructorController(InstructorService instructorService, CourseService courseService) {
        this.instructorService = instructorService;
        this.courseService = courseService;
    }


    @GetMapping("/instructors/{id}")
    public String getAllInstructors(@PathVariable Long id, Model model) {
        System.out.println("getAllInstructorController");
        model.addAttribute("instructorss", instructorService.getAllInstructor(id));
        model.addAttribute("courseId",id);
        model.addAttribute("companyId",courseService.getCourseById(id).getCompany().getId());
        return "/instructor/instructors";
    }


    @GetMapping("/getInstructor/{id}")
    public String getInstructorById(@PathVariable("id") Long id, Model model) {
        System.out.println("getInstructorByIdController");
        model.addAttribute("instructors", instructorService.getInstructorById(id));
        return "/instructor/instructors";
    }

    @GetMapping("/instructors/{id}/addInstructor")
    public String addInstructor(@PathVariable Long id, Model model) {
        System.out.println("addInstructorController");
        model.addAttribute("instructor", new Instructor());
        model.addAttribute("courseId", id);
        return "/instructor/addInstructor";
    }

    @PostMapping("/{id}/saveInstructors")
    public String saveInstructor(@ModelAttribute("instructor") Instructor instructor, @PathVariable Long id) throws IOException {
        instructorService.addInstructor(id, instructor);
        return "redirect:/instructors/" + id;
    }

    @GetMapping("/updateInstructor/{id}")
    public String updateInstructor(@PathVariable("id") Long id, Model model) {
        Instructor instructor = instructorService.getInstructorById(id);
        model.addAttribute("instructor", instructor);
        model.addAttribute("courseId", instructor.getCourse().getId());
        return "/instructor/updateInstructor";
    }


    @PostMapping("/{courseId}/{id}/updateInstructor")
    public String saveUpdateInstructor(@PathVariable("courseId") Long courseId,
                                       @PathVariable("id") Long id,
                                       @ModelAttribute("instructor") Instructor instructor) throws IOException {
        instructorService.updateInstructor(instructor, id);
        return "redirect:/instructors/" + courseId;
    }


    @GetMapping("/{courseId}/{id}/deleteInstructor")
    public String deleteInstructor(@PathVariable("id") Long id, @PathVariable("courseId") Long courseId) {
        System.out.println("deleteInstructorController");
        instructorService.deleteInstructor(id);
        return "redirect:/instructors/"+courseId;
    }
}