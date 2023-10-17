package com.runnerpia.boot.running_route;

import com.runnerpia.boot.running_route.entities.Image;
import com.runnerpia.boot.running_route.entities.RunningRoute;
import com.runnerpia.boot.running_route.repository.ImageRepository;
import com.runnerpia.boot.s3.S3Upload;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ImageServiceTest {
  private final String FAKE_IMAGE_URL = "https://fake-s3-url.com/image.jpg";
  @Mock
  private S3Upload s3Upload;
  @InjectMocks
  private ImageService imageService;

  @Test
  @Transactional
  public void S3_이미지_업로드_URL_반환_테스트() throws IOException {
    when(s3Upload.uploadToAws(any(MultipartFile.class), anyString()))
            .thenReturn(FAKE_IMAGE_URL);

    // 테스트할 파일 및 디렉토리 이름
    MultipartFile mockFile = new MockMultipartFile("image.jpg", new byte[0]);
    String directory = "images";

    // 비동기 메서드 호출
    String s3Url = imageService.uploadS3Image(mockFile, directory);

    // 가짜 S3 업로드 로직이 호출되었는지 확인
    verify(s3Upload).uploadToAws(mockFile, directory);

    // 예상한 결과값과 일치하는지 확인
    System.out.println(s3Url);
    assertEquals(FAKE_IMAGE_URL, s3Url);
  }
}

