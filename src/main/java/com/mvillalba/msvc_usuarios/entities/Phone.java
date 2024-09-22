package com.mvillalba.msvc_usuarios.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Setter
@Getter
public class Phone {
    @Column(name = "NUMBER_PHONE")
    private String number;
    @Column(name = "CITYCODE_PHONE")
    private String cityCode;
    @Column(name = "COUNTRYCODE_PHONE")
    private String countryCode;

    // Getters and Setters
}
