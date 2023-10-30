package com.runnerpia.boot.user.service;

import com.runnerpia.boot.running_route.entities.Tag;
import com.runnerpia.boot.running_route.repository.TagRepository;
import com.runnerpia.boot.user.dto.request.UserSignInReqDto;
import com.runnerpia.boot.user.entities.User;
import com.runnerpia.boot.user.entities.UserRecommendedTag;
import com.runnerpia.boot.user.entities.UserSecureTag;
import com.runnerpia.boot.user.repository.UserRecommendedTagRepository;
import com.runnerpia.boot.user.repository.UserSecureTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTagService {

    private final TagRepository tagRepository;
    private final UserSecureTagRepository userSecureTagRepository;
    private final UserRecommendedTagRepository userRecommendedTagRepository;

    @Transactional
    public List<String> createUserRecommendedTags(UserSignInReqDto request, User user) {
        List<String> recommendedTags = request.getRecommendedTags();
        List<String> userRecommendedTagUUIDs = new ArrayList<>();
        for (String recommendedTag : recommendedTags) {

            UUID secureTagUUID = UUID.fromString(recommendedTag);
            Optional<Tag> findTag = tagRepository.findById(secureTagUUID);

            UserRecommendedTag savedUserRecommendedTag = userRecommendedTagRepository.save(new UserRecommendedTag(findTag.get(), user));
            String userRecommendedTagUUID = savedUserRecommendedTag.getTag().getId().toString();
            userRecommendedTagUUIDs.add(userRecommendedTagUUID);
        }

        return userRecommendedTagUUIDs;
    }

    @Transactional
    public List<String> createUserSecureTags(UserSignInReqDto request, User user) {
        List<String> secureTags = request.getSecureTags();
        List<String> savedUserSecureTagUUIDs = new ArrayList<>();
        for (String secureTag : secureTags) {

            UUID secureTagUUID = UUID.fromString(secureTag);
            Optional<Tag> findTag = tagRepository.findById(secureTagUUID);

            UserSecureTag savedUserSecureTag = userSecureTagRepository.save(new UserSecureTag(findTag.get(), user));
            String savedUserSecureTagUUID = savedUserSecureTag.getTag().getId().toString();
            savedUserSecureTagUUIDs.add(savedUserSecureTagUUID);
        }

        return savedUserSecureTagUUIDs;
    }
}
