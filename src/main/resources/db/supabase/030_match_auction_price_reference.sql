-- Adjust vector dimension if your embedding model is not 1536.
create or replace function public.match_auction_price_reference(
    query_embedding vector(1536),
    filter_category_id bigint,
    min_condition_score double precision,
    max_condition_score double precision,
    match_count integer default 10,
    min_similarity double precision default 0,
    exclude_auction_id bigint default null
)
returns table (
    reference_id bigint,
    auction_id bigint,
    item_id bigint,
    category_id bigint,
    winner_price bigint,
    condition_score double precision,
    similarity double precision
)
language sql
stable
as $$
    select
        apr.id as reference_id,
        apr.auction_id,
        apr.item_id,
        apr.category_id,
        apr.winner_price,
        apr.condition_score,
        1 - (apr.embedding <=> query_embedding) as similarity
    from public.auction_price_reference apr
    where apr.category_id = filter_category_id
      and apr.condition_score between min_condition_score and max_condition_score
      and (exclude_auction_id is null or apr.auction_id <> exclude_auction_id)
      and 1 - (apr.embedding <=> query_embedding) >= min_similarity
    order by apr.embedding <=> query_embedding
    limit greatest(match_count, 0);
$$;
