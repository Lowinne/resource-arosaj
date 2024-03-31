package com.epsi.arosaj.web.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseFile {

    private String name;
    private String type;
    private long size;
    private byte[] data;

    public ResponseFile(String name, String type, long size, byte[] data) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.data = data;
    }
}
