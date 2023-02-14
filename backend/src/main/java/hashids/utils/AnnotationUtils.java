package hashids.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 注解工具类
 *
 * @author yanshen
 * Created by yanshen on 2019/1/3.
 */
public class AnnotationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationUtils.class);

    /**
     * 是否包含指定注解
     *
     * @param clazz
     * @param fieldName
     * @param annotationClazz
     * @return
     */
    public static boolean fieldHasAnnotation(Class clazz, String fieldName, Class annotationClazz) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            Annotation annotation = field.getAnnotation(annotationClazz);
            return annotation != null;
        } catch (NoSuchFieldException e) {
            // LOGGER.error("no such field error", e);
            return false;
        }
    }

    /**
     * 获取指定注解
     *
     * @param clazz
     * @param fieldName
     * @param annotationClazz
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getFieldAnnotation(Class clazz, String fieldName, Class<T> annotationClazz) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            T annotation = field.getAnnotation(annotationClazz);
            return annotation;
        } catch (NoSuchFieldException e) {
            LOGGER.error("no such field error", e);
        }
        return null;
    }
}
