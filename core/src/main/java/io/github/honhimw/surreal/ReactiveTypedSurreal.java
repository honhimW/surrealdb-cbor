package io.github.honhimw.surreal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * @author honhimW
 * @since 2025-05-08
 */

public interface ReactiveTypedSurreal<T, ID> {

    /**
     * Type
     *
     * @return type
     */
    Class<T> type();

    /**
     * Get one record by id
     *
     * @param id id
     * @return record
     */
    Mono<T> getById(ID id);

    /**
     * Get records by id set
     *
     * @param ids id set
     * @return Records, not in orders as input collection.
     */
    Flux<T> getByIds(Collection<ID> ids);

    /**
     * Upsert
     *
     * @param entity to be saved
     * @param kind   CONTENT, MERGE, REPLACE, PATCH
     * @return result
     */
    Mono<T> upsert(T entity, UpsertKind kind);

    /**
     * Upsert
     *
     * @param entity to be saved
     * @return result
     */
    default Mono<T> upsert(T entity) {
        return upsert(entity, UpsertKind.MERGE);
    }

}
