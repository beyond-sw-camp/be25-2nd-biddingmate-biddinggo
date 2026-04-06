-- Adjust vector dimension if your embedding model is not 1536.
create table if not exists public.auction_price_reference (
    id bigserial primary key,
    auction_id bigint not null unique,
    item_id bigint not null,
    category_id bigint not null,
    winner_price bigint not null check (winner_price >= 0),
    quality text null,
    condition_score double precision not null check (condition_score >= 0 and condition_score <= 1),
    embedding vector(1536) not null,
    embedding_model text not null,
    embedding_dimension integer not null default 1536,
    embedding_text text not null,
    completed_at timestamptz not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index if not exists idx_auction_price_reference_category_id
    on public.auction_price_reference (category_id);

create index if not exists idx_auction_price_reference_category_condition
    on public.auction_price_reference (category_id, condition_score);

create index if not exists idx_auction_price_reference_completed_at
    on public.auction_price_reference (completed_at desc);

create index if not exists idx_auction_price_reference_embedding
    on public.auction_price_reference
    using hnsw (embedding vector_cosine_ops);

drop trigger if exists trg_auction_price_reference_updated_at on public.auction_price_reference;

create trigger trg_auction_price_reference_updated_at
before update on public.auction_price_reference
for each row
execute function public.set_current_timestamp_updated_at();
