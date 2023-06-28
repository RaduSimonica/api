package ro.crownstudio.api.enums;

import lombok.Getter;

public enum Status {
    STARTED(false),
    PASSED(true),
    FAILED(true),
    SKIPPED(true);

    @Getter
    private final boolean isEnding;

    Status(boolean isEnding) {
        this.isEnding = isEnding;
    }
}
