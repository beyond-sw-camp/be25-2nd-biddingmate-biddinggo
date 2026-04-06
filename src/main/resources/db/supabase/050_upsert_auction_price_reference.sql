insert into public.auction_price_reference (
    auction_id,
    item_id,
    category_id,
    winner_price,
    quality,
    condition_score,
    embedding,
    embedding_model,
    embedding_dimension,
    embedding_text,
    completed_at
) values (
    $1,
    $2,
    $3,
    $4,
    $5,
    $6,
    $7,
    $8,
    $9,
    $10,
    $11
)
on conflict (auction_id)
do update set
    item_id = excluded.item_id,
    category_id = excluded.category_id,
    winner_price = excluded.winner_price,
    quality = excluded.quality,
    condition_score = excluded.condition_score,
    embedding = excluded.embedding,
    embedding_model = excluded.embedding_model,
    embedding_dimension = excluded.embedding_dimension,
    embedding_text = excluded.embedding_text,
    completed_at = excluded.completed_at,
    updated_at = now();
