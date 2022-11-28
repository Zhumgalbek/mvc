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
import peaksoft.service.GroupService;

@Controller
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groups/{companyId}/{id}")
    public String getAllGroups(@PathVariable("id") Long id,
                               @PathVariable("companyId")
                               Long companyId, Model model) {
        model.addAttribute("groupCourses", groupService.getAllGroupsByCourseId(id));
        model.addAttribute("courseId", id);
        model.addAttribute("companyId", companyId);
        return "/group/groups";
    }

    @GetMapping("/groups/{id}/addGroup")
    public String addGroup(@PathVariable Long id, Model model) {
        model.addAttribute("group", new Group());
        model.addAttribute("companyId", id);
        return "/group/addGroup";
    }

    @PostMapping("/{id}/saveGroup")
    public String saveGroup(@ModelAttribute("group") Group group,
                            @PathVariable Long id) {
        groupService.addGroup(id, group);
        return "redirect:/courses/" + id;
    }

    @GetMapping("/groups/{companyId}/{id}/addGroupByCourseId")
    public String addGroupByCourseId(@PathVariable("companyId") Long companyId, @PathVariable("id") Long id, Model model) {
        model.addAttribute("newGroup", new Group());
        model.addAttribute("courseId", id);
        model.addAttribute("companyId", companyId);
        return "/group/addGroupByCourse";
    }

    @PostMapping("/{courseId}/{id}/saveGroupByCourseId")
    public String saveGroupByCourseId(@ModelAttribute("group") Group group,
                            @PathVariable("id") Long id, @PathVariable("courseId") Long courseId) {
        groupService.addGroupByCourseId(id,courseId, group);
        return "redirect:/groups/" + id +"/"+courseId;
    }

    @GetMapping("/updateGroup/{id}")
    public String updateGroup(@PathVariable("id") Long id, Model model) {
        Group group = groupService.getGroupById(id);
        model.addAttribute("group", group);
        model.addAttribute("companyId", group.getCompany().getId());
        return "/group/update_group";
    }

    @PostMapping("/{companyId}/{id}/saveUpdateGroup")
    public String saveUpdateCourse(@PathVariable("companyId") Long companyId,
                                   @PathVariable("id") Long id,
                                   @ModelAttribute("group") Group group) {
        groupService.updateGroup(group,id);
        return "redirect:/courses/"+companyId;
    }

    @GetMapping("/updateGroupByCourseId/{courseId}/{id}")
    public String updateGroupByCourseId(@PathVariable("id") Long id, @PathVariable("courseId") Long courseId, Model model) {
        Group group = groupService.getGroupById(id);
        model.addAttribute("group", group);
        model.addAttribute("courseId", courseId);
        return "/group/updateGroup";
    }

    @PostMapping("/{courseId}/{id}/saveUpdateGroupByCourseId")
    public String saveUpdateGroupByCourseId(@PathVariable("courseId") Long courseId,
                                   @PathVariable("id") Long id,
                                   @ModelAttribute("group") Group group) {
        Long companyId = groupService.getGroupById(id).getCompany().getId();
        groupService.updateGroup(group,id);
        return "redirect:/groups/"+companyId+"/"+courseId;
    }

    @GetMapping("/{companyId}/{id}/deleteGroup")
    public String deleteGroup(@PathVariable("id") Long id, @PathVariable("companyId") Long companyId) {
        groupService.deleteGroup(id);
        return "redirect:/courses/" + companyId;
    }

    @GetMapping("/{courseId}/{id}/deleteGroupByCourseId")
    public String deleteGroupCourseId(@PathVariable("id") Long id, @PathVariable("courseId") Long courseId) {
        Long companyId = groupService.getGroupById(id).getCompany().getId();
        groupService.deleteGroup(id);
        return "redirect:/groups/"+ companyId + "/" + courseId;
    }
}
