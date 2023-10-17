package com.runnerpia.boot.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class S3ConnectionTest {

    @Autowired
    private AmazonS3Client amazonS3;

    @Test
    public void s3ConnectionTest() {
        System.out.println(amazonS3);
        System.out.println(amazonS3.listBuckets());
        System.out.println(amazonS3.listBuckets().get(0));
        System.out.println(amazonS3.listBuckets().size());
        assertThat(amazonS3.listBuckets()).isNotNull();
    }
}
