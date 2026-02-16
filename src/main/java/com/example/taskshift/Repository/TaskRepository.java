package com.example.taskshift.Repository;

import com.example.taskshift.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskRepository extends JpaRepository<Task, Long> {}
