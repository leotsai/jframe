package org.jframe.core.aliyun.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import org.jframe.core.logging.LogHelper;

import java.io.File;
import java.io.InputStream;

/**
 * Created by leo on 2017-10-21.
 * 您的工程中可以有多个OSSClient，也可以只有一个OSSClient。OSSClient可以并发使用。
 * https://help.aliyun.com/document_detail/32010.html?spm=5176.doc32011.6.661.21au0O
 */
public class OssBucket {
    private OSSClient client;
    private String bucketName;

    public <T extends OssBucket> T initialize(OssConfig config) {
        if(this.client != null){
            return (T)this;
        }
        try {
            this.client = new OSSClient(config.getEndPoint(), config.getKey(), config.getSecret());
            this.bucketName = config.getBucketName();
            return (T)this;
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public void close(){
        this.client.shutdown();
    }

    public void putFile(String key, String fileFullPath) {
        try {
            this.client.putObject(this.bucketName, key, new File(fileFullPath));
        } catch (Exception ex) {
            LogHelper.log(this.getClass() + ".putFile1", ex);
            throw ex;
        }
    }

    public void putFile(String key, InputStream stream) {
        try {
            client.putObject(this.bucketName, key, stream);
        } catch (Exception ex) {
            LogHelper.log(this.getClass() + ".putFile2", ex);
            throw ex;
        }
    }

    public OSSObject getObject(String key) {
        try {
            return client.getObject(this.bucketName, key);
        } catch (Exception ex) {
            LogHelper.log(this.getClass() + ".getObject", ex);
            throw ex;
        }
    }

    public InputStream getObjectContent(String key) {
        try {
            return client.getObject(this.bucketName, key).getObjectContent();
        } catch (Exception ex) {
            LogHelper.log(this.getClass() + ".getObjectContent", ex);
            throw ex;
        }
    }

    public InputStream tryGetObjectContent(String key) {
        try {
            return client.getObject(this.bucketName, key).getObjectContent();
        } catch (Exception ex) {
            LogHelper.log(this.getClass() + ".tryGetObjectContent", ex);
            return null;
        }
    }

    public boolean existObject(String key) {
        return this.client.doesObjectExist(this.bucketName, key);
    }
}
