package com.gaeko.gamecut.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final int MAX_HISTORY = 10;

    @Value("${gpt.api.key}")
    private String API_KEY;

    public String askGpt(String userId, String userMessage) throws IOException {
        List<Map<String, String>> history = getChatHistory(userId);

        // 오래된 히스토리를 요약 처리
        if (history.size() > MAX_HISTORY) {
            List<Map<String, String>> toSummarize = history.subList(0, history.size() - MAX_HISTORY);
            String summaryText = summarizeOldMessages(toSummarize);

            List<Map<String, String>> recent = history.subList(history.size() - MAX_HISTORY, history.size());
            history = new ArrayList<>();
            history.add(Map.of("role", "system", "content", "너는 친절하고 간결한 말투로 대답하는 한국어 AI 챗봇이야. 지금까지의 대화 요약: " + summaryText));
            history.addAll(recent);
        } else {
            // system 메시지가 없으면 기본 역할만 추가
            history.removeIf(m -> "system".equals(m.get("role"))); // 혹시 남아있을 수도 있으니 제거
            history.add(0, Map.of("role", "system", "content", "너는 친절하고 간결한 말투로 대답하는 한국어 AI 챗봇이야."));
        }

        // 사용자가 입력한 메시지 추가
        history.add(Map.of("role", "user", "content", userMessage));

        // GPT 요청
        String jsonBody = mapper.writeValueAsString(Map.of(
                "model", "gpt-3.5-turbo",
                "messages", history
        ));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                throw new IOException("GPT API 요청 실패: " + response.code() + " / " + responseBody);
            }

            JsonNode root = mapper.readTree(responseBody);
            String reply = root.get("choices").get(0).get("message").get("content").asText();

            // assistant 응답 저장
            history.add(Map.of("role", "assistant", "content", reply));
            saveChatHistory(userId, history);

            return reply;
        }
    }

    public List<Map<String, String>> getChatHistory(String userId) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get("chat:" + userId);
        if (json == null) return new ArrayList<>();
        return mapper.readValue(json, List.class);
    }

    public void saveChatHistory(
            String userId,
            List<Map<String, String>> history
    ) throws JsonProcessingException {
        redisTemplate.opsForValue().set("chat:" + userId, mapper.writeValueAsString(history));
    }

    public String summarizeOldMessages(List<Map<String, String>> messages) throws IOException {
        String joined = messages.stream()
                                .map(msg -> msg.get("role") + ": " + msg.get("content"))
                                .collect(Collectors.joining("\n"));

        List<Map<String, String>> prompt = List.of(
                Map.of("role", "system", "content", "다음 대화를 핵심만 간단하게 요약해줘."),
                Map.of("role", "user", "content", joined)
        );

        String jsonBody = mapper.writeValueAsString(Map.of(
                "model", "gpt-3.5-turbo",
                "messages", prompt
        ));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                throw new IOException("요약 GPT 요청 실패: " + response.code() + " / " + responseBody);
            }

            JsonNode root = mapper.readTree(responseBody);
            return root.get("choices").get(0).get("message").get("content").asText();
        }
    }

    public void resetChat(String userId) {
        redisTemplate.delete("chat:" + userId);
    }
}
