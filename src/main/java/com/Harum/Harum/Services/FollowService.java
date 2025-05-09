package com.Harum.Harum.Services;

import com.Harum.Harum.Enums.NotificationTypes;
import com.Harum.Harum.Models.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Harum.Harum.Models.Follows;
import com.Harum.Harum.Repository.FollowRepo;

import java.util.List;

@Service
public class FollowService {
    private FollowRepo followRepo;

    @Autowired
    private NotificationService notificationService;

    public List<Follows> getFollowers(String userId) {
        return followRepo.findByFollowedId(userId);
    }

    public List<Follows> getFollowing(String userId) {
        return followRepo.findByFollowerId(userId);
    }

    public boolean isFollowing(String followerId, String followedId) {
        return followRepo.existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    public Follows followUser(String followerId, String followedId) {
        if (!isFollowing(followerId, followedId)) {
            Follows follow = new Follows(followerId, followedId);
            Follows savedFollow = followRepo.save(follow);

            // Tạo notification
            Notifications noti = new Notifications(
                    followedId, // người nhận là người bị follow
                    "Bạn có người theo dõi mới!",
                    NotificationTypes.FOLLOW,
                    null, // postId
                    null, // commentId
                    savedFollow.getId() // followId
            );
            notificationService.createNotification(noti);

            return savedFollow;
        }
        return null;
    }

    public void unfollowUser(String followerId, String followedId) {
        followRepo.deleteByFollowerIdAndFollowedId(followerId, followedId);
    }
}
