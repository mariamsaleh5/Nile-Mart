
package org.route;

import java.math.BigDecimal;

public enum Zone {

    GREATER_CAIRO(50, true),
    ALEX_DELTA(65, true),
    CANAL(70, true),
    UPPER_EGYPT(85, true),
    REMOTE(120, false);

    private final BigDecimal fee;
    private final boolean freeShippingEligible;

    Zone(double fee, boolean freeShippingEligible) {
        this.fee = BigDecimal.valueOf(fee);
        this.freeShippingEligible = freeShippingEligible;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public boolean isFreeShippingEligible() {
        return freeShippingEligible;
    }
}
