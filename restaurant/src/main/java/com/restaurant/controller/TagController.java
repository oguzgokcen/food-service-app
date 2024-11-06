package com.restaurant.controller;

import com.restaurant.dto.TagDto;
import com.restaurant.model.ResponseMessage;
import com.restaurant.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createTag(@RequestParam String tagName) {
        return tagService.createTag(tagName);
    }

    @GetMapping
    public List<TagDto> getAllTags() {
        return tagService.getAllTags();
    }
}
