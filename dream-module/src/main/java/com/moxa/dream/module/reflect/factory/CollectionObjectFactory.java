package com.moxa.dream.module.reflect.factory;

import com.moxa.dream.module.reflect.wrapper.PropertyInfo;

import java.util.Collection;

public class CollectionObjectFactory extends BeanObjectFactory {
    CollectionObjectFactory() {

    }

    public CollectionObjectFactory(Class<? extends Collection> type) {
        super(type);
    }

    @Override
    public void set(PropertyInfo propertyInfo, Object value) {
        if (propertyInfo == null)
            ((Collection) result).add(value);
        else {
            super.set(propertyInfo, value);
        }
    }
}