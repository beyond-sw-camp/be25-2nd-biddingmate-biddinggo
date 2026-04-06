insert into public.auction_query_embedding (
    auction_id,
    item_id,
    category_id,
    embedding,
    embedding_model,
    embedding_dimension,
    embedding_text
) values (
    $1,
    $2,
    $3,
    $4,
    $5,
    $6,
    $7
)
on conflict (auction_id)
do update set
    item_id = excluded.item_id,
    category_id = excluded.category_id,
    embedding = excluded.embedding,
    embedding_model = excluded.embedding_model,
    embedding_dimension = excluded.embedding_dimension,
    embedding_text = excluded.embedding_text,
    updated_at = now();
