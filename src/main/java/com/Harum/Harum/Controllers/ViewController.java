package com.Harum.Harum.Controllers;


import com.Harum.Harum.Models.Views;
import com.Harum.Harum.Models.Votes;
import com.Harum.Harum.Services.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/views")
public class ViewController {

    @Autowired
    private ViewService viewService;

    // Create: Tạo mới một lượt xem
    @PostMapping
    public ResponseEntity<Views> createView(@RequestBody Views view) {
        try {
            Views createdView = viewService.createView(view);
            return new ResponseEntity<>(createdView, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Read: Lấy tất cả lượt xem
    @GetMapping
    public ResponseEntity<List<Views>> getAllViews() {
        try {
            List<Views> views = viewService.getAllViews();
            if (views.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(views, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Read: Lấy lượt xem theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Views> getViewById(@PathVariable("id") String id) {
        Optional<Views> view = viewService.getViewById(id);
        if (view.isPresent()) {
            return new ResponseEntity<>(view.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Kiểm tra xem user đã tương tác với post chưa
    @GetMapping("/check/{userId}/{postId}")
    public Optional<Votes> checkUserVote(@PathVariable String userId, @PathVariable String postId) {
        return viewService.getVoteByUserAndPost(userId, postId);
    }


}
