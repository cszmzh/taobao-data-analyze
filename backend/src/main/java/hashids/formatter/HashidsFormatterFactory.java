package hashids.formatter;

import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HashidsFormatterFactory extends EmbeddedValueResolutionSupport implements AnnotationFormatterFactory<HashidsFormat> {

    private static final Set<Class<?>> FIELD_TYPES;

    static {
        Set<Class<?>> fieldTypes = new HashSet<>(2);
        fieldTypes.add(Integer.class);
        fieldTypes.add(int.class);
        FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
    }

    /**
     * The types of fields that may be annotated with the &lt;A&gt; annotation.
     */
    @Override
    public Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    /**
     * Get the Printer to print the value of a field of {@code fieldType} annotated with
     * {@code annotation}.
     * <p>If the type T the printer accepts is not assignable to {@code fieldType}, a
     * coercion from {@code fieldType} to T will be attempted before the Printer is invoked.
     *
     * @param annotation the annotation instance
     * @param fieldType  the type of field that was annotated
     * @return the printer
     */
    @Override
    public Printer<?> getPrinter(HashidsFormat annotation, Class<?> fieldType) {
        return getFormatter();
    }

    /**
     * Get the Parser to parse a submitted value for a field of {@code fieldType}
     * annotated with {@code annotation}.
     * <p>If the object the parser returns is not assignable to {@code fieldType},
     * a coercion to {@code fieldType} will be attempted before the field is set.
     *
     * @param annotation the annotation instance
     * @param fieldType  the type of field that was annotated
     * @return the parser
     */
    @Override
    public Parser<?> getParser(HashidsFormat annotation, Class<?> fieldType) {
        return getFormatter();
    }

    protected Formatter<Long> getFormatter() {
        HashidsFormatter formatter = new HashidsFormatter();
        return formatter;
    }
}
