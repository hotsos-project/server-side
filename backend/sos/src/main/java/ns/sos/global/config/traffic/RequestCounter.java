package ns.sos.global.config.traffic;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestCounter {

    private final AtomicInteger counter = new AtomicInteger(0);

    public int increment() {
        return counter.incrementAndGet();
    }

    public int decrement() {
        return counter.decrementAndGet();
    }

    public int getCount() {
        return counter.get();
    }
}