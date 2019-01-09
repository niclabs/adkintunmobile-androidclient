package cl.niclabs.adkintunmobile.commons.utils;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Collection of methods for reflection operations
 * @author Felipe Lalanne <flalanne@niclabs.cl>
 *
 */
public class ReflectionUtils {
    /**
     * Get all the fields for the class, including those of the super classes
     *
     * @param fields
     * @param type
     * @return
     */
    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        Collections.addAll(fields, type.getDeclaredFields());

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    /**
     * Check if the field if a list of elements of the specified class type
     * @param type
     * @param field
     * @return true if field is a list of elements of class type
     */
    public static boolean isListOf(Class<?> type, Field field) {
        if (List.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                Type listType = ((ParameterizedType) genericType)
                        .getActualTypeArguments()[0];
                if (type.isAssignableFrom((Class<?>) listType)) {
                    return true;
                }
            }
        }
        return false;
    }
}

