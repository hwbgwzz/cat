package com.cat.common.toolkit.json;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;

@Slf4j
public class JSON {
    //序列化
    private static final ObjectMapper instance;

    static {
        SimpleModule simpleModule = new SimpleModule()
                .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(Long.TYPE, ToStringSerializer.instance)
                .addSerializer(BigInteger.class, ToStringSerializer.instance)
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)))
                .addDeserializer(String.class, new StringDeserializer() {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ct) throws IOException {
                        String text = p.getText() == null ? null : p.getText().trim();
                        if ("null".equals(text) || "undefined".equals(text))
                            return "";
                        return text;
                    }
                })
                .addSerializer(long[].class, new JsonSerializer<long[]>() {
                    @Override
                    public void serialize(
                            long[] value, JsonGenerator g, SerializerProvider provider)
                            throws IOException {
                        g.writeStartArray();
                        for (long l : value) g.writeString(String.valueOf(l));
                        g.writeEndArray();
                    }
                })
                .addSerializer(BigDecimal.class, ToStringSerializer.instance);
        instance = new ObjectMapper()
                .setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN))
                .registerModules(simpleModule)
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                .setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                .setLocale(Locale.CHINA)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                //忽略未知字段
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。JSON标准说明书要求所有控制符必须使用引号，因此这是一个非标准的特性
                .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
                // 字符串写入枚举
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                // 忽略不能转移的字符
                .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true)
                //单引号处理
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 允许key没有双引号
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                // 允许整数以0开头
                .configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true)
                // 允许字符串中存在回车换行控制符
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        //instance.registerModules(simpleModule, new JtsModule());
        instance.configOverride(String.class)
                .setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));
    }

    /**
     * 返回序列化实例
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return instance;
    }

    /**
     * 对象转换json字符串
     *
     * @param obj 目标对象
     * @return json string
     */
    public static String toJSONString(Object obj) {
        return obj != null ? toJSONString(obj, () -> "", false) : "";
    }


    /**
     * json 转json 节点
     *
     * @param json the json
     * @return json node
     * @throws JsonProcessingException the json processing exception
     */
    public static JsonNode readTree(String json) throws JsonProcessingException {
        return instance.readTree(json);
    }


    /**
     * 对象转json并且格式化
     *
     * @param obj 对象
     * @return the string
     */
    public static String toFormatJSONString(Object obj) {
        return obj != null ? toJSONString(obj, () -> "", true) : "";
    }


    /**
     * 对象转json
     *
     * @param obj             目标对象
     * @param defaultSupplier 默认生产者
     * @param format          是否格式化
     * @return the string
     */
    public static String toJSONString(Object obj, Supplier<String> defaultSupplier, boolean format) {
        try {
            if (obj == null) {
                return defaultSupplier.get();
            }
            if (obj instanceof String) {
                return obj.toString();
            }
            if (obj instanceof Number) {
                return obj.toString();
            }
            if (format) {
                return instance.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
            return instance.writeValueAsString(obj);
        } catch (Throwable e) {
            log.info(String.format("toJSONString %s", obj != null ? obj.toString() : "null"), e);
        }
        return defaultSupplier.get();
    }


    /**
     * json 转 java 对象
     *
     * @param <T>    the type parameter
     * @param value  json
     * @param tClass 目标对象类型
     * @return the t
     */
    public static <T> T toJavaObject(String value, Class<T> tClass) {
        return isNotBlank(value) ? toJavaObject(value, tClass, () -> null) : null;
    }


    /**
     * json 转 java 对象
     *
     * @param <T>       the type parameter
     * @param value     json
     * @param reference 类型引用                  <p>                  Example  new TypeReference<List<Long>>(){}                  </p>
     * @return the t
     */
    public static <T> T toJavaObject(String value, TypeReference<T> reference) {

        try {
            return instance.readValue(value, reference);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 对象转其他对象 「类似于深拷贝」
     *
     * @param <T>    the type parameter
     * @param obj    原对象
     * @param tClass 目标对象class
     * @return the t
     */
    public static <T> T toJavaObject(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObject(toJSONString(obj), tClass, () -> null) : null;
    }


    /**
     * To java object t.
     *
     * @param <T>    the type parameter
     * @param url    the url
     * @param tClass the t class
     * @return the t
     */
    public static <T> T toJavaObject(URL url, Class<T> tClass) {
        if (url == null) {
            return null;
        }
        try {
            return instance.readValue(url, tClass);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * To java object t.
     *
     * @param <T>         the type parameter
     * @param inputStream inputStream
     * @param tClass      the t class
     * @return the t
     */
    public static <T> T toJavaObject(InputStream inputStream, Class<T> tClass) {
        if (inputStream == null) {
            return null;
        }
        try {
            return instance.readValue(inputStream, tClass);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    /**
     * To java object t.
     *
     * @param <T>             the type parameter
     * @param value           the value
     * @param tClass          the t class
     * @param defaultSupplier the default supplier
     * @return the t
     */
    public static <T> T toJavaObject(String value, Class<T> tClass, Supplier<T> defaultSupplier) {
        try {
            if (isBlank(value)) {
                return defaultSupplier.get();
            }
            return instance.readValue(value, tClass);
        } catch (Throwable e) {
            log.error("toJavaObject error {}", e.getMessage(), e);
        }
        return defaultSupplier.get();
    }

    /**
     * To java object list list.
     *
     * @param <T>    the type parameter
     * @param value  the value
     * @param tClass the t class
     * @return the list
     */
    public static <T> List<T> toJavaObjectList(String value, Class<T> tClass) {
        return isNotBlank(value) ? toJavaObjectList(value, tClass, () -> null) : null;
    }

    /**
     * To java object list list.
     *
     * @param <T>    the type parameter
     * @param obj    the obj
     * @param tClass the t class
     * @return the list
     */
    public static <T> List<T> toJavaObjectList(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObjectList(toJSONString(obj), tClass, () -> null) : null;
    }

    /**
     * To java object list list.
     *
     * @param <T>             the type parameter
     * @param value           the value
     * @param tClass          the t class
     * @param defaultSupplier the default supplier
     * @return the list
     */
    public static <T> List<T> toJavaObjectList(String value, Class<T> tClass, Supplier<List<T>> defaultSupplier) {
        try {
            if (isBlank(value)) {
                return defaultSupplier.get();
            }
            JavaType javaType = instance.getTypeFactory().constructParametricType(List.class, tClass);
            return instance.readValue(value, javaType);
        } catch (Throwable e) {
            log.error(String.format("toJavaObjectList exception \n%s\n%s", value, tClass), e);
        }
        return defaultSupplier.get();
    }

    /**
     * Json copy t.
     *
     * @param <T>    the type parameter
     * @param obj    the obj
     * @param tClass the t class
     * @return the t
     */
// 简单地直接用json复制或者转换(Cloneable)
    public static <T> T jsonCopy(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObject(toJSONString(obj), tClass) : null;
    }

    /**
     * To map map.
     *
     * @param value the value
     * @return the map
     */
    public static Map<String, Object> toMap(String value) {
        return isNotBlank(value) ? toMap(value, () -> null) : null;
    }

    /**
     * To map map.
     *
     * @param value the value
     * @return the map
     */
    public static Map<String, Object> toMap(Object value) {
        return value != null ? toMap(value, () -> null) : null;
    }

    /**
     * To map map.
     *
     * @param value           the value
     * @param defaultSupplier the default supplier
     * @return the map
     */
    public static Map<String, Object> toMap(Object value, Supplier<Map<String, Object>> defaultSupplier) {
        if (value == null) {
            return defaultSupplier.get();
        }
        try {
            if (value instanceof Map) {
                return (Map<String, Object>) value;
            }
        } catch (Exception e) {
            log.error("fail to convert" + toJSONString(value), e);
        }
        return toMap(toJSONString(value), defaultSupplier);
    }

    /**
     * To map map.
     *
     * @param value           the value
     * @param defaultSupplier the default supplier
     * @return the map
     */
    public static Map<String, Object> toMap(String value, Supplier<Map<String, Object>> defaultSupplier) {
        if (isBlank(value)) {
            return defaultSupplier.get();
        }
        try {
            return toJavaObject(value, LinkedHashMap.class);
        } catch (Exception e) {
            log.error(String.format("toMap exception\n%s", value), e);
        }
        return defaultSupplier.get();
    }


    /**
     * To list list.
     *
     * @param value the value
     * @return the list
     */
    public static List<Object> toList(String value) {
        return isNotBlank(value) ? toList(value, () -> null) : null;
    }

    /**
     * To list list.
     *
     * @param value the value
     * @return the list
     */
    public static List<Object> toList(Object value) {
        return value != null ? toList(value, () -> null) : null;
    }

    /**
     * To list list.
     *
     * @param value          the value
     * @param defaultSuppler the default suppler
     * @return the list
     */
    public static List<Object> toList(String value, Supplier<List<Object>> defaultSuppler) {
        if (isBlank(value)) {
            return defaultSuppler.get();
        }
        try {
            return toJavaObject(value, List.class);
        } catch (Exception e) {
            log.error("toList exception\n" + value, e);
        }
        return defaultSuppler.get();
    }

    /**
     * To list list.
     *
     * @param value          the value
     * @param defaultSuppler the default suppler
     * @return list
     */
    public static List<Object> toList(Object value, Supplier<List<Object>> defaultSuppler) {
        if (value == null) {
            return defaultSuppler.get();
        }
        if (value instanceof List) {
            return (List<Object>) value;
        }
        return toList(toJSONString(value), defaultSuppler);
    }


    /**
     * Gets instance.
     *
     * @return 获取object -mapper
     */
    public static ObjectMapper getInstance() {
        return instance;
    }


    /**
     * Copy object mapper.
     *
     * @return 获取object -mapper
     */
    public static ObjectMapper copy() {
        return instance.copy();
    }


    /**
     * To json bytes byte [ ].
     *
     * @param object 目标对象
     * @return 字节数组 byte [ ]
     */
    public static byte[] toJSONBytes(Object object) {
        try {
            return instance.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return new byte[0];
    }


    /**
     * 检查是否是一个有效的JSON
     *
     * @param json the json
     * @return 是否 boolean
     */
    public static boolean isJSON(String json) {
        try {
            instance.readTree(json);
        } catch (IOException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * Byte to object t.
     *
     * @param <T>   范型
     * @param bytes 字节数组
     * @param clazz 类型
     * @return the t
     */
    public static <T> T byteToObject(byte[] bytes, Class<T> clazz) {
        try {
            return instance.readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     */
    private static boolean isNotBlank(String str) {
        return str != null && !str.isEmpty();
    }


    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     */
    private static boolean isBlank(String str) {
        return !isNotBlank(str);
    }
}
