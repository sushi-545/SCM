package com.pro.scm.helper;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String content;
    private MessageType type = MessageType.blue;
}
