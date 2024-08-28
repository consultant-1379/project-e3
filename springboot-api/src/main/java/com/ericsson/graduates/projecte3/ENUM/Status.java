package com.ericsson.graduates.projecte3.ENUM;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Status {
    @JsonEnumDefaultValue
    TBD,
    IN_PROGRESS,
    COMPLETED
}
