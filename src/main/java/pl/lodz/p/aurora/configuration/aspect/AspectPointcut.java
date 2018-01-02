package pl.lodz.p.aurora.configuration.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Class defining pointcuts for Spring AOP feature.
 */
public class AspectPointcut {

    @Pointcut("bean(*Controller) || bean(*ServiceImpl)")
    public void anyControllerAndServiceBean() {
    }
}
