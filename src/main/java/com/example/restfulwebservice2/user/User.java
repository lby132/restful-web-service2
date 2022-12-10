package com.example.restfulwebservice2.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
public class User {

    private Integer id;
    @Size(min = 2)
    private String name;
    @Past
    private Date joinDate;
}
