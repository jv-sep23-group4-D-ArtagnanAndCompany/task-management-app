package application.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbxClientV2Config {
    @Value("${drop_box_access_token}")
    private String dropBoxFileToken;

    @Bean
    public DbxClientV2 createDbxClientV2() {
        DbxRequestConfig dbxRequestConfig
                = DbxRequestConfig.newBuilder("dropbox/java-tutorial")
                .build();
        return new DbxClientV2(dbxRequestConfig, dropBoxFileToken);
    }
}
