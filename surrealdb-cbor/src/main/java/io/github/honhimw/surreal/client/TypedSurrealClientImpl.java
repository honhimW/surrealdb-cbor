package io.github.honhimw.surreal.client;

import io.github.honhimw.surreal.ReactiveTypedSurrealClient;
import io.github.honhimw.surreal.TypedSurrealClient;
import io.github.honhimw.surreal.UpsertKind;
import io.github.honhimw.surreal.util.Helpers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author honhimW
 * @since 2025-04-27
 */

class TypedSurrealClientImpl<T, ID> implements TypedSurrealClient<T, ID> {

    private final ReactiveTypedSurrealClient<T, ID> client;

    TypedSurrealClientImpl(ReactiveTypedSurrealClient<T, ID> client) {
        this.client = client;
    }

    @Override
    public String table() {
        return client.table();
    }

    @Override
    public Class<T> type() {
        return client.type();
    }

    @Override
    public Optional<T> getById(ID id) {
        return client.getById(id).blockOptional();
    }

    @Override
    public List<T> getByIds(Collection<ID> ids) {
        return Helpers.blockNonNull(client.getByIds(ids));
    }

    @Override
    public T upsert(T entity, UpsertKind kind) {
        return Helpers.blockNonNull(client.upsert(entity, kind));
    }

    @Override
    public T upsert(T entity) {
        return Helpers.blockNonNull(client.upsert(entity));
    }

    @Override
    public T create(T entity) {
        return Helpers.blockNonNull(client.create(entity));
    }

    @Override
    public void delete(ID id) {
        client.delete(id).block();
    }

    @Override
    public List<T> insert(Collection<T> entities) {
        return Helpers.blockNonNull(client.insert(entities));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> insert(T entity, T... entities) {
        return Helpers.blockNonNull(client.insert(entity, entities));
    }

    @Override
    public T update(T entity, UpsertKind kind) {
        return Helpers.blockNonNull(client.update(entity, kind));
    }

    @Override
    public T update(T entity) {
        return Helpers.blockNonNull(client.update(entity));
    }
}
