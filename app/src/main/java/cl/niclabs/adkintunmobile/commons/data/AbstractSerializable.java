package cl.niclabs.adkintunmobile.commons.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.commons.utils.ReflectionUtils;

import com.orm.dsl.Ignore;

/**
 * Generic implementation for serializable objects.
 *
 * @author Felipe Lalanne <flalanne@niclabs.cl>
 *
 * @param <E>
 *            sub-class to serialize
 */
public abstract class AbstractSerializable<E extends AbstractSerializable<E>>
        implements Serializable<E> {
    /**
     * Return the list of fields of the object except for those with the @DoNotSerizalize
     * annotation.
     *
     * @return
     */
    @Override
    public List<Field> getSerializableFields() {
        List<Field> typeFields = new ArrayList<Field>();

        ReflectionUtils.getAllFields(typeFields, getClass());

        List<Field> toStore = new ArrayList<Field>();
        for (Field field : typeFields) {
            if (!field.isAnnotationPresent(com.orm.dsl.Ignore.class)
                    && !field.isAnnotationPresent(Ignore.class)
                    && !field.isAnnotationPresent(DoNotSerialize.class)
                    && !Modifier.isStatic(field.getModifiers())
                    && !Modifier.isTransient(field.getModifiers())) {
                toStore.add(field);
            }
        }
        return toStore;
    }

    public String toString() {
        Serializer serializer = SerializerFactory.getInstance().getSerializer();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            serializer.serialize(out, this);
            out.close();
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * Deserialize object from a string
     *
     * Uses the default serializer
     *
     * @param type
     * @param serializedObject
     * @return
     */
    public static <E extends Serializable<?>> E fromString(Class<E> type,
                                                           String serializedObject) {
        Serializer s = SerializerFactory.getInstance().getSerializer();
        try {
            return s.deserialize(type, serializedObject);
        } catch (IOException e) {
            // Parsing error: safe to ignore
        }
        return null;
    }
}
