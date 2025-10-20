package com.sdu_ai_lab.project_tracker.mappers;

import com.sdu_ai_lab.project_tracker.dto.responses.ProjectResponse;
import com.sdu_ai_lab.project_tracker.entities.Image;
import com.sdu_ai_lab.project_tracker.entities.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TagMapper.class})
public interface ProjectMapper {
    @Mappings({
            @Mapping(target = "imagePaths", expression = "java(mapImagePaths(entity))")
    })
    ProjectResponse toDto(Project entity);

    // helper for mapping image paths
    default List<String> mapImagePaths(Project entity) {
        if (entity == null || entity.getImages() == null) return List.of();
        return entity.getImages().stream()
                .map(Image::getRelativePath)
                .collect(Collectors.toList());
    }
}
