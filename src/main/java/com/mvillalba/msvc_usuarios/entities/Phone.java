package com.mvillalba.msvc_usuarios.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Phone {
    @Column(name = "NUMBER_PHONE")
    private Long number;
    @Column(name = "CITYCODE_PHONE")
    private Integer cityCode;
    @Column(name = "COUNTRYCODE_PHONE")
    private String countryCode;

    // Getters and Setters
}
