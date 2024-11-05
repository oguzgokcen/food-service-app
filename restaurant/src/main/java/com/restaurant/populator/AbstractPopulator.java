package com.restaurant.populator;

import java.util.List;

public abstract class AbstractPopulator<Source, Target> {
    public Target populate(Source source) {
        return this.populate(source, getTarget());
    }

    public abstract Target populate(Source source, Target target);

    public List<Target> populateAll(List<Source> sources) {
        return sources.stream().map(this::populate).toList();
    }

    public abstract Target getTarget();

}
