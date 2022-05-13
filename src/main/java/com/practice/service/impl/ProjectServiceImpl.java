package com.practice.service.impl;

import com.practice.dto.ProjectDTO;
import com.practice.dto.UserDTO;
import com.practice.entity.Project;
import com.practice.entity.User;
import com.practice.enums.Status;
import com.practice.mapper.ProjectMapper;
import com.practice.mapper.UserMapper;
import com.practice.repository.ProjectRepository;
import com.practice.service.ProjectService;
import com.practice.service.TaskService;
import com.practice.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

   private final  ProjectRepository projectRepository;
   private final  ProjectMapper projectMapper;
   private final UserService userService;
   private final UserMapper userMapper;
   private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }


    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        return projectMapper.convertToDto(project);
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> list = projectRepository.findAll();

        return list.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
    Project project=projectMapper.convertToEntity(dto);
    projectRepository.save(project);
    }

    @Override
    public void update(ProjectDTO dto) {
        //Find current user
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        //Map updated user dto to entity object
        Project convertedProject = projectMapper.convertToEntity(dto);
        //set id to converted object
        convertedProject.setId(project.getId());
        convertedProject.setProjectStatus(project.getProjectStatus());
        //save updated user
        projectRepository.save(convertedProject);

        return ;

    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);
        projectRepository.save(project);

    }

    @Override
    public void complete(String projectCode) {
        Project project =projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDTO> ListAllProjectDetails() {
        UserDTO currentDTO = userService.findByUserName("harold@manager.com");
        User user = userMapper.convertToEntity(currentDTO);

        List<Project> list = projectRepository.findAllByAssignedManager(user);

        return list.stream().map(project -> {
            ProjectDTO obj = projectMapper.convertToDto(project);
            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            obj.setUnfinishedTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));

            return obj;


        }).collect(Collectors.toList());
    }
}