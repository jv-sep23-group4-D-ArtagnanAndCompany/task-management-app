package application.service;

import application.dto.attachment.FileUploadResponseDto;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class DropBoxService {
    private static final String EXCEPTION_UPLOADING = "Can't upload file ";
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

    public OutputStream downloadFromDropBox(String dropBoxFileId) {
        try {
            InputStream inputStream
                    = dbxClientV2.files().download(dropBoxFileId).getInputStream();
            byte [] bytes = inputStream.readAllBytes();
            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            byteArrayInputStream.write(bytes);
            return byteArrayInputStream;
        } catch (DbxException | IOException e) {
            throw new RuntimeException(EXCEPTION_DOWNLOADING + dropBoxFileId);
        }
    }
}
