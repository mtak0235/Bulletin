package seoul.bulletin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import seoul.bulletin.domain.PostsRepository;
import seoul.bulletin.domain.entity.Posts;
import seoul.bulletin.dto.*;

import javax.transaction.Transactional;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final Post2XService post2XService;
    private         RestTemplate restTemplate = new RestTemplate();


    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        Posts savedPost = postsRepository.save(requestDto.toEntiy());
        try {
            postsRepository.saveOnFile(post2XService.post2File(savedPost));
            postsRepository.saveOnEmail(post2XService.post2Email(savedPost));
            postsRepository.saveOnExcel(post2XService.post2Excel(savedPost));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedPost.getId();
    }

    @Transactional
    public boolean delete(Long id) {
        Posts postToDelete = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id=" + id));
        postsRepository.delete(postToDelete);
        try{
            postsRepository.deleteOnFile(id);
            postsRepository.deleteOnEmail(id);
            postsRepository.deleteOnExcel(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !postsRepository.findById(id).isPresent();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("그런 게시글 없다. id" + id));
        post.update(requestDto.getTitle(), requestDto.getContent());
        try {
            postsRepository.updateOnFile(post2XService.post2File(post));
            postsRepository.updateOnEmail(post2XService.post2Email(post));
            postsRepository.updateOnExcel(post2XService.post2Excel(post));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post.getId();
    }

    @Transactional
    public PostsResponseDto findById(Long id) {
        Posts postOnDB = postsRepository.findById(id).get();
        PostOnFileDto postOnFile = null;
        PostOnExcelDto postOnExcel = null;
        try {
            postOnFile = postsRepository.findByIdOnFile(id);
            postOnExcel = postsRepository.findByIdOnExcel(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //post format = id + title + content + author
        //무슨 데이터를 줄지 조작 가능했으면 좋겠다.
        PostsResponseDto post = new PostsResponseDto(postOnDB, postOnFile, postOnExcel);
        return post;
    }

    @Transactional
    public List<PostsResponse4ListDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsResponse4ListDto::new)
                .collect(Collectors.toList());
    }

    public String getStringFromFile(String fileName) {
        String readline = null;
        StringBuffer readlineB = new StringBuffer();
        File file = new File(fileName);
        if (!file.isFile()) {
            return null;
        }
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            while ((readline = br.readLine()) != null) {
                readlineB.append(readline);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readlineB.toString();
    }

    @Transactional
    public PostsResponseDto getPostInMtakPostAPI(Long id) {
        return restTemplate.getForObject("http://localhost:8080/posts/api?id={id}",
                PostsResponseDto.class, id);
    }

    @Transactional
    public ResponseEntity<Long> savePostInMtakPostAPI(PostsResponseDto post) {
        return restTemplate.postForEntity("http://localhost:8080/posts/api", post, Long.class);
    }

    @Transactional
    public PostsSaveRequestDto getPostInOutsidePostApifake(String name) throws IOException, ParseException {

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1390802/AgriFood/FdImage/getKoreanFoodFdImageList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=csd/9isLnOcfaTZ9sdpArdEmVSBmX2L2Ml2Upn348u0yPkPYDAqp/LkA1zWCvUKMk8/1CZIiPuDhKxvp/JmuCw=="); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("service_Type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 과 json 형식 지원*/
        urlBuilder.append("&" + URLEncoder.encode("Page_No", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("Page_Size", "UTF-8") + "=" + URLEncoder.encode("2", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("food_Name", "UTF-8") + "=" + URLEncoder.encode("콩", "UTF-8")); /*음식 명 (검색어 입력값 포함 검색)*/

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange(URI.create(urlBuilder.toString()), HttpMethod.GET, entity, String.class);

        String givenData = resp.getBody();
        System.out.println("givenData = " + givenData);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(givenData);
        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONArray list = (JSONArray) response.get("list");
        JSONObject target = (JSONObject) list.get(0);
        PostsSaveRequestDto givenPost = PostsSaveRequestDto.builder()
                .title((String) target.get("upper_Fd_Grupp_Nm"))
                .content((String) target.get("fd_Nm"))
                .author((String) target.get("fd_Grupp_Nm"))
                .build();
        return givenPost;
    }

    public PostsSaveRequestDto getPostInOutsidePostApifake2(String name) throws IOException, ParseException, URISyntaxException {
//        URI uri = UriComponentsBuilder
//                .fromUriString("http://apis.data.go.kr/1390802/AgriFood/FdImage/getKoreanFoodFdImageList")
//                .queryParam("serviceKey", "csd/9isLnOcfaTZ9sdpArdEmVSBmX2L2Ml2Upn348u0yPkPYDAqp/LkA1zWCvUKMk8/1CZIiPuDhKxvp/JmuCw==")
//                .queryParam("service_Type", "json")
//                .queryParam("Page_No", "1")
//                .queryParam("Page_Size", "2")
//                .queryParam("food_Name", name)
//                .build()
//                .toUri();
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1390802/AgriFood/FdImage/getKoreanFoodFdImageList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=csd/9isLnOcfaTZ9sdpArdEmVSBmX2L2Ml2Upn348u0yPkPYDAqp/LkA1zWCvUKMk8/1CZIiPuDhKxvp/JmuCw=="); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("service_Type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 과 json 형식 지원*/
        urlBuilder.append("&" + URLEncoder.encode("Page_No", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("Page_Size", "UTF-8") + "=" + URLEncoder.encode("2", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("food_Name", "UTF-8") + "=" + URLEncoder.encode("콩", "UTF-8")); /*음식 명 (검색어 입력값 포함 검색)*/

        URI uri = new URI(urlBuilder.toString());
        System.out.println("uri.toString() = " + uri.toString());
        RequestEntity request = RequestEntity
                .get(uri)
                .header("Content-type", "application/json")
                .build();
        ResponseEntity<String> givenData = restTemplate.exchange(request, String.class);
        System.out.println("givenData = " + givenData);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(givenData.getBody());
        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONArray list = (JSONArray) response.get("list");
        JSONObject target = (JSONObject) list.get(0);
        PostsSaveRequestDto givenPost = PostsSaveRequestDto.builder()
                .title((String) target.get("upper_Fd_Grupp_Nm"))
                .content((String) target.get("fd_Nm"))
                .author((String) target.get("fd_Grupp_Nm"))
                .build();
        return givenPost;
    }
    public PostsSaveRequestDto getPostInOutsidePostApi(String name) throws IOException, ParseException, URISyntaxException {
//        String k = URLDecoder.decode("csd/9isLnOcfaTZ9sdpArdEmVSBmX2L2Ml2Upn348u0yPkPYDAqp/LkA1zWCvUKMk8/1CZIiPuDhKxvp/JmuCw==", "UTF-8");
        URI uri = UriComponentsBuilder
                .fromUriString("http://apis.data.go.kr/1390802/AgriFood/FdImage/getKoreanFoodFdImageList")
                .queryParam("serviceKey", "csd/9isLnOcfaTZ9sdpArdEmVSBmX2L2Ml2Upn348u0yPkPYDAqp/LkA1zWCvUKMk8/1CZIiPuDhKxvp/JmuCw==")
                .queryParam("service_Type", "json")
                .queryParam("Page_No", "1")
                .queryParam("Page_Size", "2")
                .queryParam("food_Name", name)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        System.out.println("uri.toString() = " + uri.toString());
        RequestEntity request = RequestEntity
                .get(uri)
                .header("Content-type", "application/json")
                .build();
        ResponseEntity<String> givenData = restTemplate.exchange(request, String.class);
        System.out.println("givenData = " + givenData);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(givenData.getBody());
        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONArray list = (JSONArray) response.get("list");
        JSONObject target = (JSONObject) list.get(0);
        PostsSaveRequestDto givenPost = PostsSaveRequestDto.builder()
                .title((String) target.get("upper_Fd_Grupp_Nm"))
                .content((String) target.get("fd_Nm"))
                .author((String) target.get("fd_Grupp_Nm"))
                .build();
        return givenPost;
    }
}
