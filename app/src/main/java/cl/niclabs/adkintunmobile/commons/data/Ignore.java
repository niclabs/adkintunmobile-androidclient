package cl.niclabs.adkintunmobile.commons.data;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicate that a field must be ignored by
 * for persistence and serialization
 *
 * Use instead of com.orm.dsl.Ignore
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
}