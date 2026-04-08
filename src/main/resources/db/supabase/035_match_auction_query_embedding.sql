-- Adjust vector dimension if your embedding model is not 1536.
-- 검색어 임베딩을 기준으로 유사한 경매 query embedding 후보를 검색하는 함수.
create or replace function public.match_auction_query_embedding(
    query_embedding vector(1536),
    match_count integer default 100,
    min_similarity double precision default 0
)
returns table (
    auction_id bigint,
    item_id bigint,
    category_id bigint
)
language sql
stable
as $$
    select
        aqe.auction_id,
        aqe.item_id,
        aqe.category_id
    from public.auction_query_embedding aqe
    where 1 - (aqe.embedding <=> query_embedding) >= min_similarity
    order by aqe.embedding <=> query_embedding
    limit greatest(match_count, 0);
$$;
