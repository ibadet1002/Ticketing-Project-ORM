package com.practice.service.impl;

import com.practice.dto.TaskDTO;
import com.practice.entity.Task;
import com.practice.enums.Status;
import com.practice.mapper.TaskMapper;
import com.practice.repository.TaskRepository;
import com.practice.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskMapper taskMapper, TaskRepository taskRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }


    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()) {
            return taskMapper.convertToDto(task.get());
        }
        return null;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO dto) {
    dto.setTaskStatus(Status.OPEN);
    dto.setAssignedDate(LocalDate.now());
    Task task = taskMapper.convertToEntity(dto);
    taskRepository.save(task);
    }

    @Override
    public void update(TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(dto.getId());
        Task convertedTask = taskMapper.convertToEntity(dto);
        if(task.isPresent()){
            convertedTask.setId(task.get().getId());
            convertedTask.setTaskStatus(task.get().getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }
    }

    @Override
    public void delete(Long id) {
    Optional<Task> foundTask = taskRepository.findById(id);
    if(foundTask.isPresent()){
        foundTask.get().setIsDeleted(true);
        taskRepository.save(foundTask.get());
    }
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {

        return taskRepository.totalNonCompletedTask(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTask(projectCode);
    }
}
