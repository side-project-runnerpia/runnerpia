package com.runnerpia.boot.user.service;

import com.runnerpia.boot.running_route.entities.Tag;
import com.runnerpia.boot.running_route.entities.enums.TagStatus;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.user.dto.request.UserSignInReqDto;
import com.runnerpia.boot.user.entities.User;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class UserTagServiceTest {

    @Autowired
    UserTagService userTagService;

    @Autowired
    UserService userService;

    @Autowired
    TagRepository tagRepository;

    private static final String USER_ID = "userId";
    private static final String USER_CITY = "userCity";
    private static final String USER_STATE = "userState";
    private static final String SECURE_TAG = "secureTag";
    private static final String RECOMMENDED_TAG = "recommendedTag";


    private User savedUser;

    private Tag savedSecureTag;
    private Tag savedRecommendedTag;

    private UserSignInReqDto request;

    @BeforeEach
    void initData() {

        Tag secureTag = new Tag(SECURE_TAG, TagStatus.SECURE);
        savedSecureTag = tagRepository.save(secureTag);

        Tag recommendedTag = new Tag(RECOMMENDED_TAG, TagStatus.SECURE);
        savedRecommendedTag = tagRepository.save(recommendedTag);

        List<String> secureTags = new ArrayList<>();
        secureTags.add(savedSecureTag.getId().toString());

        List<String> recommendedTags = new ArrayList<>();
        recommendedTags.add(savedRecommendedTag.getId().toString());

        request = UserSignInReqDto.builder()
                .userId(USER_ID)
                .city(USER_CITY)
                .state(USER_STATE)
                .secureTags(secureTags)
                .recommendedTags(recommendedTags)
                .build();

        savedUser = userService.createUser(request);
    }

    @Test
    @DisplayName("추천태그 저장")
    void createUserRecommendedTagsTest() {

        List<String> userRecommendedTags = userTagService.createUserRecommendedTags(request, savedUser);

        assertThat(userRecommendedTags).contains(SECURE_TAG);

    }

    @Test
    @DisplayName("안심태그 저장")
    void createUserSecureTagsTest() {

        List<String> userSecureTags = userTagService.createUserSecureTags(request, savedUser);

        assertThat(userSecureTags).contains(SECURE_TAG);


    }



}