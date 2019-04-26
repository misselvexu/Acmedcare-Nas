package com.acmedcare.nas.client;

import com.acmedcare.nas.api.NasClientConstants.ResponseCode;
import com.acmedcare.nas.api.ProgressCallback;
import com.acmedcare.nas.api.entity.ResponseEntity;
import com.acmedcare.nas.api.entity.UploadEntity;
import com.alibaba.fastjson.JSON;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * com.acmedcare.nas.client
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
public class NasClientTest {

  private NasClient nasClient;
  private String fileName = "acmed";
  private String fileSuffix = "xml";
  private String filePath =
      "/Users/misselvexu/Documents/acmedcare.gitlab.com/Acmedcare-Nas/client/src/test/resources/acmed.xml";

  private String destFilePath =
      "/Users/ive/git-acmedcare/Acmedcare-Nas/client/src/test/resources/downloaded-china-map-1.jpg";

  @Before
  public void init() {
    NasProperties nasProperties = new NasProperties();
    nasProperties.setServerAddrs(Lists.newArrayList("47.97.26.165:18848"));
    nasProperties.setHttps(false);
    nasProperties.setAppId("nas-app-id");
    nasProperties.setAppKey("nas-app-key");

    nasClient = NasClientFactory.createNewNasClient(nasProperties);
  }

  @Test
  public void createNewBucket() {}

  @Test
  public void deleteBucket() {}

  @Test
  public void upload() {
    UploadEntity uploadEntity =
        this.nasClient.upload(
            fileName,
            fileSuffix,
            filePath,
            new ProgressCallback() {
              @Override
              public void onProgress(long uploaded, long total) {
                System.out.println("上传进度:" + uploaded / total);
              }
            });
    Assert.assertNotNull(uploadEntity);
    Assert.assertEquals(uploadEntity.getResponseCode(), ResponseCode.UPLOAD_OK);
    Assert.assertNotNull(uploadEntity.getFid());
    Assert.assertNotNull(uploadEntity.getPublicUrl());

    System.out.println(JSON.toJSONString(uploadEntity));
  }

  @Test
  public void upload1() {
    upload();
  }

  @Test
  public void download() {

    ResponseEntity responseEntity = this.nasClient.download("2,0dc2a17774", destFilePath);

    Assert.assertNotNull(responseEntity);
    Assert.assertEquals(responseEntity.getResponseCode(), ResponseCode.DOWNLOAD_OK);
  }
}
