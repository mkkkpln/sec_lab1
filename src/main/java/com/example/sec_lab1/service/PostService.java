package com.example.sec_lab1.service;

import com.example.sec_lab1.exception.ApplicationException;
import com.example.sec_lab1.model.Post;
import com.example.sec_lab1.model.User;
import com.example.sec_lab1.model.dto.PostDto;
import com.example.sec_lab1.repository.PostRepository;
import com.example.sec_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public List<PostDto> getUserPosts() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Post> posts = postRepository.findAllByUserEmail(user.getEmail());
        return posts.stream()
                .map(post -> new PostDto(post.getContent()))
                .toList();
    }

    @Transactional
    public PostDto createPost(PostDto postDto) {
        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        User user = userRepository.findByEmail(email);

        Post post = Post.builder()
                .content(postDto.getContent())
                .user(user)
                .build();

        try {
            postRepository.save(post);
        } catch (Exception ex) {
            throw new ApplicationException("Bad request", ApplicationException.Reason.BAD_REQUEST);
        }

        return postDto;
    }
}
