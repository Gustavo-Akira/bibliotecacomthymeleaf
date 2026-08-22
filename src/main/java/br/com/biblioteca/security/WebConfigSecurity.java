package br.com.biblioteca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity {

	private final ImplementUserService service;

	public WebConfigSecurity(ImplementUserService service) {
		this.service = service;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								HttpMethod.GET,
								"/",
								"/livros/**",
								"/editoras/**",
								"/perfil/cadastrar"
						).permitAll()
						.requestMatchers(HttpMethod.GET, "/dashboard/**")
						.hasAnyRole("ADMIN", "SECRETARY")
						.requestMatchers("/perfis/**", "/compras/**")
						.hasAnyRole("USER")
						.anyRequest()
						.authenticated()
				)
				.formLogin(form -> form
						.permitAll()
						.loginPage("/login")
						.defaultSuccessUrl("/entrar")
						.failureUrl("/login?error=true")
				)
				.logout(logout -> logout
						.logoutSuccessUrl("/login")
						.logoutRequestMatcher(PathPatternRequestMatcher.withDefaults().matcher("/logout"))
				);

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(service);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}