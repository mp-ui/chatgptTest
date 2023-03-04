package com.prince.chat;

import lombok.Data;

import java.util.List;

/**
 * @author Prince
 * @date 2023/3/4 14:17
 */
@Data
public class ChatRequest {
    private String model;
    private List<ChatMessage> messages;
    private Double temperature;
    private Long maxTokens;
    private String user;
}
