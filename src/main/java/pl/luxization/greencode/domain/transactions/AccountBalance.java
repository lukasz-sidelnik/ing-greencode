package pl.luxization.greencode.domain.transactions;

import javax.money.MonetaryAmount;

import static java.util.Objects.requireNonNull;

public record AccountBalance(Account account, int debitCount, int creditCount, MonetaryAmount balance)
        implements Comparable<AccountBalance> {

    public AccountBalance {
        requireNonNull(balance);
        requireNonNull(account);
    }

    AccountBalance credit(MonetaryAmount amount) {
        requireNonNull(amount);
        return new AccountBalance(account, debitCount, creditCount + 1, balance.add(amount));
    }

    AccountBalance debit(MonetaryAmount amount) {
        requireNonNull(amount);
        return new AccountBalance(account, debitCount + 1, creditCount, balance.subtract(amount));
    }

    public AccountBalance plus(AccountBalance otherBalance) {
        requireNonNull(otherBalance);
        if (!account.equals(otherBalance.account)) {
            throw new IllegalArgumentException("cannot add balances for different accounts");
        }
        return new AccountBalance(
                account,
                debitCount + otherBalance.debitCount,
                creditCount + otherBalance.creditCount,
                balance.add(otherBalance.balance)
        );
    }

    @Override
    public int compareTo(AccountBalance otherBalance) {
        return balance.compareTo(otherBalance.balance);
    }

}
