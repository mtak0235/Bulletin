package seoul.bulletin.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import seoul.bulletin.dto.PostsSaveRequestDto;

@Component
@RequiredArgsConstructor
public class Json2XService {
    private final JSONParser jsonParser = new JSONParser();

    public PostsSaveRequestDto json2PostsSaveRequestDto(String givenData) throws ParseException {
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
}
