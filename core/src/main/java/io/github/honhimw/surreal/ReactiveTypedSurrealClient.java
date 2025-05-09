package io.github.honhimw.surreal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author honhimW
 * @since 2025-05-08
 */

public interface ReactiveTypedSurrealClient<T, ID> {

    /**
     * Current table name
     *
     * @return table name
     */
    String table();

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
     * UPSERT ONLY `table` `kind` $content;
     *
     * @param entity to be saved
     * @param kind   CONTENT, MERGE, REPLACE, PATCH
     * @return result
     */
    Mono<T> upsert(T entity, UpsertKind kind);

    /**
     * UPSERT ONLY `table` MERGE $content;
     *
     * @param entity to be saved
     * @return result
     */
    default Mono<T> upsert(T entity) {
        return upsert(entity, UpsertKind.MERGE);
    }

    /**
     * CREATE ONLY `table` CONTENT $content;
     *
     * @param entity to be saved
     * @return result
     */
    Mono<T> create(T entity);

    /**
     * (DELETE `record_id` RETURN BEFORE).map(|$record| $record.id);
     *
     * @param id id
     * @return none
     */
    Mono<Void> delete(ID id);

    /**
     * INSERT INTO `table` [$one, $more,...];
     *
     * @param entities to be saved
     * @return result
     */
    Flux<T> insert(Collection<T> entities);

    /**
     * INSERT IGNORE INTO `table` [$one, $more,...];
     *
     * @param entity   one
     * @param entities more
     * @return result
     */
    default Flux<T> insert(T entity, T... entities) {
        int size = entities.length + 1;
        List<T> list = new ArrayList<>(size);
        list.add(entity);
        Collections.addAll(list, entities);
        return insert(list);
    }

    /**
     * UPDATE ONLY `table` `kind` $content;
     *
     * @param entity to be updated, entity.id should not be null
     * @param kind   update kind
     * @return result
     */
    Mono<T> update(T entity, UpsertKind kind);

    /**
     * UPDATE ONLY `table` CONTENT $content;
     *
     * @param entity to be updated, entity.id should not be null
     * @return result
     */
    default Mono<T> update(T entity) {
        return update(entity, UpsertKind.CONTENT);
    }

}
