package pl.lodz.p.aurora.configuration.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Class defining pointcuts for Spring AOP feature.
 */
public class AspectPointcut {

    @Pointcut("bean(*Controller)")
    public void anyControllerBean() {}

    @Pointcut("bean(*ServiceImpl)")
    public void anyServiceBean() {}

    @Pointcut("anyControllerBean() || anyServiceBean()")
    public void anyControllerAndServiceBean() {}
}
