package com.example.bestyou.utils;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Accesstoken {

    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken(){
        try{
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"bestyou-26e86\",\n" +
                    "  \"private_key_id\": \"099ad1150d937b542ab91b5cfc5d2afb3afac970\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDCl8y1v+i5BV31\\n8dabPStUVGLz66CWeav1Y4wikgUBtttFb8jVWyKLhBbVPO+aPCDDQOoxTe+FjAtJ\\nhMbEm9oWhoRDwL8tUqSiwNBlsmPTGWw9lEPeV1ne954pySkCwSSsFjiKs4ULkfeW\\npB9lVVtJMY6zvsfHVLNyhhRTRFnBg5z3CXgsxEk/ZKuV+HSz35fSC2XmQASEe+ls\\nx39s0sTscbuj1hX9Cf7u4kJbIM48Agqe4lGbwPyhBC3WN9mGxEGVvw3W0VEMoSS+\\nsyZZHqoEOHmgvGKXiTMkh2v8UqsDzaME4XFbbAaDmwEMUap4GTfMOJQvepZI2jFD\\nWJY8PxvBAgMBAAECggEAJWBa5Z4S6VbqkU7+VZsOM4hx6UAc/EoqiVuxkwJRC98/\\nhrrN7I0wuXOJnzOxIgLimB5j3UyG8TPRBzIFq8Xzwdfcl9bx4Za6ILjATohRPD7p\\n13oe/xnwHS+apZQJ2ImYraUm/x00NOC4maq0jRY0Dn8CVewTG2pdfWXbiZLbWpvC\\nkLBskixaZTC9bzn6VNWeC0X31B2zthd3BqqBhEZwHxFBje681t6PomGYgeckSaiA\\nNSrJaXl6vZidepqRhP+GI5ISWAVf6O/k0gi4N1C2gVDyu42vJ7KOGxOn+f06FLFE\\nayCDPb7tfaii0Piq90as+EIl6tTOR3QHdSEl0fNWcQKBgQD+vB9BN22TDkIzqMn7\\nq0PoH0Nhb77qqsXafKlCufFkf7D/jpMca8taNKcvQQKr1CRC/JhXDDQLAanAY/44\\nnPitJ2MbUi9EI0UqXI0RQaRmjAn4bu5JO5xeZHxS+NklbgWEufvA5g+0A6Me1ofw\\nisVYTS02LCCEfqj8RtLE2y4qswKBgQDDjzYWCo0z7GnrW+g9BgGOU+4bMqEnw9hy\\nDpeahFnDOy8xE7XH6a5x8pRV0SgDo+J1w75Awhb8YQ3mBM/f4oi62G6JdMaoe2G3\\nzVVMprYZIKm+rtPR06Iy6svUKQhdxGgv79nDsRtXXmjyXZPLViajkKIkEupGlDZF\\nzT9keTPpuwKBgQC8+/pAGwwa9pAc3VOEd+o4UsFdj1bXqHyC/MtiKqsxAR7iafcw\\nNbS3JqsF/rgpnIiOffHTWsVdiQ1UI5seiy5e4ALB6TYR2gzdQa6Sp919CtX2zu3P\\nvP7kdK5brC0FfNtxPb3NvaDWz3/8R/KwfXOtcAxjzVcOlR1vlGeYDnfwywKBgHko\\nUe8H7IGd1+WMVG7yOKaQUzWggX2cdY38gmql0uME2PED8ByCJ6TLEKG2ap8OuvAR\\nLN78ILeN/cljc9KWi/WOv7UDl0ys3Fa+rTlHFAyvUtsjf3JBNv1qwIuaPGezloUU\\niYsXQAQ6JrT5yuYwYtH1eq701RaTHqjSR5MdEE+fAoGAAT9jlCVt92BG97bKppT7\\nJkNg+TMampe2d8SPXIqcJhMBoPK5nqUeDWQZyj/fkMesZFzRyPCa11MuIj/z4m26\\nNCMAalF/UhS4qfvz351wl8GeC6OHSn5D+2GIzXA+daiuRG5OnoepDq+UJtgiVH2u\\nEUJXpLOBFkE4wG331rBahhc=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-91hcy@bestyou-26e86.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"102373516690711524227\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-91hcy%40bestyou-26e86.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";

            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(Lists.newArrayList(firebaseMessagingScope));

            googleCredentials.refresh();

            return googleCredentials.getAccessToken().getTokenValue();


        } catch (IOException e){
            Log.e("error", "" + e.getMessage());
            return null;
        }
    }
}
