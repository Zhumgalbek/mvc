package peaksoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import peaksoft.model.Course;
import peaksoft.model.Group;
import peaksoft.model.Instructor;
import peaksoft.model.Student;
import peaksoft.service.*;

import java.io.IOException;


@Controller
public class CourseController {
    private final CompanyService companyService;
    private final CourseService courseService;
    private final GroupService groupService;

    private final InstructorService instructorService;

    private final StudentService studentService;

    @Autowired
    public CourseController(CompanyService companyService, CourseService courseService, GroupService groupService, InstructorService instructorService, StudentService studentService) {
        this.companyService = companyService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.instructorService = instructorService;
        this.studentService = studentService;
    }

    @GetMapping("/courses/{id}")
    public String getAllCourses(@PathVariable Long id, Model model,
                                @ModelAttribute("group") Group group, @ModelAttribute("instructor") Instructor instructor, @ModelAttribute("student") Student student) {
        model.addAttribute("courses", courseService.getAllCourses(id));
        model.addAttribute("groups", groupService.getAllGroup(id));
        model.addAttribute("instructors", instructorService.getAllList());
        model.addAttribute("students", studentService.getAllListStudent());
        model.addAttribute("companyId", id);
        return "/course/courses";
    }

    @GetMapping("/courses/{id}/addCourse")
    public String addCourse(@PathVariable Long id, Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("companyId", id);
        return "/course/addCourse";
    }

    @PostMapping("/{id}/saveCourse")
    public String saveCourse(@ModelAttribute("course") Course course,
                             @PathVariable Long id) throws IOException {
        courseService.addCourse(id, course);
        return "redirect:/courses/" + id;
    }

    @GetMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("companyId", course.getCompany().getId());
        return "/course/update_courses";
    }

    @PostMapping("/{companyId}/{id}/saveUpdateCourse")
    public String saveUpdateCourse(@PathVariable("companyId") Long companyId,
                                   @PathVariable("id") Long id,
                                   @ModelAttribute("course") Course course) throws IOException {
        courseService.updateCourse(course, id);
        return "redirect:/courses/"+companyId;
    }

    @GetMapping("/{companyId}/{id}/deleteCourse")
    public String deleteCourse(@PathVariable("id") Long id, @PathVariable("companyId") Long companyId) {
        courseService.deleteCourse(id);
        return "redirect:/courses/" + companyId;
    }

    @PostMapping("{companyId}/{courseId}/assignGroup")
    private String assignGroup(@PathVariable("companyId") Long comId,
                               @PathVariable("courseId") Long courseId,
                               @ModelAttribute("group") Group group)
            throws IOException {
        System.out.println(group);
        groupService.assignGroup(courseId, group.getId());
        return "redirect:/groups/" + comId+"/"+courseId;
    }

    @PostMapping("/{courseId}/assignInstructor")
    private String assignInstructor(@PathVariable("courseId") Long courseId,
                                    @ModelAttribute("instructor") Instructor instructor)
            throws IOException {
        System.out.println(instructor);
        instructorService.assignInstructor(courseId, instructor.getId());
        return "redirect:/instructors/"+courseId;
    }

    @PostMapping("/{groupId}/assignStudent")
    private String assignStudent(@PathVariable("groupId") Long groupId,
                                    @ModelAttribute("student") Student student)
            throws IOException {
        System.out.println(student);
        studentService.assignStudent(groupId, student.getId());
        return "redirect:/students/"+groupId;
    }
}