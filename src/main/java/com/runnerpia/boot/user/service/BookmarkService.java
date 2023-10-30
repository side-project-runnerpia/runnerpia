package com.runnerpia.boot.user.service;

import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.repository.RunningRouteRepository;
import com.runnerpia.boot.user.dto.request.BookmarkInfoReqDto;
import com.runnerpia.boot.user.dto.response.BookmarkInfoRespDto;
import com.runnerpia.boot.user.entities.Bookmark;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.repository.BookmarkRepository;
import com.runnerpia.boot.user.repository.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final RunningRouteRepository runningRouteRepository;
    private final UserRepository userRepository;

    
    public BookmarkInfoRespDto getAllUserBookmark(String userUUID) {

        User user = validateAndGetUser(userUUID);

        List<Bookmark> bookmarkList = bookmarkRepository.findByUser(user);
        List<String> runningRouteIdList = new ArrayList<>();
        for (Bookmark bookmark : bookmarkList) {
            runningRouteIdList.add(bookmark.getRunningRoute().getId().toString());
        }

        return new BookmarkInfoRespDto(runningRouteIdList);
    }
    
    @Transactional
    public Bookmark createBookmark(BookmarkInfoReqDto request) {

        User user = validateAndGetUser(request.getUserId());
        RunningRoute runningRoute = validateAndGetRunningRoute(request.getRunningRouteId());

        Bookmark bookmark = request.toEntity(user, runningRoute);

        return bookmarkRepository.save(bookmark);
    }

    @Transactional
    public Long deleteBookmark(BookmarkInfoReqDto request) {

        User user = validateAndGetUser(request.getUserId());
        RunningRoute runningRoute = validateAndGetRunningRoute(request.getRunningRouteId());

        return bookmarkRepository.deleteByUserAndRunningRoute(user, runningRoute);
    }

    private User validateAndGetUser(String userUUID) {
        Optional<User> findUser = userRepository.findById(UUID.fromString(userUUID));
        if(!findUser.isPresent()) throw new NoSuchElementException("사용자를 찾을 수 없습니다.");
        return findUser.get();
    }

    private RunningRoute validateAndGetRunningRoute(String runningRouteUUID) {
        Optional<RunningRoute> findRunningRoute = runningRouteRepository.findById(UUID.fromString(runningRouteUUID));
        if(!findRunningRoute.isPresent()) throw new NoSuchElementException("러닝 경로를 찾을 수 없습니다.");
        return findRunningRoute.get();
    }
}
