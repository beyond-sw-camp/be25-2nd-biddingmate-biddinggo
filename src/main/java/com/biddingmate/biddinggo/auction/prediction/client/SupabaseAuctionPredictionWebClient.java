package com.biddingmate.biddinggo.auction.prediction.client;

import com.biddingmate.biddinggo.auction.prediction.model.AuctionPriceReference;
import com.biddingmate.biddinggo.auction.prediction.model.AuctionQueryEmbedding;
import com.biddingmate.biddinggo.config.AuctionPredictionSupabaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Supabase REST RPC를 호출해 벡터 저장소 레코드를 upsert 하는 구현체.
 * 현재는 query embedding 저장이 먼저 사용되고, price reference 저장은 다음 단계에서 이어진다.
 */
@Component
@RequiredArgsConstructor
public class SupabaseAuctionPredictionWebClient implements AuctionPredictionSupabaseClient {
    private final WebClient webClient;
    private final AuctionPredictionSupabaseProperties properties;

    @Override
    /**
     * Supabase URL과 service role key가 준비된 경우에만 연동 가능 상태로 본다.
     */
    public boolean isEnabled() {
        return properties.isEnabled()
                && StringUtils.hasText(properties.getUrl())
                && StringUtils.hasText(properties.getServiceRoleKey());
    }

    @Override
    /**
     * upsert_auction_query_embedding RPC 함수를 호출해 조회용 임베딩을 저장한다.
     */
    public void upsertAuctionQueryEmbedding(AuctionQueryEmbedding auctionQueryEmbedding) {
        if (!isEnabled()) {
            throw new IllegalStateException("Auction prediction supabase client is not configured.");
        }

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("p_auction_id", auctionQueryEmbedding.getAuctionId());
        requestBody.put("p_item_id", auctionQueryEmbedding.getItemId());
        requestBody.put("p_category_id", auctionQueryEmbedding.getCategoryId());
        requestBody.put("p_embedding", toVectorLiteral(auctionQueryEmbedding.getEmbedding()));
        requestBody.put("p_embedding_model", auctionQueryEmbedding.getEmbeddingModel());
        requestBody.put("p_embedding_dimension", auctionQueryEmbedding.getEmbeddingDimension());
        requestBody.put("p_embedding_text", auctionQueryEmbedding.getEmbeddingText());

        invokeRpc("upsert_auction_query_embedding", requestBody);
    }

    @Override
    /**
     * upsert_auction_price_reference RPC 함수를 호출해 낙찰 reference를 저장한다.
     */
    public void upsertAuctionPriceReference(AuctionPriceReference auctionPriceReference) {
        if (!isEnabled()) {
            throw new IllegalStateException("Auction prediction supabase client is not configured.");
        }

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("p_auction_id", auctionPriceReference.getAuctionId());
        requestBody.put("p_item_id", auctionPriceReference.getItemId());
        requestBody.put("p_category_id", auctionPriceReference.getCategoryId());
        requestBody.put("p_winner_price", auctionPriceReference.getWinnerPrice());
        requestBody.put("p_quality", auctionPriceReference.getQuality());
        requestBody.put("p_condition_score", auctionPriceReference.getConditionScore());
        requestBody.put("p_embedding", toVectorLiteral(auctionPriceReference.getEmbedding()));
        requestBody.put("p_embedding_model", auctionPriceReference.getEmbeddingModel());
        requestBody.put("p_embedding_dimension", auctionPriceReference.getEmbeddingDimension());
        requestBody.put("p_embedding_text", auctionPriceReference.getEmbeddingText());
        requestBody.put("p_completed_at", auctionPriceReference.getCompletedAt());

        invokeRpc("upsert_auction_price_reference", requestBody);
    }

    /**
     * 지정한 Supabase RPC 함수를 공통 방식으로 호출한다.
     */
    private void invokeRpc(String functionName, Map<String, Object> requestBody) {
        webClient.post()
                .uri(normalizeBaseUrl(properties.getUrl()) + "/rest/v1/rpc/" + functionName)
                .headers(headers -> applySupabaseHeaders(headers))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofMillis(properties.getTimeoutMillis()))
                .block();
    }

    /**
     * Supabase REST 호출에 필요한 인증/스키마 헤더를 설정한다.
     */
    private void applySupabaseHeaders(HttpHeaders headers) {
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getServiceRoleKey());
        headers.set("apikey", properties.getServiceRoleKey());
        headers.set("Content-Profile", properties.getSchema());
        headers.set("Accept-Profile", properties.getSchema());
    }

    /**
     * Java List 벡터를 pgvector 입력 형식 문자열로 변환한다.
     */
    private String toVectorLiteral(List<Double> embedding) {
        if (embedding == null || embedding.isEmpty()) {
            throw new IllegalArgumentException("Embedding vector must not be empty.");
        }

        return embedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }

    /**
     * base URL 마지막 슬래시를 제거해 경로 조합을 안정화한다.
     */
    private String normalizeBaseUrl(String baseUrl) {
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }
}
