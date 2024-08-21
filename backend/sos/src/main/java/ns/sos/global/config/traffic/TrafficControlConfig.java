package ns.sos.global.config.traffic;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class TrafficControlConfig {

    @Bean
    public RequestCounter requestCounter() {
        return new RequestCounter();
    }

//    @Bean
//    public FilterRegistrationBean<TrafficControlFilter> trafficControlFilterRegistration(RequestCounter requestCounter) {
//        FilterRegistrationBean<TrafficControlFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new TrafficControlFilter(requestCounter));
//        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 가장 높은 우선순위로 설정
//        return registrationBean;
//    }
}
