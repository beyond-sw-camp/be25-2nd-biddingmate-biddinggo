-- Adjust vector dimension if your embedding model is not 1536.
create table if not exists public.auction_query_embedding (
    id bigserial primary key,
    auction_id bigint not null unique,
    item_id bigint not null unique,
    category_id bigint not null,
    embedding vector(1536) not null,
    embedding_model text not null,
    embedding_dimension integer not null default 1536,
    embedding_text text not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index if not exists idx_auction_query_embedding_category_id
    on public.auction_query_embedding (category_id);

create index if not exists idx_auction_query_embedding_updated_at
    on public.auction_query_embedding (updated_at desc);

create index if not exists idx_auction_query_embedding_embedding
    on public.auction_query_embedding
    using hnsw (embedding vector_cosine_ops);

drop trigger if exists trg_auction_query_embedding_updated_at on public.auction_query_embedding;

create trigger trg_auction_query_embedding_updated_at
before update on public.auction_query_embedding
for each row
execute function public.set_current_timestamp_updated_at();
