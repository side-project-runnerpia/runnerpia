package com.runnerpia.boot.user.dto.response;

import com.runnerpia.boot.user.entities.Bookmark;
import lombok.Getter;

import java.util.List;

@Getter
public class BookmarkInfoRespDto {

    private List<String> runningRouteIdList;

    /* List -> Dto */
    public BookmarkInfoRespDto(List<String> runningRouteIdList) {
        this.runningRouteIdList = runningRouteIdList;
    }
}
