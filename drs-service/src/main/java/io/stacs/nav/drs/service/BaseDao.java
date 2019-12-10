package io.stacs.nav.drs.service;

import java.util.List;

/**
 * The interface Base dao.
 *
 * @param <T> the type parameter
 * @Description:
 * @author: pengdi
 */
public interface BaseDao<T> {

    /**
     * Add int.
     *
     * @param t the t
     * @return the int
     */
    int add(T t);

    /**
     * Delete int.
     *
     * @param id the id
     * @return the int
     */
    int delete(Object id);

    /**
     * Query by count int.
     *
     * @param t the t
     * @return the int
     */
    int queryByCount(T t);

    /**
     * Query by list list.
     *
     * @param t the t
     * @return the list
     */
    List<T> queryByList(T t);

    /**
     * Query by id t.
     *
     * @param id the id
     * @return the t
     */
    T queryById(Object id);

}
