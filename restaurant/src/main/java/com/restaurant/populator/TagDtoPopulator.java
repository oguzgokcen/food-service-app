package com.restaurant.populator;

import com.restaurant.dto.TagDto;
import com.restaurant.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagDtoPopulator extends AbstractPopulator<Tag, TagDto> {
    @Override
    public TagDto populate(Tag tag, TagDto tagDto) {
        tagDto.setName(tag.getName());
        return tagDto;
    }

    @Override
    public TagDto getTarget() {
        return new TagDto();
    }
}
