package com.example.demo.config;

import com.example.demo.repository.JwtTokenRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр запросов для поддержки работы токенов
 */
public class JwtCsrfFilter extends OncePerRequestFilter {
    /**
     * Репозиторий токенов
     */
    private final CsrfTokenRepository tokenRepository;
    /**
     * Обработчик исключений
     */
    private final HandlerExceptionResolver resolver;

    /**
     * Конструктор фильтра
     *
     * @param tokenRepository - репозиторий токенов
     * @param resolver        - обработчик исключений
     */
    public JwtCsrfFilter(CsrfTokenRepository tokenRepository, HandlerExceptionResolver resolver) {
        this.tokenRepository = tokenRepository;
        this.resolver = resolver;
    }

    /**
     * Фильтрация запросов
     *
     * @param request     - запрос
     * @param response    - ответ
     * @param filterChain - объект фильтра
     * @throws ServletException - исключения сервера
     * @throws IOException      - исключения ввода/вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // задаём запросу объект ответа
        request.setAttribute(HttpServletResponse.class.getName(), response);
        // получаем токен
        CsrfToken csrfToken = this.tokenRepository.loadToken(request);
        // если токен утерян
        boolean missingToken = csrfToken == null;
        if (missingToken) {
            // получаем новый
            csrfToken = this.tokenRepository.generateToken(request);
            // сохраняем
            this.tokenRepository.saveToken(csrfToken, request, response);
        }
        // сохраняем токен в запросе
        request.setAttribute(CsrfToken.class.getName(), csrfToken);
        request.setAttribute(csrfToken.getParameterName(), csrfToken);

        // пропускаем дальше все запросы, которые начинаются с `/tasks/`, если только это не запрос на регистрацию
        if (!request.getServletPath().startsWith("/tasks/") || request.getServletPath().equals("/tasks/login")) {
            try {
                // фильтруем запрос
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                // обрабатываем исключения
                resolver.resolveException(request, response, null, new MissingCsrfTokenException(csrfToken.getToken()));
            }
        } else {
            // получаем текущий токен
            String actualToken = request.getHeader(csrfToken.getHeaderName());
            // если токен утерян
            if (actualToken == null) {
                actualToken = request.getParameter(csrfToken.getParameterName());
            }
            try {
                //пробуем получить его
                if (!StringUtils.isEmpty(actualToken)) {
                    // по секретному ключу расшифровываем токен
                    Jwts.parser()
                            .setSigningKey(((JwtTokenRepository) tokenRepository).getSecret())
                            .parseClaimsJws(actualToken);
                    // выполняем фильтрацию
                    filterChain.doFilter(request, response);
                } else
                    // обрабатываем исключение
                    resolver.resolveException(request, response, null, new InvalidCsrfTokenException(csrfToken, actualToken));
            } catch (JwtException e) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Invalid CSRF token found for " + UrlUtils.buildFullRequestUrl(request));
                }

                if (missingToken) {
                    resolver.resolveException(request, response, null, new MissingCsrfTokenException(actualToken));
                } else {
                    resolver.resolveException(request, response, null, new InvalidCsrfTokenException(csrfToken, actualToken));
                }
            }
        }
    }
}