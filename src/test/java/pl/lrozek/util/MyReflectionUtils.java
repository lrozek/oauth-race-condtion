package pl.lrozek.util;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

public class MyReflectionUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getField( Object input, String fieldName ) {
        Field field = ReflectionUtils.findField( input.getClass(), fieldName );
        ReflectionUtils.makeAccessible( field );
        return (T) ReflectionUtils.getField( field, input );
    }

}
