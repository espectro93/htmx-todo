package com.example.htmxtodo;

import io.github.wimdeblauwe.hsbt.mvc.HtmxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoRepository todoRepository;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping
    public String getTodos(Model model) {
        model.addAttribute("todos", todoRepository.findAll());
        return "todos";
    }

    @PostMapping
    HtmxResponse add(@RequestParam("new-todo") String newTodo, Model model) {
        todoRepository.save(new Todo(null, newTodo));
        model.addAttribute("todos", todoRepository.findAll());
        return new HtmxResponse()
                .addTemplate("todos :: todos")
                .addTemplate("todos :: todos-form");
    }

    @ResponseBody
    @DeleteMapping(path = "/{todoId}", produces = MediaType.TEXT_HTML_VALUE)
    String delete(@PathVariable Integer todoId) {
        todoRepository.findById(todoId).ifPresent(todoRepository::delete);
        return "";
    }

    @PostMapping("/reset")
    String reset(Model model) {
        eventPublisher.publishEvent(new TodosResetEvent());
        model.addAttribute("todos", todoRepository.findAll());
        return "todos :: todos";
    }

}
