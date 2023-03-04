package com.prince.chat;

import lombok.Data;

import java.util.List;

/**
 * @author Prince
 * @date 2023/3/4 14:11
 */
@Data
public class ChatResponse {
    private String id;
    private String object;
    private Long created;
    private List<ChatChoice> choices;
    private ChatUsage usage;
}
