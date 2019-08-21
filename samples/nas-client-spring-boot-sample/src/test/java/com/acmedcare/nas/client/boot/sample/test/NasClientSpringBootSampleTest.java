package com.acmedcare.nas.client.boot.sample.test;

import com.acmedcare.nas.api.entity.UploadEntity;
import com.acmedcare.nas.client.NasClient;
import com.acmedcare.nas.client.boot.sample.NasClientSpringBootSample;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@link NasClientSpringBootSampleTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-04-26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NasClientSpringBootSample.class)
public class NasClientSpringBootSampleTest {

  @Autowired
  private NasClient nasClient;

  @Test
  public void nasClientUpload() {

    UploadEntity uploadEntity =
        nasClient.upload(
            "draw.io-9.3.1",
            "dmg",
            "/Users/misselvexu/Downloads/draw.io-9.3.1.dmg",
            null);

    Assert.assertNotNull(uploadEntity);
    Assert.assertNotNull(uploadEntity.getFid());
  }
}
