package com.runnerpia.boot.s3;

import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Import(S3MockConfig.class)
@SpringBootTest
class AwsS3UploaderTest {

  @Autowired
  private S3Mock s3Mock;
  @Autowired
  private S3Upload s3Upload;

  @AfterEach
  public void tearDown() {
    s3Mock.stop();
  }

  @Test
  public void upload() throws IOException {
    // given
    String path = "images/demo2.png";
    String contentType = "image/png";
    String dirName = "static";

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(path).getFile());
    MockMultipartFile mockMultipartFile = new MockMultipartFile("test", file.getName(), contentType, FileUtils.readFileToByteArray(file));

    // when
    String urlPath = s3Upload.uploadToAws(mockMultipartFile, dirName);

    // then
    assertThat(urlPath).contains(path.split("/")[1]);
    assertThat(urlPath).contains(dirName);
    System.out.println(urlPath);
  }
}