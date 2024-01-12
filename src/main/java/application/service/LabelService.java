package application.service;

import application.dto.label.LabelRequestDto;
import application.dto.label.LabelResponseDto;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface LabelService {
    LabelResponseDto create(LabelRequestDto labelRequestDto);

    Set<LabelResponseDto> getAllByIds(Pageable pageable);

    LabelResponseDto update(LabelRequestDto labelRequestDto, Long id);

    void deleteById(Long id);
}
