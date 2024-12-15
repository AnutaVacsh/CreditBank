package ru.vaschenko.deal.annotation;

import feign.FeignException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Retryable(
    retryFor = {
      FeignException.InternalServerError.class,
      FeignException.ServiceUnavailable.class,
      FeignException.GatewayTimeout.class
    },
    maxAttempts = 3,
    backoff = @Backoff(delay = 2000))
public @interface FeignRetryable {}
