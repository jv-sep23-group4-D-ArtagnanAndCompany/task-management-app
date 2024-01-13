package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.dto.label.LabelRequestDto;
import application.dto.label.LabelResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.LabelMapper;
import application.model.Label;
import application.repository.LabelRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class LabelServiceImplTest {
    private static final String EXCEPTION_MESSAGE = "Can't find label with id: ";
    private static final Integer ONE_TIME = 1;
    private static final Long CORRECT_LABEL_ID = 2L;
    private static final Long INCORRECT_LABEL_ID = 20L;
    private static final Integer RED_LABEL_ID = 0;
    private static final Integer YELLOW_LABEL_ID = 1;
    private static final Integer GREEN_LABEL_ID = 2;
    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static List<Label> labels;
    private static List<LabelResponseDto> labelResponseDtos;
    @Mock
    private LabelRepository labelRepository;
    @Mock
    private LabelMapper labelMapper;
    @InjectMocks
    private LabelServiceImpl labelServiceImpl;

    @BeforeAll
    static void beforeAll() {
        Label redLabel = new Label().setName("red label").setColor("red");
        Label yellowLabel = new Label().setName("yellow label").setColor("yellow");
        Label greenLabel = new Label().setName("green label").setColor("green");
        labels = List.of(redLabel, yellowLabel, greenLabel);
        LabelResponseDto redLabelDto = new LabelResponseDto()
                .setName(redLabel.getName()).setColor(redLabel.getColor());
        LabelResponseDto yellowLabelDto = new LabelResponseDto()
                .setName(yellowLabel.getName()).setColor(yellowLabel.getColor());
        LabelResponseDto greenLabelDto = new LabelResponseDto()
                .setName(greenLabel.getName()).setColor(greenLabel.getColor());
        labelResponseDtos = List.of(redLabelDto, yellowLabelDto, greenLabelDto);
    }

    @Test
    @DisplayName("""
            Verify create() method
            """)
    void create_ValidRequestDto_ReturnResponseDto() {
        // given
        LabelRequestDto labelRequestDto = new LabelRequestDto()
                .setName("red label").setColor("red");

        // when
        when(labelMapper.toEntity(labelRequestDto))
                .thenReturn(labels.get(RED_LABEL_ID));
        when(labelRepository.save(labels.get(RED_LABEL_ID)))
                .thenReturn(labels.get(RED_LABEL_ID));
        when(labelMapper.toResponseDto(labels.get(RED_LABEL_ID)))
                .thenReturn(labelResponseDtos.get(RED_LABEL_ID));

        // then
        LabelResponseDto actual = labelServiceImpl.create(labelRequestDto);
        LabelResponseDto expected = labelResponseDtos.get(RED_LABEL_ID);
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(labelRepository, times(ONE_TIME)).save(labels.get(RED_LABEL_ID));

    }

    @Test
    @DisplayName("""
            Verify getAllByIds() method
            """)
    void getAllByIds_ValidPageable_ReturnExpectedLabelDtoList() {
        // given
        PageRequest pageRequest = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        PageImpl page = new PageImpl(labels);

        // when
        when(labelRepository.findAll(pageRequest)).thenReturn(page);
        when(labelMapper.toResponseDto(labels.get(RED_LABEL_ID)))
                .thenReturn(labelResponseDtos.get(RED_LABEL_ID));
        when(labelMapper.toResponseDto(labels.get(YELLOW_LABEL_ID)))
                .thenReturn(labelResponseDtos.get(YELLOW_LABEL_ID));
        when(labelMapper.toResponseDto(labels.get(GREEN_LABEL_ID)))
                .thenReturn(labelResponseDtos.get(GREEN_LABEL_ID));

        // then
        Set<LabelResponseDto> actual = labelServiceImpl.getAllByIds(pageRequest);
        assertEquals(3, actual.size());
        assertEquals(new HashSet<>(labelResponseDtos), actual);
        verify(labelRepository, times(ONE_TIME)).findAll(pageRequest);
    }

    @Test
    @DisplayName("""
            Verify getAllByIds() method
            """)
    void getAllByIds_EmptyDb_ReturnEmptyList() {
        // given
        PageRequest pageRequest = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        PageImpl page = new PageImpl(new ArrayList());

        // when
        when(labelRepository.findAll(pageRequest)).thenReturn(page);

        // then
        Set<LabelResponseDto> actual = labelServiceImpl.getAllByIds(pageRequest);
        assertEquals(0, actual.size());
        verify(labelRepository, times(ONE_TIME)).findAll(pageRequest);
    }

    @Test
    @DisplayName("""
            Verify update() method
            """)
    void update_ValidRequestDto_ReturnResponseDto() {
        // given
        LabelRequestDto labelRequestDto = new LabelRequestDto()
                .setName("black label").setColor("black");
        Label label = new Label().setId(CORRECT_LABEL_ID)
                        .setName(labelRequestDto.getName())
                .setColor(labelRequestDto.getColor());
        LabelResponseDto labelResponseDto = new LabelResponseDto().setId(label.getId())
                        .setName(label.getName()).setColor(label.getColor());

        // when
        when(labelRepository.findById(CORRECT_LABEL_ID))
                .thenReturn(Optional.of(labels.get(RED_LABEL_ID)));
        when(labelMapper.toEntity(labelRequestDto)).thenReturn(label);
        when(labelRepository.save(label)).thenReturn(label);
        when(labelMapper.toResponseDto(label)).thenReturn(labelResponseDto);

        // then
        LabelResponseDto actual = labelServiceImpl.update(labelRequestDto, CORRECT_LABEL_ID);
        assertNotNull(actual);
        assertEquals(labelResponseDto, actual);
        verify(labelRepository, times(ONE_TIME)).findById(CORRECT_LABEL_ID);
        verify(labelRepository, times(ONE_TIME)).save(label);
    }

    @Test
    @DisplayName("""
            Verify update() method
            """)
    void update_InvalidInputId_ThrowException() {
        // given
        LabelRequestDto labelRequestDto = new LabelRequestDto()
                .setName("black label").setColor("black");

        // when
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> labelServiceImpl.update(labelRequestDto, INCORRECT_LABEL_ID)
        );

        // then
        String expected = EXCEPTION_MESSAGE + INCORRECT_LABEL_ID;
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
