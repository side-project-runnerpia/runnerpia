package com.runnerpia.boot.user.dto.response;

import com.runnerpia.boot.user.entities.Bookmark;
import lombok.Getter;

import java.util.List;

@Getter
public class BookmarkInfoRespDto {

    private String runningRouteId;
    private List<String> runningRouteIdList;

    /* Entity -> Dto*/
    public BookmarkInfoRespDto(Bookmark bookmark) {
        this.runningRouteId = bookmark.getRunningRoute().getId().toString();
    }

    /* List -> Dto */
    public BookmarkInfoRespDto(List<String> runningRouteIdList) {
        this.runningRouteIdList = runningRouteIdList;
    }
}
