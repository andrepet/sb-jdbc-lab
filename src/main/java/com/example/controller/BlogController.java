package com.example.controller;

import com.example.domain.Blog;
import com.example.domain.Comment;
import com.example.domain.Post;
import com.example.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Calendar;

@Controller
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/blog/")
    public ModelAndView listBlogs() {
        return new ModelAndView("blog/list")
                .addObject("blogs", blogRepository.listBlogs());
    }

    @GetMapping("/blog/{blogId}/")
    public ModelAndView listPosts(@PathVariable long blogId) {
        Blog blog = blogRepository.getBlog(blogId);
        return new ModelAndView("blog/posts")
                .addObject("blog", blog)
                .addObject("posts", blogRepository.getEntriesIn(blog))
                .addObject("author", blogRepository.getAuthorOf(blog));
    }

    @GetMapping("/blog/{blogId}/posts/{postId}")
    public ModelAndView listPost(@PathVariable long blogId, @PathVariable long postId) {
        Blog blog = blogRepository.getBlog(blogId);
        Post post = blogRepository.getPost(postId);

        // get the post and comments from the database,
        // set post and comments in the model and show this information in the view

        return new ModelAndView("blog/post")
                .addObject("blog", blog)
                .addObject("post", post)
                .addObject("comments", blogRepository.getCommentsFor(post))
                .addObject("author", blogRepository.getAuthorOf(blog));
    }

    @PostMapping("/comment")
    public ModelAndView comment(@RequestParam long blogId, @RequestParam long postId, @RequestParam String name, @RequestParam String text) {

        Blog blog = blogRepository.getBlog(blogId);
        Post post = blogRepository.getPost(postId);

        blogRepository.addComment(postId, name, text, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        return new ModelAndView("blog/post")
                .addObject("blog", blog)
                .addObject("post", post)
                .addObject("comments", blogRepository.getCommentsFor(post))
                .addObject("author", blogRepository.getAuthorOf(blog));
    }

    @PostMapping("/blog")
    public ModelAndView blog(@RequestParam String title) {
        blogRepository.addBlog(title);
        return new ModelAndView("blog/list")
                .addObject("blogs", blogRepository.listBlogs());
    }

}
