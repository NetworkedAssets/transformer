package com.networkedassets.condoc.javadocConverterPlugin;

import com.networkedassets.condoc.javadocConverterPlugin.util.UnsafeHelper;
import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.common.docitem.OmniDocItem;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.ConverterPlugin;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Here be dragons.
 * <p>
 * This class wraps the {@link ConverterPlugin} class, so that the {@code tools.jar} is available for it.
 * <p>
 * The class accomplishes this through the use of {@link URLClassLoader} and {@link sun.misc.Unsafe} to translate
 * between the classes from different class loaders (in jvm, if the same class file is loaded in two different class
 * loaders, the resulting classes are treated as different classes).
 */
@WebListener
public class JavadocConverterPluginToolsJarWrapper implements ConverterPlugin {

    private ClassLoader javadocClassLoader;

    private Object javadocConverterPlugin;
    private Method convertMethod;
    private Method getIdentifierMethod;

    @Override
    public String getIdentifier() {
        try {
            return (String) getIdentifierMethod.invoke(javadocConverterPlugin);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Documentation convert(RawData rawData) {
        try {
            Object reflectiveRawData = makeReflectiveRawData(rawData);
            Object documentationFromTheOtherSide = convertMethod.invoke(javadocConverterPlugin, reflectiveRawData);

            return performKlassTransplantation(documentationFromTheOtherSide);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Documentation performKlassTransplantation(Object documentationFromTheOtherSide) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, InstantiationException {
        // bullshit to get relevant classes loaded
        @SuppressWarnings("unused") OmniDocItem.Relation rel;
        @SuppressWarnings("unused") OmniDocItem.MemberCategory cat;
        @SuppressWarnings("unused") OmniDocItem.MemberCategory.Member mem;
        // endbullshit

        // change the class of the foreign Documentation
        Documentation documentationKlassDonor = new Documentation();
        int documentationKlass = UnsafeHelper.getKlass(documentationKlassDonor);
        UnsafeHelper.setKlass(documentationFromTheOtherSide, documentationKlass);
        Documentation itsAllOgreNow = (Documentation) documentationFromTheOtherSide;

        // change the classes of all the DocItems in the Documentation
        Set docItems = itsAllOgreNow.getDocItems();
        for (Object item : docItems) {
            recursiveKlassTransplantation(item);
        }

        // set proper type
        itsAllOgreNow.setType(Documentation.DocumentationType.valueOf(itsAllOgreNow.getType().toString()));

        return itsAllOgreNow;
    }

    private Object recursiveKlassTransplantation(Object illegalAlien) throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        if (illegalAlien == null) return illegalAlien;
        String canonicalName = illegalAlien.getClass().getName();
        if (illegalAlien.getClass().equals(Class.forName(canonicalName))) return illegalAlien;

        int klass = UnsafeHelper.getKlassForName(canonicalName);

        Object lawAbidingCitizen = UnsafeHelper.setKlass(illegalAlien, klass);


        int length = lawAbidingCitizen.getClass().getDeclaredFields().length;
        for (int i = 0; i < length; i++) {
            try {
                Field f = lawAbidingCitizen.getClass().getDeclaredFields()[i];
                if (!f.getType().getCanonicalName().startsWith("java") && !f.getType().isPrimitive()) {
                    boolean oldAccessibility = f.isAccessible();
                    if (!oldAccessibility) f.setAccessible(true);
                    recursiveKlassTransplantation(f.get(illegalAlien));
                    f.setAccessible(oldAccessibility);

                } else if (Collection.class.isAssignableFrom(f.getType())) { // collections
                    boolean oldAccessibility = f.isAccessible();
                    if (!oldAccessibility) f.setAccessible(true);

                    Collection<Object> os = (Collection<Object>) f.get(illegalAlien);
                    for (Object child : os) {
                        recursiveKlassTransplantation(child);
                    }

                    f.setAccessible(oldAccessibility);
                }
            } catch (RuntimeException e) {
                if (e.getCause() instanceof IllegalArgumentException) {
                    --i;
                } else {
                    throw e;
                }
            }
        }

        return lawAbidingCitizen;
    }

    private Object makeReflectiveRawData(RawData rawData) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<?> reflectiveRawDataClass = Class.forName(
                "com.networkedassets.condoc.javadocConverterPlugin.util.ReflectiveRawData",
                true,
                javadocClassLoader);
        Object o = reflectiveRawDataClass.newInstance();
        Field wrappedObjectField = reflectiveRawDataClass.getField("wrappedObject");
        wrappedObjectField.set(o, rawData);
        return o;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Path javadocPath = Paths.get(System.getProperty("java.home"), "../lib/tools.jar");
        if (Files.notExists(javadocPath)) throw new RuntimeException("javadoc exec not found");
        try {
            ArrayList<URL> libs = Collections.list(this.getClass().getClassLoader().getResources(""));
            libs.add(javadocPath.toUri().toURL());
            javadocClassLoader = new URLClassLoader(libs.toArray(new URL[]{}));
            Class<?> converterClass = Class.forName(
                    "com.networkedassets.condoc.javadocConverterPlugin.JavadocConverterPlugin",
                    true,
                    javadocClassLoader
            );
            getIdentifierMethod = converterClass.getMethod("getIdentifier");
            convertMethod = Arrays.stream(converterClass.getMethods())
                    .filter(method -> method.getName().equals("convert")).findAny()
                    .orElseThrow(() -> new RuntimeException("could not find convert method"));
            javadocConverterPlugin = converterClass.newInstance();

            ConverterPlugin.super.contextInitialized(servletContextEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
