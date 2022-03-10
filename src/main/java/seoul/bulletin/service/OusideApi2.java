package seoul.bulletin.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLEncoder;

public class OusideApi2 {
    public static void main(String[] args) throws UnsupportedEncodingException, MalformedURLException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1390802/AgriFood/FdImage/getKoreanFoodFdImageList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=csd/9isLnOcfaTZ9sdpArdEmVSBmX2L2Ml2Upn348u0yPkPYDAqp/LkA1zWCvUKMk8/1CZIiPuDhKxvp/JmuCw=="); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("service_Type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 과 json 형식 지원*/
        urlBuilder.append("&" + URLEncoder.encode("Page_No","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("Page_Size","UTF-8") + "=" + URLEncoder.encode("2", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("food_Name", "UTF-8") + "=" + URLEncoder.encode("콩", "UTF-8")); /*음식 명 (검색어 입력값 포함 검색)*/

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(URI.create(urlBuilder.toString()), HttpMethod.GET, entity, String.class);
        System.out.println("response.getBody() = " + response.getBody());

    }
}
