package org.noear.dami.spring.boot.annotation;

import org.noear.dami.spring.boot.DamiImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * DamiScan
 *
 * @author tangxin
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DamiImportBeanDefinitionRegistrar.class)
public @interface DamiScan {


	@AliasFor("basePackages")
	String[] value() default {};


	@AliasFor("value")
	String[] basePackages() default {};

	Class<?>[] basePackageClasses() default {};

}
