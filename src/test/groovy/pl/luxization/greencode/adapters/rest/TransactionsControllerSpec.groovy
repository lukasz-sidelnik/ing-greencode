package pl.luxization.greencode.adapters.rest


import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class TransactionsControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    def "should return correct json"() {
        String req = this.getClass().getResourceAsStream("/transactions/example_request.json").text
        HttpRequest<String> httpRequest = HttpRequest.POST("/transactions/report", req)
        when:
            HttpResponse response = client.toBlocking().exchange(httpRequest, String)

        then:
            response.status == HttpStatus.OK
            response.body() == this.getClass().getResourceAsStream("/transactions/example_response.json").text
    }

    def "should return 200"() {
        when:
            HttpResponse response = callEndpoint()

        then:
            response.status == HttpStatus.OK
            with(response.body()) {
                it.size() == 2
            }
    }

    private HttpResponse callEndpoint() {
        HttpRequest<List<Map>> httpRequest = HttpRequest.POST("/transactions/report", [["debitAccount": "123", "creditAccount": "567", "amount": "4.6"]] as List<Map>)
        return client.toBlocking().exchange(httpRequest, Argument.listOf(Argument.mapOf(String, Object)))
    }

}
