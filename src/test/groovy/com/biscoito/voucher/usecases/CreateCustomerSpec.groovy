package com.biscoito.voucher.usecases

import com.biscoito.voucher.gateways.AccountGateway
import com.biscoito.voucher.gateways.inmemory.CustomerGatewayInMemory
import com.hedera.hashgraph.sdk.account.AccountId
import spock.lang.Specification

class CreateCustomerSpec extends Specification {

    def customerGateway = new CustomerGatewayInMemory()
    def accountGateway = Mock(AccountGateway)
    def createCustomer = new CreateCustomer(customerGateway, accountGateway)

    def setup() {
        customerGateway.clean()
        accountGateway.create(_) >> AccountId.fromString("0.0.1")
    }

    def "Create a new customer" () {
        given: "new client joseph"
        def customerIdentifier = "joseph@gmail.com"
        when: "execute creation"
        def customer = createCustomer.execute(customerIdentifier, "xyz")
        then: "a new customer are created"
        customer != null
        customer.customerIdentifier == customerIdentifier
        customer.accountId == "0.0.1"
    }

}
