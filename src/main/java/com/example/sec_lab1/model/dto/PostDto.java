package com.example.sec_lab1.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class PostDto {

    @NotBlank(message = "Content must not be blank!")
    @Length(min = 1, max = 256, message = "Content's length must be from 1 to 256 symbols!")
    private String content;

}
