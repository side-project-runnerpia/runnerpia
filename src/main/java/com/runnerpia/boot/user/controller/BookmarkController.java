package com.runnerpia.boot.user.controller;

import com.runnerpia.boot.user.dto.BookmarkInfoDto;
import com.runnerpia.boot.user.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUserBookmark(@RequestBody BookmarkInfoDto.Request request) {

        //TODO: 토큰에서 uuid 를 꺼내는 방식추가 필요, 토큰 구현전까지 @RequestBody 받는다고 가정하고 진행
        BookmarkInfoDto.Response response = bookmarkService.getAllUserBookmark(request.getUserId());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBookmark(@RequestBody BookmarkInfoDto.Request request) {

        //TODO: 토큰에서 uuid 를 꺼내는 방식추가 필요

        BookmarkInfoDto.Response response = bookmarkService.createBookmark(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteBookmark(@RequestBody BookmarkInfoDto.Request request) {

        //TODO: 토큰에서 uuid 를 꺼내는 방식추가 필요

        bookmarkService.deleteBookmark(request);
        return ResponseEntity.ok().build();
    }
}
