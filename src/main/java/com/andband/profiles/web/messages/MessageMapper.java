package com.andband.profiles.web.messages;

import com.andband.profiles.persistence.message.Message;
import com.andband.profiles.persistence.message.view.MessageView;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    Message dtoToEntity(MessageDTO messageDTO);

    List<MessageDTO> entityToDTO(List<MessageView> messages);

}
