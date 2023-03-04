package com.prince.chat;

import lombok.Data;

/**
 * @author Prince
 * @date 2023/3/4 14:12
 */
@Data
public class ChatChoice {
    private Long index;
    private ChatMessage message;
    private String finishReason;
}
