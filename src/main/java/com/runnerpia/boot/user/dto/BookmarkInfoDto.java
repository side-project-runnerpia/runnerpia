package com.runnerpia.boot.user.dto;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.entities.User;
import lombok.*;

import java.util.List;

public class BookmarkInfoDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String runningRouteId;
        private String userId;

        public Bookmark toEntity(User user, RunningRoute runningRoute) {
            return Bookmark.builder()
                    .user(user)
                    .runningRoute(runningRoute)
                    .build();
        }
    }

    @Getter
    public static class Response {
        private String runningRouteId;
        private List<String> runningRouteIdList;

        /* Entity -> Dto*/
        public Response(Bookmark bookmark) {
            this.runningRouteId = bookmark.getRunningRoute().getId().toString();
        }

        /* List -> Dto */
        public Response(List<String> runningRouteIdList) {
            this.runningRouteIdList = runningRouteIdList;
        }
    }
}
