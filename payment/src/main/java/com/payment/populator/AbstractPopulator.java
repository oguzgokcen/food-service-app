package com.payment.populator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractPopulator <Source,Target>{
    public Target populate(Source source) {
        return this.populate(source, getTarget());
    }

    public abstract Target populate(Source source, Target target);

    public List<Target> populateAll(List<Source> sources) {
        return sources.stream().map(this::populate).toList();
    }

    public Set<Target> populateAllSet(Set<Source> sources) {
        return sources.stream().map(this::populate).collect(Collectors.toSet());
    }

    public abstract Target getTarget();

}
