package com.techlabs.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class DocumentDto {

    private String documentType;
    private String documentName;
    private byte[] documentFile;
}