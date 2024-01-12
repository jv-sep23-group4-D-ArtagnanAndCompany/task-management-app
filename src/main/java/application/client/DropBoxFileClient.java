package application.client;

import application.dto.drop_box.DropBoxFileGetDto;
import application.dto.drop_box.DropBoxFileUploadDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class DropBoxFileClient {
    private static final String URL_UPLOAD = "https://api.dropboxapi.com/2/file_requests/create";
    private static final String URL_GET = "https://api.dropboxapi.com/2/file_requests/get";
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    @Value("${DROP_BOX_ACCESS_TOKEN")
    private String dropBoxFileToken;
    public DropBoxFileUploadDto uploadFileToDropBoxFile(String fileName) {
        String jsonBody = "{\"title\": \"" + fileName + "\"}";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .uri(URI.create(URL_UPLOAD))
                .header("Authorization", "Bearer " + dropBoxFileToken)
                .build();
        try {
            HttpResponse<String> httpResponse
                    = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(httpResponse.body(), DropBoxFileUploadDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public DropBoxFileGetDto getDto(String dropBoxFileId) {
        String jsonBody = "{\"id\": \"" + dropBoxFileId + "\"}";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .uri(URI.create(URL_GET))
                .header("Authorization", "Bearer " + dropBoxFileToken)
                .build();
        try {
            HttpResponse<String> httpResponse
                    = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(httpResponse.body(), DropBoxFileGetDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
