package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShippingQuoteService {

    private final ShippingCalculator zoneCalculator;
    private final ShippingCalculator freeShippingCalculator;
    private final BigDecimal freeShippingThreshold;

    public ShippingQuoteService(
            ShippingCalculator zoneCalculator,
            @Qualifier("freeShipping") ShippingCalculator freeShippingCalculator,
            @Value("${store.free-shipping-threshold}") BigDecimal freeShippingThreshold) {

        this.zoneCalculator = zoneCalculator;
        this.freeShippingCalculator = freeShippingCalculator;
        this.freeShippingThreshold = freeShippingThreshold;
    }

    public BigDecimal quote(Governorate g, BigDecimal subtotal) {

        if (subtotal.compareTo(freeShippingThreshold) >= 0
                && g.getZone().isFreeShippingEligible()) {

            return freeShippingCalculator.calculate(g);
        }

        return zoneCalculator.calculate(g);
    }
}