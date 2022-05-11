package com.example.demo.repository;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;

/**
 * Репозиторий токенов
 */
@Repository
public class JwtTokenRepository implements CsrfTokenRepository {
    /**
     * секретный код шифрования
     */
    @Getter
    private final String secret;

    /**
     * Конструктор
     */
    public JwtTokenRepository() {
        this.secret = "springrest";
    }

    /**
     * Функция-генератор токена
     *
     * @param httpServletRequest - данные запроса
     * @return CSRF токен
     */
    @Override
    public CsrfToken generateToken(HttpServletRequest httpServletRequest) {
        // получаем случайную строку с id
        String id = UUID.randomUUID().toString().replace("-", "");
        // определяем время жизни токена: он будет актуален 30 минут
        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now().plusMinutes(30)
                .atZone(ZoneId.systemDefault()).toInstant());

        // рассчитываем токен
        String token = "";
        try {
            token = Jwts.builder()
                    .setId(id)
                    // время создания
                    .setIssuedAt(now)
                    .setNotBefore(now)
                    // дата сгорания
                    .setExpiration(exp)
                    // шифрование
                    .signWith(SignatureAlgorithm.HS256, secret)
                    // сборка
                    .compact();
        } catch (JwtException e) {
            e.printStackTrace();
        }
        // возвращаем новый токен
        return new DefaultCsrfToken("x-csrf-token", "_csrf", token);
    }

    /**
     * Сохранение токена
     *
     * @param csrfToken - токен
     * @param request   - запрос
     * @param response  - ответ
     */
    @Override
    public void saveToken(CsrfToken csrfToken, HttpServletRequest request, HttpServletResponse response) {
        // если токен задан
        if (Objects.nonNull(csrfToken)) {
            // если в заголовке ответа не содержится нужный заголовок
            if (!response.getHeaderNames().contains(ACCESS_CONTROL_EXPOSE_HEADERS))
                // добавляем его
                response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, csrfToken.getHeaderName());
            // если в заголовке ответа содержится имя токена
            if (response.getHeaderNames().contains(csrfToken.getHeaderName()))
                // задаём значение токена
                response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
            else
                // иначе просто добавляем
                response.addHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }
    }

    /**
     * Загрузить токен
     *
     * @param request - запрос
     * @return токен
     */
    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    /**
     * Очистить токен
     *
     * @param response - ответ
     */
    public void clearToken(HttpServletResponse response) {
        if (response.getHeaderNames().contains("x-csrf-token"))
            response.setHeader("x-csrf-token", "");
    }
}