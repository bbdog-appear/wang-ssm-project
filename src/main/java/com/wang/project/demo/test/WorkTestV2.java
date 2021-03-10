package com.wang.project.demo.test;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.GeneralBasicOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.GeneralBasicOCRResponse;

/**
 * <p>
 *      工作测试2
 * </p>
 *
 * @author cheng.wang
 * @version Id：OcrClientConfig.java Date：2021/3/9 19:00 Version：1.0
 */
public class WorkTestV2 {

    /**
     * 测试图片解析成json字符串
     * 使用腾讯云的通用文字识别SDK，调用API，其中api秘钥需要自己的账号生成。
     * https://cloud.tencent.com/document/product/866/33526
     *
     * @param args 入参
     */
    public static void main(String[] args) {
        try{

            Credential cred = new Credential(
                    "to_be_determined", "to_be_determined");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            OcrClient client = new OcrClient(cred, "ap-shanghai", clientProfile);

            GeneralBasicOCRRequest req = new GeneralBasicOCRRequest();
            req.setImageUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic3.16pic.com%2F00%2F00%2F13%2F16pic_13772_b.jpg&refer=http%3A%2F%2Fpic3.16pic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1617873512&t=ce78bc7423a07a3cc7ec34cad09d5f61");

            GeneralBasicOCRResponse resp = client.GeneralBasicOCR(req);

            System.out.println(GeneralBasicOCRResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

    }

}
