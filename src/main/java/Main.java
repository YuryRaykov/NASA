import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=7c6ajzJo960GSfDsTBAHtiVfdm8DRTwgU7cR66L8";
    public static ObjectMapper mapper = new ObjectMapper(); //ObjectMapper осуществляет десирилизацию и серилизацию
//    public static String REMOTE_SERVICE_URI2 = a;

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create() // создаем htpp клиент и кофигурируем его
                .setUserAgent("My Test Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000) // время ожидания для подключения (5 сек.)
                        .setSocketTimeout(30000) // сколько держим сокет открытым (как мы подключились не более 30 сек)
                        .setRedirectsEnabled(false) // не отправляем повторный запрос
                        .build())
                .build();
// создание объекта запроса с произвольными заголовками
        HttpGet request = new HttpGet(REMOTE_SERVICE_URI); // указываем на какой адрес отправляем запрос
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType()); // указываем какой формат мы поддерживаем
// отправка запроса
        CloseableHttpResponse response = httpClient.execute(request); // вызываем метод execute передав ему объект запроса request

// вывод полученных заголовков
        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

//         чтение тела ответа
//        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
//        System.out.println(body);
        Post post = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
        });
//        System.out.println(post);
        System.out.println(post.getUrl());
        String a = post.getUrl();

        // создание объекта запроса с произвольными заголовками
        HttpGet request1 = new HttpGet(a); // указываем на какой адрес отправляем запрос
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType()); // указываем какой формат мы поддерживаем
// отправка запроса
        CloseableHttpResponse response1 = httpClient.execute(request1); // вызываем метод execute передав ему объект запроса request

// вывод полученных заголовков
        Arrays.stream(response1.getAllHeaders()).forEach(System.out::println);

        URL url = new URL(a);
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1 != (n = in.read(buf))) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response2 = out.toByteArray();

        FileOutputStream fos = new FileOutputStream("D://fromNASA.jpg");
        fos.write(response2);
        fos.close();

        response.close();
        httpClient.close();
    }

}
