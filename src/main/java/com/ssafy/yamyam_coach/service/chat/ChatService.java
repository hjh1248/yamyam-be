package com.ssafy.yamyam_coach.service.chat;

import com.ssafy.yamyam_coach.controller.chat.request.ChatRequest;
import com.ssafy.yamyam_coach.domain.body_spec.BodySpec;
import com.ssafy.yamyam_coach.domain.challenge.Challenge;
import com.ssafy.yamyam_coach.domain.daily_diet.DailyDiet;
import com.ssafy.yamyam_coach.repository.body_spec.BodySpecRepository;
import com.ssafy.yamyam_coach.repository.challenge.ChallengeRepository;
import com.ssafy.yamyam_coach.repository.daily_diet.DailyDietRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final String promptTemplate = """
            당신은 '냠냠코치' 프로젝트의 영양 전문가 쩝쩝 교수 입니다.
            아래에 제공된 [참고 정보]를 바탕으로 사용자의 질문에 답변해 주세요.
            답변을 모를 경우 억지로 지어내지 말고 모른다고 답변하세요.
            
            아래 제공된 [배경 지식]과 [사용자 데이터]를 바탕으로 질문에 답변해 주세요.
            
            [배경 지식 (매뉴얼/영양 정보)]
            {rag_context}
            
            [사용자 데이터 (선택된 기록)]
            {user_context}
            
            [사용자 질문]
            {question}
            """;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    // Repository 주입
    private final BodySpecRepository bodySpecRepository;
    private final DailyDietRepository dailyDietRepository;
    private final ChallengeRepository challengeRepository;

    public String request(Long userId, ChatRequest request) {
        // 1. [RAG] 벡터 DB 검색
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(request.getContent())
                        .topK(3)
                        .similarityThreshold(0.7)
                        .build()
        );

        String ragContext = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        // 2. [Context Injection] 선택된 ID로 DB 조회 -> 문자열 변환
        String userContext = buildUserContext(request);

        // 3. [LLM] AI 호출
        return chatClient.prompt()
                .user(u -> u.text(promptTemplate)
                        .param("rag_context", ragContext.isEmpty() ? "관련 배경 지식 없음" : ragContext)
                        .param("user_context", userContext.isEmpty() ? "선택된 데이터 없음" : userContext)
                        .param("question", request.getContent())
                )
                .call()
                .content();
    }

    // 데이터를 문자열로 변환하는 로직
    private String buildUserContext(ChatRequest req) {
        StringBuilder sb = new StringBuilder();

        // A. 신체 정보
        if (req.getBodySpecIds() != null && !req.getBodySpecIds().isEmpty()) {
            List<BodySpec> specs = bodySpecRepository.findAllById(req.getBodySpecIds());
            sb.append("[신체 정보]\n");
            for (BodySpec s : specs) {
                sb.append(String.format("- 날짜: %s, 키: %s, 체중: %s\n", s.getCreatedAt(), s.getHeight(), s.getWeight()));
            }
            sb.append("\n");
        }

        // B. 식단 정보
        if (req.getDailyDietIds() != null && !req.getDailyDietIds().isEmpty()) {
            List<DailyDiet> diets = dailyDietRepository.findAllById(req.getDailyDietIds());
            sb.append("[식단 기록]\n");
            for (DailyDiet d : diets) {
                sb.append(String.format("<날짜: %s>\n", d.getDate()));
                // ★ 중요: 엔티티 구조에 맞춰서 상세 내용을 문자열로 만들어주세요
                // 예: d.getMeals()를 순회하며 음식 이름과 칼로리 추가
                sb.append("  (상세 식단 내용...)\n");
            }
            sb.append("\n");
        }

        // C. 챌린지 정보
        if (req.getChallengeIds() != null && !req.getChallengeIds().isEmpty()) {
            List<Challenge> challenges = challengeRepository.findAllById(req.getChallengeIds());
            sb.append("[참여 챌린지]\n");
            for (Challenge c : challenges) {
                sb.append(String.format("- %s (상태: %s)\n", c.getTitle(), c.getStatus()));
            }
        }

        return sb.toString();
    }
}