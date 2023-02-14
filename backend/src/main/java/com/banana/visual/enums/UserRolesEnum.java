package com.banana.visual.enums;

import lombok.Getter;

@Getter
public enum UserRolesEnum {

    HIGH("admin"),
    MIDDLE("manager"),
    LOW("staff");

    private String permission;

    UserRolesEnum(String permission) {
        this.permission = permission;
    }
}
