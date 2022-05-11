package com.example.demo.controller;

import com.example.demo.repository.JwtTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Обработчик исключений
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Репозиторий токенов
     */
    private final JwtTokenRepository tokenRepository;

    /**
     * Конструктор обработчика исключений
     *
     * @param tokenRepository - репозиторий токенов
     */
    @Autowired
    public GlobalExceptionHandler(JwtTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Обработчик исключений
     *
     * @param ex       - исключение
     * @param request  - запрос
     * @param response - ответ
     * @return структура с информацией об ошибке
     */
    @ExceptionHandler({AuthenticationException.class, MissingCsrfTokenException.class, InvalidCsrfTokenException.class, SessionAuthenticationException.class})
    public ErrorInfo handleAuthenticationException(RuntimeException ex, HttpServletRequest request, HttpServletResponse response) {
        this.tokenRepository.clearToken(response);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return new ErrorInfo(UrlUtils.buildFullRequestUrl(request), "error.authorization");
    }

    /**
     * Структура для обработки исключений
     */
    public record ErrorInfo(String url, String info) {
    }
}