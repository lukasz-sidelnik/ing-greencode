package pl.luxization.greencode.domain.transactions;

import jakarta.inject.Singleton;
import org.javamoney.moneta.spi.MonetaryAmountProducer;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.List;

import static javax.money.Monetary.getCurrency;

@Singleton
public class BookkeepingService {
    private static final CurrencyUnit DEFAULT_CURRENCY = getCurrency("PLN");
    private final MonetaryAmount initialBalanceAmount;

    public BookkeepingService(MonetaryAmountProducer monetaryAmountProducer) {
        this.initialBalanceAmount = monetaryAmountProducer.create(DEFAULT_CURRENCY, 0);
    }

    public List<AccountBalance> recordBalances(Transaction transaction) {
        return List.of(
                createInitialBalance(transaction.creditedAccount()).credit(transaction.amount()),
                createInitialBalance(transaction.debitedAccount()).debit(transaction.amount())
        );
    }

    private AccountBalance createInitialBalance(Account account) {
        return new AccountBalance(account, 0, 0, initialBalanceAmount);
    }

}