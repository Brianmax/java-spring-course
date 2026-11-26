package com.codigo.codigo.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReniecResponse {

    @JsonAlias("first_name")
    private String firstName;

    @JsonAlias("first_last_name")
    private String firstLastName;

    @JsonAlias("second_last_name")
    private String secondLastName;

    @JsonAlias("full_name")
    private String fullName;

    @JsonAlias("document_number")
    private String documentNumber;
}