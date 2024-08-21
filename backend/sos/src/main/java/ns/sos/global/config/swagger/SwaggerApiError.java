package ns.sos.global.config.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ns.sos.global.error.ErrorCode;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerApiError {

    ErrorCode[] value();
}
