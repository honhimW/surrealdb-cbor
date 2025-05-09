package io.github.honhimw.surreal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author honhimW
 * @since 2025-05-08
 */

public interface TypedSurrealClient<T, ID> {

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
    Optional<T> getById(ID id);

    /**
     * Get records by id set
     *
     * @param ids id set
     * @return Records, not in orders as input collection.
     */
    List<T> getByIds(Collection<ID> ids);

    /**
     * UPSERT ONLY `table` `kind` $content;
     *
     * @param entity to be saved
     * @param kind   CONTENT, MERGE, REPLACE, PATCH
     * @return result
     */
    T upsert(T entity, UpsertKind kind);

    /**
     * UPSERT ONLY `table` MERGE $content;
     *
     * @param entity to be saved
     * @return result
     */
    T upsert(T entity);

    /**
     * CREATE ONLY `table` CONTENT $content;
     *
     * @param entity to be saved
     * @return result
     */
    T create(T entity);

    /**
     * (DELETE `record_id` RETURN BEFORE).map(|$record| $record.id);
     *
     * @param id id
     */
    void delete(ID id);

    /**
     * INSERT INTO `table` [$one, $more,...];
     *
     * @param entities to be saved
     * @return result
     */
    List<T> insert(Collection<T> entities);

    /**
     * INSERT IGNORE INTO `table` [$one, $more,...];
     *
     * @param entity   one
     * @param entities more
     * @return result
     */
    List<T> insert(T entity, T... entities);

    /**
     * UPDATE ONLY `table` `kind` $content;
     *
     * @param entity to be updated, entity.id should not be null
     * @param kind   update kind
     * @return result
     */
    T update(T entity, UpsertKind kind);

    /**
     * UPDATE ONLY `table` CONTENT $content;
     *
     * @param entity to be updated, entity.id should not be null
     * @return result
     */
    T update(T entity);

}
