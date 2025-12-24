package com.ssafy.yamyam_coach.global.config;

import com.google.genai.Client;
import com.google.genai.types.HttpOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AiConfig {

    @Value("${spring.ai.google.genai.api-key}")
    private String genaiApiKey;

    @Value("${spring.ai.google.genai.base-url}")
    private String genaiBaseUrl;

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    }

    // 1. Gemini 모델 수동 등록 (Qualifier 부여)
    @Bean
    public ChatModel geminiChatModel() {
        Client client = Client.builder()
                .apiKey(genaiApiKey)
                .httpOptions(HttpOptions.builder().baseUrl(genaiBaseUrl).build())
                .build();

        GoogleGenAiChatOptions options = GoogleGenAiChatOptions.builder()
                .model("gemini-2.0-flash")
                .temperature(0.7)
                .build();

        return GoogleGenAiChatModel.builder()
                .genAiClient(client)
                .defaultOptions(options)
                .build();
    }

    // 2. OpenAI용 ChatClient 설정
    @Bean
    @Qualifier("openAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("너는 OpenAI 기반의 영양 전문가 '쩝쩝교수'야.")
                .build();
    }

    // 3. Gemini용 ChatClient 설정
    @Bean
    @Qualifier("geminiChatClient")
    public ChatClient geminiChatClient(@Qualifier("geminiChatModel") ChatModel geminiChatModel) {
        return ChatClient.builder(geminiChatModel)
                .defaultSystem("너는 Google Gemini 기반의 영양 전문가 '쩝쩝교수'야.")
                .build();
    }

    // 4. 기본 ChatClient 지정 (서비스에서 이름 없이 주입받을 때 사용)
    @Bean
    @Primary
    public ChatClient chatClient(@Qualifier("openAiChatClient") ChatClient openAiChatClient) {
        return openAiChatClient;
    }
}