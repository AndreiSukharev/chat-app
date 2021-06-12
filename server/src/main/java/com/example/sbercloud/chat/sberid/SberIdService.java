//package com.example.sbercloud.chat.sberid;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//
//public class SberIdService {
//
//    AuthData authData;
//
//    SberApiClient client;
//
//    public SberIdService() {
//        this.client = new SberApiClient(
//                "6a0e103b-1873-4ed5-9903-b4fb51192b23",
//                "oYBjtOQmQNX4cMAmnKxAnvoGrWeccGKzKxvCKqtm0jQ",
//                "https://api.sberbank.ru/ru/prod/tokens/v2/oidc", //url на получение access token (для подключений через двусторонний TLS)
//                //https://sec.api.sberbank.ru/ru/prod/tokens/v2/oidc - для подключений через ФПСУ
//                "https://api.sberbank.ru/ru/prod/sberbankid/v2.1/userinfo" //url на получение пользовательских данных (для подключений через двусторонний TLS)
//                //https://sec.api.sberbank.ru/ru/prod/sberbankid/v2.1/userinfo - для подключений через ФПСУ
//        );
//        installSsl();
//    }
//
//    private void installSsl() {
//        try (InputStream keyStoreStream = new FileInputStream("./cert.p12")) {
//            client.setSslContext(keyStoreStream, "put_your_key_here");
//        }
//    }
//
//    public void getToken() {
//        authData = client.authRequest(
//                "http://127.0.0.1:8080/login", //redirect_uri
//                "3C506040-15A9-226B-EFB4-6389A0C0C165", //auth_code
//                "q5P5afVZ1kdehAfbn5XvnCkIfe9kDV1nSRicU8v6efU", //nonce
//                "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk") //code_verifier (опционально)
//                .execute();
//    }
//
//    public String getUserData() {
//        UserInfoData userInfoData = client.userInfoRequest(authData.getAccessToken()).execute();
//        String sub = userInfoData.getSub();
//        return sub;
//    }
//}
