package com.djeno.lab1.persistence.enums;

import java.util.Set;

public enum Role {
    ROLE_USER(Set.of(
            Privilege.DOWNLOAD_APP,
            Privilege.PURCHASE_APP,
            Privilege.ADD_PAYMENT_METHOD,
            Privilege.SET_PRIMARY_PAYMENT_METHOD,
            Privilege.VIEW_PAYMENT_METHOD,
            Privilege.DELETE_PAYMENT_METHOD,
            Privilege.CREATE_REVIEW,
            Privilege.DELETE_REVIEW
    )),
    ROLE_DEVELOPER(Set.of(
            Privilege.PUBLISH_APP,
            Privilege.DELETE_APP,
            Privilege.DOWNLOAD_APP
    )),
    ROLE_ADMIN(Set.of(
            Privilege.DELETE_APP,
            Privilege.DOWNLOAD_APP,
            Privilege.DELETE_REVIEW));

    private final Set<Privilege> privileges;

    Role(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }
}
