package com.Harum.Harum.Services;


import com.Harum.Harum.Models.Posts;
import com.Harum.Harum.Models.Views;

import com.Harum.Harum.Models.Votes;
import com.Harum.Harum.Repository.PostRepo;
import com.Harum.Harum.Repository.ViewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViewService {

    @Autowired
    private ViewRepo viewRepository;
     @Autowired
    private PostRepo postRepository;

    // Create: Thêm một lượt xem mới
    // Tạo mới một lượt xem
    public Views createView(Views view) {
        // Lưu lại lượt xem
        Views savedView = viewRepository.save(view);

        // Cập nhật countView của bài viết
        Posts post = postRepository.findById(view.getPostId()).orElse(null);
        if (post != null) {
            post.setCountView(post.getCountView() + 1); // Tăng countView lên 1
            postRepository.save(post); // Lưu lại bài viết sau khi cập nhật
        }

        return savedView;
    }


    // Read: Lấy tất cả lượt xem
    public List<Views> getAllViews() {
        return viewRepository.findAll();
    }

    // Read: Lấy lượt xem theo ID
    public Optional<Views> getViewById(String id) {
        return viewRepository.findById(id);
    }

    // Read: Lấy tất cả lượt xem của một bài viết theo postId
    public List<Views> getViewByPostId(String postId) {
        return viewRepository.findByPostId(postId);
    }
    // Kiểm tra xem user đã xem   post hay chưa
    public Optional<Views> getViewByUserAndPost(String userId, String postId) {
        return viewRepository.findByUserIdAndPostId(userId, postId);
    }


}
