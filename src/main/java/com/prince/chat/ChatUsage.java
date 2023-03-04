package com.prince.chat;

import lombok.Data;

/**
 * @author Prince
 * @date 2023/3/4 14:14
 */
@Data
public class ChatUsage {
    private Long promptTokens;
    private Long completionTokens;
    private Long totalTokens;
}
