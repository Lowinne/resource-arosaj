package com.epsi.arosaj.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseFileDto {

    private String name;
    private String url;
    private String type;
    private long size;

    public ResponseFileDto(String name, String url, String type, long size) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
    }
}
