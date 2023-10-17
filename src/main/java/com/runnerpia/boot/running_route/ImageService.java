package com.runnerpia.boot.running_route;

import com.runnerpia.boot.running_route.entities.Image;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.repository.ImageRepository;
import com.runnerpia.boot.s3.S3Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
  private final S3Upload s3Upload;
  private final ImageRepository imageRepository;
  private final String DIRECTORY_NAME = "running_route";
  @Async
  @Transactional
  public String uploadS3Image(MultipartFile file, String directory) throws IOException {
    return s3Upload.uploadToAws(file, directory);
  }

  @Async
  public CompletableFuture<List<Image>> saveAll(List<MultipartFile> files, RunningRoute route) {
    List<Image> images = files.parallelStream()
            .map(file -> setImageEntity(file, route))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    List<Image> savedImages = imageRepository.saveAll(images);
    System.out.println(savedImages);
    return CompletableFuture.completedFuture(savedImages);
  }

  private Image setImageEntity(MultipartFile file, RunningRoute route) {
    try {
      String url = uploadS3Image(file, DIRECTORY_NAME);
      return Image.builder()
              .url(url)
              .runningRoute(route)
              .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
