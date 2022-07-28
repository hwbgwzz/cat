package com.cat.common.toolkit;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;


@Slf4j
public abstract class ObjectUtil extends ObjectUtils {

    /**
     * 是否为null、空字符串、空数组、空list、空map。
     *
     * @param object the object
     * @return the boolean
     */
    public static boolean isEmptyObject(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof String) {
            return ((String) object).isEmpty();
        } else if (object.getClass().isArray()) {
            return ArrayUtil.isEmpty(object);
        } else if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        } else {
            return false;
        }
    }

    /**
     * 对象（空字符串、空数组、空list、空map。）不为空
     *
     * @param object the object
     * @return the boolean
     */
    public static boolean notEmpty(Object object) {
        return !isEmptyObject(object);
    }


    /**
     * Any not empty boolean.
     *
     * @param objects the objects
     * @return the boolean
     */
    public static boolean anyNotEmpty(Object... objects) {
        for (Object object : objects) {
            if (notEmpty(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为null、空字符串、空数组、空list、空map。
     *
     * @param objects the objects
     * @return the boolean
     */
    public static boolean isAnyEmptyObject(Object... objects) {
        for (Object object : objects) {
            if (isEmptyObject(object)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否为null、空字符串、空数组、空list、空map。
     *
     * @param objects the objects
     * @return the boolean
     */
    public static boolean allNotEmpty(Object... objects) {
        for (Object object : objects) {
            if (isEmptyObject(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为null、空字符串、空数组、空list、空map。
     *
     * @param objects the objects
     * @return the boolean
     */
    public static boolean allEmpty(Object... objects) {
        for (Object object : objects) {
            if (!isEmptyObject(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将源List<S> sourceList 转化为目标List<T> targetList
     *
     * @param <S>        源List<S> sourceList元素类型
     * @param <T>        目标List<T> targetList元素类型
     * @param sourceList 源List<S> sourceList
     * @param clazz      目标List<T> targetList元素类型
     * @return 目标List<T>  targetList
     */
    public static <S, T> List<T> deepCopyList(List<S> sourceList, Class<T> clazz) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(sourceList, clazz);
        /*

        String source = JSON.toFormatJSONString(sourceList);
        return JSON.toJavaObjectList(source, clazz);

         */
    }


    /**
     * 获取类的所有属性，包括父类
     *
     * @param <F>        the type parameter
     * @param <T>        the type parameter
     * @param entity     源实体
     * @param modelClass 目标元素类型
     * @return t
     */
    public static <F, T> T entityToModel(F entity, Class<T> modelClass) {

        return BeanUtil.copyProperties(entity, modelClass);
    }


    /**
     * 获取类的所有属性，包括父类
     *
     * @param object the object
     * @return field [ ]
     */
    public static Field[] getAllFields(Object object) {

        if (object == null) {
            return new Field[]{};
        }
        Class<?> clazz;
        if (object instanceof Class) {
            clazz = ((Class<?>) object);
        } else {
            clazz = object.getClass();
        }

        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }


    /**
     * Transform function.
     *
     * @param <T>    the type parameter
     * @param <R>    the type parameter
     * @param rClass the r class
     * @return the function
     */
    public static <T, R> Function<T, R> transform(Class<R> rClass) {
        return t -> ObjectUtil.entityToModel(t, rClass);
    }

    /**
     * Default if empty t.
     *
     * @param <T>          the type parameter
     * @param str          the str
     * @param defaultValue the default value
     * @return the t
     */
    public static <T> T defaultIfEmpty(final T str, final T defaultValue) {
        return isEmptyObject(str) ? defaultValue : str;
    }

    /**
     * 判断一个数字是否是正数
     *
     * @param number the number
     * @return boolean
     */
    public static boolean positive(Number number) {
        return number != null && number.doubleValue() > 0;
    }


    /**
     * 判断一个对象的所有属性是否都为null
     *
     * @param object 参数
     * @return null true ； not null false
     */
    public static boolean checkObjAllFieldsIsNull(Object object) {
        if (isEmptyObject(object)) {
            return true;
        }
        Class<?> clazz = object.getClass();
        if (Arrays.stream(clazz.getDeclaredFields())
                .allMatch(field ->
                        {
                            try {
                                return ObjectUtil.isEmpty(FieldUtils.readDeclaredField(object, field.getName(), true));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                log.error("判断异常:{}", e);
                            }
                            return true;
                        }
                )) {
            log.warn("empty {}, class: {}", object, clazz);
            return true;
        }

        return false;
    }


}
