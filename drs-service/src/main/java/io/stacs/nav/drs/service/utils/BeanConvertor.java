package io.stacs.nav.drs.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Bean convertor.
 *
 * @author liuyu
 * @description bean convertor
 * @date 2018 -04-10
 */
@Slf4j public class BeanConvertor {

    /**
     * convert bean,return new object
     *
     * @param <T>     the type parameter
     * @param from    the from
     * @param toClazz the to clazz
     * @return t
     */
    public static <T> T convertBean(@Nonnull Object from, Class<T> toClazz) {
        return convertBean(from, toClazz, (String[])null);
    }

    public static <T> T convertBean(@Nonnull Object from, Class<T> toClazz, String... ignoreProperties) {
        if (from == null) {
            return null;
        }
        try {
            T dest = toClazz.newInstance();
            BeanUtils.copyProperties(from, dest, ignoreProperties);
            return dest;
        } catch (Exception e) {
            log.error("[convertBean]has error", e);
        }
        return null;
    }

    /**
     * convert list bean,return new list for object
     *
     * @param <T>     the type parameter
     * @param <O>     the type parameter
     * @param from    the from
     * @param toClazz the to clazz
     * @return list
     */
    @Nullable public static <T, O> List<T> convertList(List<O> from, Class<T> toClazz) {
        if (CollectionUtils.isEmpty(from)) {
            return null;
        }
        List<T> list = new ArrayList<>(from.size());
        for (O o : from) {
            try {
                T dest = toClazz.newInstance();
                BeanUtils.copyProperties(o, dest);
                list.add(dest);
            } catch (Exception e) {
                log.error("[convertList]has error", e);
                return null;
            }
        }
        return list;
    }
}
