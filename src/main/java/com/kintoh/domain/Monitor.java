package com.kintoh.domain;

import java.util.Optional;

public interface Monitor {
    Optional<Event> check(Resource resource);
}
