package com.practice.controller;

import com.practice.dto.ProjectDTO;
import com.practice.service.ProjectService;
import com.practice.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/project")
public class ProjectController {

    ProjectService projectService;
    UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createProject(Model model) {

        model.addAttribute("project", new ProjectDTO());
        model.addAttribute("projects", projectService.listAllProjects());
        model.addAttribute("managers", userService.listAllByRole("manager"));

        return "/project/create";

    }

    @PostMapping("/create")
    public String insertProject(@Valid @ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.listAllProjects());
            model.addAttribute("managers", userService);

            return "/project/create";

        }

        projectService.save(project);
        return "redirect:/project/create";

    }

//    @GetMapping("/delete/{projectcode}")
//    public String deleteProject(@PathVariable("projectcode") String projectcode) {
//        projectService.deleteById(projectcode);
//        return "redirect:/project/create";
//    }
//
//    @GetMapping("/complete/{projectcode}")
//    public String completeProject(@PathVariable("projectcode") String projectcode) {
//        projectService.complete(projectService.findById(projectcode));
//        return "redirect:/project/create";
//    }
//
//    @GetMapping("/update/{projectcode}")
//    public String editProject(@PathVariable("projectcode") String projectcode, Model model) {
//
//        model.addAttribute("project", projectService.findById(projectcode));
//        model.addAttribute("projects", projectService.findAll());
//        model.addAttribute("managers", userService.findManagers());
//
//        return "/project/update";
//
//    }
//
//    @PostMapping("/update")
//    public String updateProject(@Valid @ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {
//
//        if (bindingResult.hasErrors()) {
//
//            model.addAttribute("projects", projectService.findAll());
//            model.addAttribute("managers", userService.findManagers());
//
//            return "/project/update";
//
//        }
//
//        projectService.update(project);
//        return "redirect:/project/create";
//
//    }
//
//    @GetMapping("/manager/project-status")
//    public String getProjectByManager(Model model) {
//
//        UserDTO manager = userService.findById("john@cydeo.com");
//
//        List<ProjectDTO> projects = projectService.getCountedListOfProjectDTO(manager);
//
//        model.addAttribute("projects", projects);
//
//        return "/manager/project-status";
//    }
//
//    @GetMapping("/manager/complete/{projectCode}")
//    public String managerCompleteProject(@PathVariable("projectCode") String projectCode) {
//        projectService.complete(projectService.findById(projectCode));
//        return "redirect:/project/manager/project-status";
//    }

}