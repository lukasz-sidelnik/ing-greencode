package pl.luxization.greencode.domain.transactions;

import javax.money.MonetaryAmount;

import static java.util.Objects.requireNonNull;

public record Transaction(Account creditedAccount, Account debitedAccount, MonetaryAmount amount) {
    public Transaction {
        requireNonNull(creditedAccount);
        requireNonNull(debitedAccount);
        requireNonNull(amount);
    }
}
