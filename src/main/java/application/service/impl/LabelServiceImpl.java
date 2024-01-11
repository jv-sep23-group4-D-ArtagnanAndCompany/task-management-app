package application.service.impl;

import application.dto.label.LabelRequestDto;
import application.dto.label.LabelResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.LabelMapper;
import application.model.Label;
import application.repository.LabelRepository;
import application.service.LabelService;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private static final String CANT_FIND_BY_ID = "Can't find label with id: ";
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    public LabelResponseDto create(LabelRequestDto labelRequestDto) {
        Label label = labelMapper.toEntity(labelRequestDto);
        Label savedLabel = labelRepository.save(label);
        return labelMapper.toResponseDto(savedLabel);
    }

    @Override
    public Set<LabelResponseDto> getAllByIds(Pageable pageable) {
        return labelRepository.findAll(pageable).stream()
                .map(labelMapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    @Override
    public LabelResponseDto update(LabelRequestDto labelRequestDto, Long id) {
        Label label = labelRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(CANT_FIND_BY_ID + id));
        label.setName(labelRequestDto.getName())
                .setColor(labelRequestDto.getColor());
        Label savedLabel = labelRepository.save(label);
        return labelMapper.toResponseDto(savedLabel);
    }

    @Override
    public void deleteById(Long id) {
        labelRepository.deleteById(id);
    }
}
