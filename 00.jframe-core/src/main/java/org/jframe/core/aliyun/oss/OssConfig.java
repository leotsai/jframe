package org.jframe.core.aliyun.oss;

import org.jframe.core.aliyun.AliyunAccountConfig;

/**
 * Created by Leo on 2017/10/20.
 */
public interface OssConfig extends AliyunAccountConfig{
    String getBucketName();
    String getEndPoint();
}
