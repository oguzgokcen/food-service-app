package com.restaurant.service;

import com.restaurant.dto.TagDto;
import com.restaurant.entity.Tag;
import com.restaurant.exception.TagNotFoundException;
import com.restaurant.model.CustomResponseMessages;
import com.restaurant.model.ResponseMessage;
import com.restaurant.populator.TagDtoPopulator;
import com.restaurant.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagDtoPopulator tagDtoPopulator;

    public ResponseEntity<ResponseMessage> createTag(String tagName) {
        if (tagRepository.findByName(tagName).isPresent())
            return ResponseEntity.internalServerError().body(new ResponseMessage(CustomResponseMessages.TAG_ALREADY_EXISTS, HttpStatus.INTERNAL_SERVER_ERROR));
        tagRepository.save(Tag.builder().name(tagName).build());
        return ResponseEntity.ok(new ResponseMessage(CustomResponseMessages.TAG_CREATED, HttpStatus.OK));
    }

    public Set<Tag> getTagsFromRequest(Set<String> tagNameSet) {
        List<Tag> tagList = tagRepository.findAll();
        return tagNameSet.stream().map(e -> {
            try {
                return tagList.stream().filter(x -> x.getName().equalsIgnoreCase(e)).findFirst().orElseThrow(() -> new TagNotFoundException(CustomResponseMessages.TAG_NOT_FOUND));
            } catch (TagNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toSet());
    }

    public List<TagDto> getAllTags() {
        return tagDtoPopulator.populateAll(tagRepository.findAll());
    }
}
