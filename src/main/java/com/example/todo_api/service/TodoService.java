package com.example.todo_api.service;


import com.example.todo_api.entity.Todo;
import com.example.todo_api.exception.ResourceNotFoundException;
import com.example.todo_api.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.resource.beans.container.spi.BeanLifecycleStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService extends ResourceNotFoundException {
    private final TodoRepository repo;

    public List<Todo> getAll() {
        return repo.findAll();
    }

    public Todo getById(Long id) {
        return repo.findById(id).orElseThrow(
                ()-> new RuntimeException("Todo not found: "+id)
        );
    }

    @Transactional
    public Todo create(Todo todo){
        return repo.save(todo);
    }

    @Transactional
    public Todo update(Long id, Todo updated){
        Todo existing = getById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCompleted(updated.isCompleted());
        return repo.save(existing);
    }

    @Transactional
    public Todo toggleComplete(Long id){
        Todo todo =  getById(id);
        todo.setCompleted(!todo.isCompleted());
        return repo.save(todo);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }
}
