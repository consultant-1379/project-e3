package com.ericsson.graduates.projecte3.ENUM;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum ItemCategory {
    SAD,

    @JsonEnumDefaultValue
    MAD,
    GLAD
}
