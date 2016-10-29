package com.networkedassets.condoc.javadocConverterPlugin.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeHelper {

    private static final Unsafe unsafe = createUnsafe();
    /**
     * Offset from the beginning of object (object header to be exact) to the klass (compressed-) pointer.
     * <br>DANGER: this is not portable, works on 64-bit Oracle JVM, maaaaybe others
     * @see <a href="https://blogs.oracle.com/jonthecollector/entry/presenting_the_permanent_generation">internal representation of java objects</a>
     */
    private static final long KLASS_POINTER_OFFSET = 8L;

    private static Unsafe createUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Can't use unsafe", e);
        }
    }

    /**
     * DANGER: this is not portable, as it assumes 32-bit compressed pointers as used in 64-bit Oracle JVM
     */
    public static int getKlass(Object o) {
        return unsafe.getInt(o, KLASS_POINTER_OFFSET);
    }

    /**
     * DANGER: this is not portable, as it assumes 32-bit compressed pointers as used in 64-bit Oracle JVM
     */
    public static Object setKlass(Object o, int klass) {
        unsafe.getAndSetInt(o, KLASS_POINTER_OFFSET, klass);
        return o;
    }

    public static int getKlassForName(String className) throws ClassNotFoundException, InstantiationException {
        Object o = unsafe.allocateInstance(Class.forName(className));
        return getKlass(o);
    }
}