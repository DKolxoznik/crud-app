package com.crud_app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @InjectMocks
    private LoggingAspect loggingAspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Test
    void aspectProceedsAndReturnsResult() throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("ItemService.saveItem(..)");
        when(joinPoint.proceed()).thenReturn("ok");

        Object result = loggingAspect.logServiceExecution(joinPoint);

        assertEquals("ok", result);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void aspectRethrowsException() throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("ItemService.deleteItem(..)");
        when(joinPoint.proceed()).thenThrow(new RuntimeException("boom"));

        try {
            loggingAspect.logServiceExecution(joinPoint);
        } catch (RuntimeException ex) {
            assertEquals("boom", ex.getMessage());
        }

        verify(joinPoint, times(1)).proceed();
    }
}
