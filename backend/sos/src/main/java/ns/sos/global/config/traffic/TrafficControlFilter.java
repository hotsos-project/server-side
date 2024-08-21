package ns.sos.global.config.traffic;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class TrafficControlFilter implements Filter {

    private static final int MAX_REQUESTS = 10000;
    private final RequestCounter requestCounter;

    public TrafficControlFilter(RequestCounter requestCounter) {
        this.requestCounter = requestCounter;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (requestCounter.increment() > MAX_REQUESTS) {
            requestCounter.decrement();
            response.getWriter().write("Server is too busy, please try again later.");
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().close();
            return;
        }

        try {
            chain.doFilter(request, response);
        } finally {
            requestCounter.decrement();
            log.info("Request completed. Current request count: {}", requestCounter.getCount());
        }
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
