package com.sdu_ai_lab.project_tracker.mappers;

import com.sdu_ai_lab.project_tracker.dto.responses.TagResponse;
import com.sdu_ai_lab.project_tracker.entities.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponse toDto(Tag entity);
    Tag toEntity(TagResponse dto);
}
