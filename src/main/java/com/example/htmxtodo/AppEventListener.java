package com.example.htmxtodo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AppEventListener {

    private final TodoRepository todoRepository;

    @EventListener({ApplicationReadyEvent.class, TodosResetEvent.class})
    void handle() {
        todoRepository.deleteAll();
        Stream.of("Go for a run, Make some coffee, Code a  bit".split(","))
                .forEach(todo -> todoRepository.save(new Todo(null, todo)));
    }
}
