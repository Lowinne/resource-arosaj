package com.epsi.arosaj.web.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseFile {

    private String name;
    private String url;
    private String type;
    private long size;

    public ResponseFile(String name, String url, String type, long size) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
    }
}
