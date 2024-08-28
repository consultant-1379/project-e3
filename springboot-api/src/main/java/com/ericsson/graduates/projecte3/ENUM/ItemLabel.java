package com.ericsson.graduates.projecte3.ENUM;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum ItemLabel {
    EPIC,
    STORY,

    @JsonEnumDefaultValue
    TASK,
    SUBTASK
}
