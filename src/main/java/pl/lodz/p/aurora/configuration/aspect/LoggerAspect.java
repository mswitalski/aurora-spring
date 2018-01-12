package pl.lodz.p.aurora.configuration.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.mus.domain.entity.User;

import java.util.Arrays;

/**
 * Logger aspect.
 */
@Aspect
@Component
public class LoggerAspect {

    private static Logger logger = Logger.getLogger(LoggerAspect.class.getName());

    @Around("pl.lodz.p.aurora.configuration.aspect.AspectPointcut.anyControllerAndServiceBean()")
    public Object aroundAnyControllerOrServiceCall(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StringBuilder messageToLog = new StringBuilder("Invoked business method: ")
                .append(proceedingJoinPoint.getTarget().getClass().getName())
                .append(".").append(proceedingJoinPoint.getSignature().getName()).append("()");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            messageToLog.append(" Invoked by principal: ").append(((User) auth.getPrincipal()).getUsername());
        }

        messageToLog.append(" Parameters values: ");
        Object[] params = proceedingJoinPoint.getArgs();

        if (params.length > 0) {
            Arrays.stream(params).forEach(param -> {
                if (param == null) {
                    messageToLog.append("null ");

                } else {
                    messageToLog.append(param.toString()).append(" ");
                }
            });

        } else {
            messageToLog.append("no invocation arguments ");
        }

        Object result;

        try {
            long startTime = System.currentTimeMillis();
            result = proceedingJoinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            messageToLog.append(" Processing time: ").append(duration).append("ms");

        } catch (Throwable e) {
            messageToLog.append(" Thrown exception: ").append(e.toString());

            throw e;
        }

        messageToLog.append(" Returned value: ");

        if (null == result) {
            messageToLog.append("null");

        } else {
            messageToLog.append(result.toString());
        }

        logger.info(messageToLog.toString());

        return result;
    }
}
