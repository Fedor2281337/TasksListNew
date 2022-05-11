package com.example.demo.config;

import com.example.demo.repository.JwtTokenRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Настройки веб-безопасности
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * Сервис для работы с пользователями
     */
    private final UserService userService;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;


    /**
     * Объект для работы с паролями
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * Конструктор сервиса пользователей
     *
     * @param userService - сервис для работы с пользователями
     */
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Задать настройки безопасности
     *
     * @param httpSecurity - объект веб-безопасности
     * @throws Exception - исклбючение
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .addFilterAt(new JwtCsrfFilter(jwtTokenRepository, resolver), CsrfFilter.class)
                        // не использовать csrf-токен
                        .csrf().ignoringAntMatchers("/**")
                        .and()
                        // позволяет ограничивать запросы
                        .authorizeRequests()
                        //Доступ только для не зарегистрированных пользователей
                        .antMatchers("/auth/register", "/auth/login").not().fullyAuthenticated()
                        //Доступ только для пользователей с ролью Администратор
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        //Доступ только для всех авторизованных пользователей
                        .antMatchers("/taskList", "/tasks/**").authenticated()
                        //Доступ разрешен всем
                        .antMatchers("/", "/js/**", "/css/**", "/img/**", "/contact").permitAll()
                        //Все остальные страницы требуют аутентификации
                        .anyRequest().authenticated()
                        .and()
                        // Настройка для входа в систему
                        .formLogin()
                        // страница регистрации
                        .loginPage("/auth/login")
                        //Перенаправление на главную страницу после успешного входа
                        .defaultSuccessUrl("/")
                        .permitAll()
                        .and()
                        // настройка выхода
                        .logout()
                        .permitAll()
                        .logoutSuccessUrl("/").and()
                .httpBasic()
                .authenticationEntryPoint(((request, response, e) -> resolver.resolveException(request, response, null, e)));;
    }

    /**
     * Глобальные настройки
     *
     * @param auth объект для настройки
     * @throws Exception - исключение
     */
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}