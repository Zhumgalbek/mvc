package peaksoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import peaksoft.enums.StudyFormat;
import peaksoft.model.Student;
import peaksoft.service.GroupService;
import peaksoft.service.StudentService;

import java.io.IOException;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final GroupService groupService;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("/students/{id}")
    public String getAllStudents(@PathVariable Long id, Model model) {
        Long companyId = groupService.getGroupById(id).getCompany().getId();
        model.addAttribute("students", studentService.getAllStudents(id));
        model.addAttribute("groupId",id);
        model.addAttribute("companyId",companyId);
        return "/student/students";
    }

    @GetMapping("/{id}/addStudent")
    public String assignStudent(@PathVariable Long id, Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("studyFormatOnline", StudyFormat.ONLINE);
        model.addAttribute("studyFormatOffline", StudyFormat.OFFLINE);
        model.addAttribute("groupId", id);
        return "/student/addStudent";
    }

    @PostMapping("/{id}/saveStudent")
    public String saveStudent(@ModelAttribute("student") Student student,
                             @PathVariable Long id) throws IOException {
        studentService.addStudent(id, student);
        return "redirect:/students/"+id;
    }

    @GetMapping("/updateStudent/{id}")
    public String updateStudent(@PathVariable("id") Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("groupId", student.getGroups().getId());
        model.addAttribute("studyFormatOnline", StudyFormat.ONLINE);
        model.addAttribute("studyFormatOffline", StudyFormat.OFFLINE);
        return "/student/updateStudent";
    }

    @PostMapping("/{groupId}/{id}/saveUpdateStudent")
    public String saveUpdateStudent(@PathVariable("groupId") Long groupId,
                                   @PathVariable("id") Long id,
                                   @ModelAttribute("student") Student student) throws IOException {
       studentService.updateStudent(student, id);
        return "redirect:/students/"+groupId;
    }

    @GetMapping("/{groupId}/{id}/deleteStudent")
    public String deleteStudent(@PathVariable("id") Long id, @PathVariable("groupId") Long groupId) {
        studentService.deleteStudent(id);
        return "redirect:/students/"+groupId;
    }
}

