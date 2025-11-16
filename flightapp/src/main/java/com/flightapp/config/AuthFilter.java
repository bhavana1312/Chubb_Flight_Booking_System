package com.flightapp.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthFilter implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String path = request.getRequestURI();
		if (path.startsWith("/api/v1.0/flight/airline/inventory/add")) {
			String token = request.getHeader("Authorization");
			if (token == null || !token.contains(System.getProperty("app.admin.token", "ADMIN_SECRET_TOKEN"))) {
				((HttpServletResponse) res).sendError(HttpServletResponse.SC_FORBIDDEN, "Admin token required");
				return;
			}
		}
		chain.doFilter(req, res);
	}
}
