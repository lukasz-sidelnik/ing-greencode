package pl.luxization.greencode.adapters.rest;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.serde.annotation.Serdeable;
import org.javamoney.moneta.spi.MonetaryAmountProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.luxization.greencode.domain.transactions.Account;
import pl.luxization.greencode.domain.transactions.AccountBalance;
import pl.luxization.greencode.domain.transactions.Transaction;
import pl.luxization.greencode.domain.transactions.BookkeepingService;
import reactor.core.publisher.Flux;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller("/transactions")
final class TransactionsController {

    private final BookkeepingService bookkeepingService;
    private final MonetaryAmountProducer monetaryAmountProducer;

    public TransactionsController(BookkeepingService bookkeepingService, MonetaryAmountProducer monetaryAmountProducer) {
        this.bookkeepingService = bookkeepingService;
        this.monetaryAmountProducer = monetaryAmountProducer;
    }

    private static final Logger logger = LoggerFactory.getLogger(TransactionsController.class);

    @Post(value = "/report", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public Flux<AccountBalanceResponse> calculateAccountBalanceReport(@Body Flux<TransactionRequest> transactions) {
        return transactions
                .doOnEach(transactionRequest -> logger.debug("received transactions request: " + transactionRequest.toString()))
                .map(this::toDomain)
                .concatMapIterable(bookkeepingService::recordBalances)
                .groupBy(AccountBalance::account)
                .flatMap(accountByBalances -> accountByBalances.reduce(AccountBalance::plus))
                .sort()
                .map(TransactionsController::toResponse);
    }

    private Transaction toDomain(TransactionRequest request) {
        final MonetaryAmount amount = monetaryAmountProducer.create(Monetary.getCurrency("PLN"), request.amount());
        return new Transaction(new Account(request.creditAccount()), new Account(request.debitAccount()), amount);
    }

    private static AccountBalanceResponse toResponse(AccountBalance accountBalance) {
        return new AccountBalanceResponse(accountBalance.account().id(), accountBalance.debitCount(), accountBalance.creditCount(), accountBalance.balance().getNumber().floatValue());
    }
}

@Serdeable
record TransactionRequest(String debitAccount, String creditAccount, float amount) {
}

@Serdeable
record AccountBalanceResponse(String account, int debitCount, int creditCount, float balance) {
}