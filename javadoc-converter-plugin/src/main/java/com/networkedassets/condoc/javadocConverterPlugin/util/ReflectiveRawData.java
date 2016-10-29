package com.networkedassets.condoc.javadocConverterPlugin.util;

import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Set;

@SuppressWarnings("unused")
public class ReflectiveRawData implements RawData {
    @SuppressWarnings("WeakerAccess")
    public Object wrappedObject;

    @Override
    public Set<Path> getDataPaths() {
        try {
            Method getDataPaths = wrappedObject.getClass().getMethod("getDataPaths");
            //noinspection unchecked
            return (Set<Path>) getDataPaths.invoke(wrappedObject);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
