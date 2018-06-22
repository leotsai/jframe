package org.jframe.core.extensions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by denghua on 2017-09-25.
 */
public class JMap<K, V> extends HashMap<K, V> {

    public JMap() {
        super();
    }

    public JMap(int initialCapacity) {
        super(initialCapacity);
    }

    public JMap(Map<? extends K, ? extends V> m) {
        super(m);
    }


    public <T> T get(K key, Class<T> clazz, Function<V, T> parse) {
        return this.get(key, clazz, parse, null);
    }

    public <T> T get(K key, Class<T> clazz, Function<V, T> parse, T defaultValue) {
        V value = super.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        return parse.apply(value);
    }

    public Long getLong(K key) {
        return this.get(key, Long.class, x -> Long.valueOf(x.toString()));
    }

    public Long getLong(K key, Long defaultValue) {
        Long value = this.getLong(key);
        return value == null ? defaultValue : value;
    }

    public Integer getInteger(K key) {
        return this.get(key, Integer.class, x -> Integer.valueOf(x.toString()));
    }

    public Integer getInteger(K key, Integer defaultValue) {
        Integer value = this.getInteger(key);
        return value == null ? defaultValue : value;
    }

    public Double getDouble(K key) {
        return this.get(key, Double.class, x -> Double.valueOf(x.toString()));
    }

    public Double getDouble(K key, Double defaultValue) {
        Double value = this.getDouble(key);
        return value == null ? defaultValue : value;
    }

    public String getString(K key) {
        return this.get(key, String.class, x -> x.toString());
    }

    public String getString(K key, String defaultValue) {
        String value = this.getString(key);
        return value == null ? defaultValue : value;
    }

    public BigDecimal getBigDecimal(K key) {
        return this.get(key, BigDecimal.class, x -> BigDecimal.valueOf(Double.valueOf(x.toString())));
    }

    public BigDecimal getBigDecimal(K key, BigDecimal defaultValue) {
        BigDecimal value = this.getBigDecimal(key);
        return value == null ? defaultValue : value;
    }

    public BigInteger getBigInteger(K key) {
        return this.get(key, BigInteger.class, x -> BigInteger.valueOf(Long.valueOf(x.toString())));
    }

    public BigInteger getBigInteger(K key, BigInteger defaultValue) {
        BigInteger value = this.getBigInteger(key);
        return value == null ? defaultValue : value;
    }

    public Date getDate(K key) {
        return this.get(key, Date.class, x -> JDate.tryParseFrom(x.toString()));
    }

    public Date getDate(K key, Date defaultValue) {
        Date value = this.getDate(key);
        return value == null ? defaultValue : value;
    }

    public Boolean getBoolean(K key) {
        return this.get(key, Boolean.class, x -> Boolean.valueOf(x.toString()));
    }

    public Boolean getBoolean(K key, Boolean defaultValue) {
        Boolean value = this.getBoolean(key);
        return value == null ? defaultValue : value;
    }


}