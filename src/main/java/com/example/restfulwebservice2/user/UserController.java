package com.example.restfulwebservice2.user;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // HATEOAS
        // "all-users", SERVER_PATH + "/users"
        // retieveAllUsers
        final EntityModel<User> model = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(                        // 어떤 링크를 추가할지
                WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());    // methodOn은 현재 클래스에 있는 retrieveAllUsers()를 추가하겠다는 것.
        model.add(linkTo.withRel("all-users"));                                     // all-users라는 url과 연결을 시킨다.

        return model;   // 위에서 만든 링크를 클라이언트에게 반환한다.
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }

}
