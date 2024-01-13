package application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.label.LabelRequestDto;
import application.dto.label.LabelResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@Sql(scripts = "classpath:database/labels/remove_four_labels_from_labels_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LabelControllerTest {
    protected static MockMvc mockMvc;
    private static final String BLACK_ID = "/6";
    private static final String PINK_ID = "/7";
    private static final String USER = "user";
    private static final String BASE_PATH = "classpath:database/labels/";
    private static final String URL = "/api/labels";
    private static final String USER_ROLE = "USER";
    private static Set<LabelResponseDto> labelResponseDtos;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        LabelResponseDto redLabelDto = new LabelResponseDto()
                .setId(1L).setName("red label").setColor("red");
        LabelResponseDto blueLabelDto = new LabelResponseDto()
                .setId(2L).setName("blue label").setColor("blue");
        LabelResponseDto greenLabelDto = new LabelResponseDto()
                .setId(3L).setName("green label").setColor("green");
        LabelResponseDto yellowLabelDto = new LabelResponseDto()
                .setId(4L).setName("yellow label").setColor("yellow");
        labelResponseDtos = Set.of(redLabelDto, blueLabelDto, greenLabelDto, yellowLabelDto);
    }

    @Test
    @Sql(scripts = BASE_PATH + "remove_white_label_from_labels_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = USER, roles = USER_ROLE)
    @DisplayName("""
            Verify create() method
            """)
    void create_ValidRequestDto_ReturnResponseDto() throws Exception {
        // given
        LabelRequestDto labelRequestDto = new LabelRequestDto()
                .setName("white label")
                .setColor("white");
        String jsonBody = objectMapper.writeValueAsString(labelRequestDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)).andExpect(status().isCreated()).andReturn();

        // then
        LabelResponseDto expected = new LabelResponseDto()
                .setName(labelRequestDto.getName())
                .setColor(labelRequestDto.getColor());
        LabelResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(),
                LabelResponseDto.class);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = USER, roles = USER_ROLE)
    @DisplayName("""
            Verify getAll() method
            """)
    void getAll_NonEmptyDb_ReturnExpectedLabelDtoList() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get(URL)).andReturn();

        // then
        Set<LabelResponseDto> actual
                = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<Set<LabelResponseDto>>() {});
        assertEquals(4, actual.size());
        assertEquals(labelResponseDtos, actual);
    }

    @Test
    @Sql(scripts = BASE_PATH + "add_black_label_to_labels_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BASE_PATH + "remove_black_label_from_labels_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = USER, roles = USER_ROLE)
    @DisplayName("""
            Verify update() method
            """)
    void update_ValidRequestDto_ReturnResponseDto() throws Exception {
        // given
        LabelRequestDto labelRequestDto = new LabelRequestDto()
                .setName("white label")
                .setColor("white");
        String jsonBody = objectMapper.writeValueAsString(labelRequestDto);

        // when
        MvcResult mvcResult = mockMvc.perform(put(URL + BLACK_ID).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn();

        // then
        LabelResponseDto expected = new LabelResponseDto()
                .setId(6L)
                .setName(labelRequestDto.getName())
                .setColor(labelRequestDto.getColor());
        LabelResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(),
                LabelResponseDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify delete() method
            """)
    @Sql(scripts = BASE_PATH + "add_pink_label_to_labels_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = USER, roles = USER_ROLE)
    void delete_ValidInputId_Success() throws Exception {
        // when
        mockMvc.perform(delete(URL + PINK_ID));
        MvcResult mvcResult = mockMvc.perform(get(URL)).andReturn();

        Set<LabelResponseDto> actual = objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(),
                new TypeReference<Set<LabelResponseDto>>() {});
        assertEquals(4, actual.size());
        assertEquals(labelResponseDtos, actual);
    }
}
