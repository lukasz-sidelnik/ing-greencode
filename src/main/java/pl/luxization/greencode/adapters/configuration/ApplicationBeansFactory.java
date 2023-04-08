package pl.luxization.greencode.adapters.configuration;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.javamoney.moneta.spi.FastMoneyProducer;
import org.javamoney.moneta.spi.MonetaryAmountProducer;

@Factory
public class ApplicationBeansFactory {

    @Bean
    public MonetaryAmountProducer monetaryAmountProducer() {
        return new FastMoneyProducer();
    }
}
