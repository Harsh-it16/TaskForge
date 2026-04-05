package com.example.todo_api.controller;


import com.example.todo_api.entity.Todo;
import com.example.todo_api.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService service;

    @GetMapping
    public List<Todo> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody Todo todo){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(todo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id,
                                       @Valid @RequestBody Todo todo ){
                   return ResponseEntity.ok(service.update(id, todo));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggle(@PathVariable Long id){
        return ResponseEntity.ok(service.toggleComplete(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo>  delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
