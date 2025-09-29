package com.example.sec_lab1.controller;

import com.example.sec_lab1.model.dto.PostDto;
import com.example.sec_lab1.service.PostService;
import com.example.sec_lab1.validation.XssSanitizer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final XssSanitizer xssSanitizer;

    @GetMapping
    public List<PostDto> getUserPosts() {
        return postService.getUserPosts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestBody @Valid PostDto postDto) {
        postDto = xssSanitizer.sanitize(postDto);
        return postService.createPost(postDto);
    }

}
