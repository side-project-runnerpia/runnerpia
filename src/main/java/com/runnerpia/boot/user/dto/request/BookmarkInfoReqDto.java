package com.runnerpia.boot.user.dto.request;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookmarkInfoReqDto {

    private String runningRouteId;
    private String userId;

    public Bookmark toEntity(User user, RunningRoute runningRoute) {
        return Bookmark.builder()
                .user(user)
                .runningRoute(runningRoute)
                .build();
    }
}
