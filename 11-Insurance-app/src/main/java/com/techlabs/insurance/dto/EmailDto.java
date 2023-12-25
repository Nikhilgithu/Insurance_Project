package com.techlabs.insurance.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
	private String sender;
	private List<String> recipients;
    private String msgBody;
    private String subject;
    private String attachment;
}
