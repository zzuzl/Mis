package cn.zzuzl.common.util;

import cn.zzuzl.dto.Result;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by Administrator on 2016/10/12.
 */
public class FileUtil {
    // 七牛云存储秘钥
    private static final String ACCESS_KEY = "aJhTVbsXt5eVkHhleqtFurnhrMFynlpUcHl5tija";
    private static final String SECRET_KEY = "2Rg0414DCgnbTlrEvfg_etqV8HX1PAEyqVgTvQpe";
    private static final String BUCKET_NAME = "mis-pic";
    private static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    private static BucketManager bucketManager = new BucketManager(auth);
    private static UploadManager uploadManager = new UploadManager();
    private static Logger logger = LogManager.getLogger(FileUtil.class);

    // 覆盖上传
    private static String getUploadToken(String key) {
        //<bucket>:<key>，表示只允许用户上传指定key的文件。在这种格式下文件默认允许“修改”，已存在同名资源则会被本次覆盖。
        return auth.uploadToken(BUCKET_NAME, key);
    }

    public static Result upload(String filePath, String schoolNum) throws IOException {
        Result result = new Result(true);
        String key = schoolNum + ".png";
        try {
            //调用put方法上传，这里指定的key和上传策略中的key要一致
            uploadManager.put(filePath, key, getUploadToken(key));
            FileInfo fileInfo = bucketManager.stat(BUCKET_NAME,key);

        } catch (QiniuException e) {
            Response r = e.response;
            logger.error(r.toString());
            result.setSuccess(false);
            result.setError(r.toString());
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            logger.info(upload("F:/1.png", "20133410139"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
