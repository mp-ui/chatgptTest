package com.prince.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author Prince
 * @date 2023/3/4 12:40
 */
public class ChatGPTClient {
    private static final String API_KEY = "API_KEY";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private static final String EXIT_STRING = "exit";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * fastjson配置
     */
    private static final SerializeConfig serializeConfig = new SerializeConfig();

    public static void main(String[] args) throws IOException {
        // 设置代理
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");

        // 配置fastjson为下划线分割
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

        System.out.println("Welcome to the ChatGPT console app!");
        System.out.println("Type 'exit' to quit at any time.\n");

        // 保存对话的上下文
        List<ChatMessage> messages = new ArrayList<>();
        // messages.add(new ChatMessage("system", "You are a helpful assistant."));

        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print("You: ");
            input = scanner.nextLine();
            if (input.equals(EXIT_STRING)) {
                System.out.println("bye! ");
                System.exit(0);
            }
            messages.add(new ChatMessage("user", input));
            String response = getResponse(messages);
            System.out.println("ChatGPT: " + response + "\n");
        }
    }

    /**
     * 获取ChatGPT的回复
     *
     * @param messages 上下文信息
     * @return ChatGPT的回答
     */
    private static String getResponse(List<ChatMessage> messages) {
        MediaType mediaType = MediaType.parse("application/json");

        ChatRequest chatRequest = new ChatRequest();
        // chatRequest.setModel("gpt-3.5-turbo-0301");
        chatRequest.setModel("gpt-3.5-turbo");
        chatRequest.setTemperature(0.7);
        chatRequest.setMessages(messages);
        // chatRequest.setMaxTokens(2048L);
        chatRequest.setUser("LiuDuck");

        String chatRequestString = JSON.toJSONString(chatRequest, serializeConfig);

        RequestBody requestBody = RequestBody.create(mediaType, chatRequestString);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            ChatResponse chatResponse = JSON.parseObject(responseBody, ChatResponse.class);
            ChatMessage cm = chatResponse.getChoices().get(0).getMessage();
            messages.add(cm);
            return cm.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            messages.remove(messages.size() - 1);
            return e.getMessage();
        }
    }
}


