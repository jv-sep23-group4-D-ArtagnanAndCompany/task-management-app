package application.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.exception.EntityNotFoundException;
import application.model.User;
import java.sql.Connection;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    private static final Long USER_ID = 3L;
    private static final String CLASS_PATH_RESOURCE_ADD =
            "database/users/add_two_default_users.sql";
    private static final String CLASS_PATH_RESOURCE_DELETE =
            "database/users/remove_two_added_users.sql";
    private static final String CANT_FIND_BY_ID = "The user with id %s was not found";
    private static final String USER_PASSWORD =
            "$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG";
    private static final String USER_EMAIL = "john@gmail.com";
    private static final String USER_FIRST_NAME = "John";
    private static final String USER_LAST_NAME = "Lollipop";
    private static final String USER_USER_NAME = "John";
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    @SneakyThrows
    static void beforeAll(
            @Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            CLASS_PATH_RESOURCE_ADD)
            );
        }
    }

    @AfterAll
    @SneakyThrows
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @Test
    @DisplayName("Get user by id")
    public void findUserById_WithUser_ReturnUser() {
        // Given
        User user = new User();
        user.setId(USER_ID);
        user.setUserName(USER_USER_NAME);
        user.setPassword(USER_PASSWORD);
        user.setEmail(USER_EMAIL);
        user.setFirstName(USER_FIRST_NAME);
        user.setLastName(USER_LAST_NAME);

        // When
        User actualUser = userRepository.findUserById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        CANT_FIND_BY_ID, user.getId())));
        // Then
        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            CLASS_PATH_RESOURCE_DELETE)
            );
        }
    }
}
