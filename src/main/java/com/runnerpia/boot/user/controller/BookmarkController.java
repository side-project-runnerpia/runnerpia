package com.runnerpia.boot.user.controller;

import com.runnerpia.boot.user.dto.request.BookmarkInfoReqDto;
import com.runnerpia.boot.user.dto.response.BookmarkInfoRespDto;
import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "모든 북마크 가져오기")
    @GetMapping("/getAll")
    public ResponseEntity<BookmarkInfoRespDto> getAllUserBookmark(Authentication authentication) {

        BookmarkInfoRespDto response =
                bookmarkService.getAllUserBookmark(authentication.getName());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "북마크 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created")})
    @PostMapping("/create")
    public ResponseEntity<Void> createBookmark(@RequestBody BookmarkInfoReqDto request,
                                               Authentication authentication) {

        Bookmark savedBookmark = bookmarkService.createBookmark(request, authentication.getName());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBookmark.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @Operation(summary = "북마크 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteBookmark(@RequestBody BookmarkInfoReqDto request, Authentication authentication) {

        bookmarkService.deleteBookmark(request, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
