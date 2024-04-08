package com.geng.api;

import com.tencent.ads.ApiContextConfig;
import com.tencent.ads.ApiException;
import com.tencent.ads.TencentAds;
import com.tencent.ads.model.OauthTokenResponseData;

public class Example {

    private static Long clientId = 1112021952L; // 修改为你的clientId
    private static String clientSecret = "aBKNIJd7PIqjiR3g"; // 修改为你的clientSecret
    private static String grantType = "authorization_code";
    private static String authorizationCode = "fbf006c1206fe566a2f3cd143b0611b7"; // 修改为你获取到的AUTHORIZATION CODE
    private static String redirectUri = "https://api.talkingdata.com/ia00006/dataNexusCallBack"; // 修改为你的回调地址
    private static String state = "data_nexus_1112021952";
    private static String scope = "audience_management";
    private static String accountType = "ACCOUNT_TYPE_QQ";
    private static Long accountDisplayNumber = 1L;
    //authorization_code=fbf006c1206fe566a2f3cd143b0611b7&state=data_nexus_1112021952
    public static void main(String[] args) throws ApiException {

//        TencentAds tencentAds = TencentAds.getInstance();
//        tencentAds.init(new ApiContextConfig().isDebug(true));
//
//        String authorize = tencentAds
//                .oauth()
//                .oauthAuthorize(clientId, redirectUri, state, scope, accountType, accountDisplayNumber, null, null);
//        System.out.println(authorize);
//        try {
//            OauthTokenResponseData responseData = tencentAds.oauth()
//                    .oauthToken(clientId, clientSecret, grantType, authorizationCode, null, redirectUri);
//            if (responseData != null) {
//                String accessToken = responseData.getAccessToken();
//                System.out.println(accessToken);
//                tencentAds.setAccessToken(accessToken);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}