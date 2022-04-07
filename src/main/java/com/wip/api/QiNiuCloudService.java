
package com.wip.api;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 *
 */
public class QiNiuCloudService {

    /**
     *
     */
    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";
    /**
     *
     */
    private static final String BUCKET = "";

    /**
     *
     */
    public static final String QINIU_UPLOAD_SITE = "";

    /**
     *
     * @param file
     * @param fileName
     * @return
     */
    public static String upload(MultipartFile file, String fileName) {

        //
        //
        //
        //
        //  Zone.zoneNa0()
        Configuration cfg = new Configuration(Zone.zone2());
        //
        UploadManager uploadManager = new UploadManager(cfg);

        //
        String key = null;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = null;
            response = uploadManager.put(file.getInputStream(), fileName, upToken,null,null);

            //
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return  putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.out.println(r.toString());
            try {
                System.out.println(r.bodyString());
            } catch (QiniuException ex2) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
