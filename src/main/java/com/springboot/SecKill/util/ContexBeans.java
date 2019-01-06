package com.springboot.SecKill.util;

/**
 * @author WilsonSong
 * @date 2018/8/5/005
 */

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.util.Validate;
public class ContexBeans implements Map<String, Object> {
    private final ApplicationContext ctx;

    public ContexBeans(ApplicationContext ctx) {
        Validate.notNull(ctx, "Application Context cannot be null");
        this.ctx = ctx;
    }

    public boolean containsKey(Object key) {
        Validate.notNull(key, "Key cannot be null");
        return this.ctx.containsBean(key.toString());
    }

    public Object get(Object key) {
        Validate.notNull(key, "Key cannot be null");
        return this.ctx.getBean(key.toString());
    }

    public Set<String> keySet() {
        return new LinkedHashSet(Arrays.asList(this.ctx.getBeanDefinitionNames()));
    }

    public int size() {
        return this.ctx.getBeanDefinitionCount();
    }

    public boolean isEmpty() {
        return this.ctx.getBeanDefinitionCount() <= 0;
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Method \"containsValue\" not supported in Beans object");
    }

    public Object put(String key, Object value) {
        throw new UnsupportedOperationException("Method \"put\" not supported in Beans object");
    }

    public void putAll(Map<? extends String, ?> m) {
        throw new UnsupportedOperationException("Method \"putAll\" not supported in Beans object");
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException("Method \"remove\" not supported in Beans object");
    }

    public void clear() {
        throw new UnsupportedOperationException("Method \"clear\" not supported in Beans object");
    }

    public Collection<Object> values() {
        throw new UnsupportedOperationException("Method \"values\" not supported in Beans object");
    }

    public Set<Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException("Method \"entrySet\" not supported in Beans object");
    }

}
