package com.epsi.arosaj.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@ToString
@Getter
@Setter
public class MessageDto {
    private String receiver;
    private String sender;
    private String content;
    private Date date;
}
