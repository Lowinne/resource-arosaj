package com.epsi.arosaj.persistence.dto.mapper;

import com.epsi.arosaj.persistence.dto.MessageDto;
import com.epsi.arosaj.persistence.model.Message;

public class MessageMapper {

    public static MessageDto convertMessageToMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setSender(message.getExpediteur().getPseudo());
        messageDto.setReceiver(message.getDestinataire().getPseudo());
        messageDto.setContent(message.getMessage());
        messageDto.setDate(message.getDate());

        return messageDto;
    }


}
