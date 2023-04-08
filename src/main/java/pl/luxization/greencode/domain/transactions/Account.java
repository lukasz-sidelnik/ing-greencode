package pl.luxization.greencode.domain.transactions;

import static java.util.Objects.requireNonNull;

public record Account(String id) {
    public Account {
        requireNonNull(id);
    }
}
