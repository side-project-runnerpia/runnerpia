package com.runnerpia.boot.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import static com.runnerpia.boot.user.entities.QBookmark.bookmark;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<UUID> findRunningRouteIdsByUserId(String userId) {
        return queryFactory
                .select(bookmark.runningRoute.id)
                .from(bookmark)
                .where(bookmark.user.userId.eq(userId))
                .fetch();
    }
}
