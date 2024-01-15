package application.service;

import application.dto.attachment.FileUploadResponseDto;
import application.model.Attachment;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DropBoxService {
    private static final String EXCEPTION_UPLOADING = "Can't upload a file ";
    private static final Integer NUMBER_OF_BYTES = 8092;
    private static final Integer MINUS_ONE = -1;
    private static final Integer ZERO = 0;
    private static final String EXCEPTION_DOWNLOADING = "Can't download a file by file id ";
    private final DbxClientV2 dbxClientV2;
    @Value("${DROP_BOX_USERS_FILES_PATH}")
    private String path;

    public FileUploadResponseDto uploadFileToDropBox(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String pathOfFile = String.format("%s/%s", path, multipartFile.getOriginalFilename());
            FileMetadata fileMetadata = dbxClientV2.files().uploadBuilder(pathOfFile)
                    .withMode(WriteMode.ADD)
                    .withMute(false)
                    .withAutorename(false)
                    .withStrictConflict(false)
                    .uploadAndFinish(inputStream);
            return new FileUploadResponseDto()
                    .setFileName(multipartFile.getOriginalFilename())
                    .setDropBoxFileId(fileMetadata.getId());
        } catch (IOException | DbxException e) {
            throw new RuntimeException(EXCEPTION_UPLOADING + multipartFile.getOriginalFilename());
        }
    }

    public void downloadFromDropBox(Attachment attachment, HttpServletResponse response) {
        try (InputStream inputStream = dbxClientV2.files().download(attachment.getDropBoxFileId())
                .getInputStream()) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Filename", attachment.getFileName());
            response.setHeader("DropBoxFileId", attachment.getDropBoxFileId());
            response.setHeader("Cache-Control", "no-cache");
            byte[] buffer = new byte[NUMBER_OF_BYTES];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != MINUS_ONE) {
                response.getOutputStream().write(buffer, ZERO, bytesRead);
            }
            response.getOutputStream().flush();
        } catch (DbxException | IOException e) {
            throw new RuntimeException(EXCEPTION_DOWNLOADING + attachment.getDropBoxFileId());
        }
    }
}
