package com.biscoito.voucher.usecases

import com.biscoito.voucher.domains.Customer
import com.biscoito.voucher.gateways.CustomerGateway
import spock.lang.Specification

class FindCustomerSpec extends Specification {

    def gateway = Mock(CustomerGateway)
    def findCustomer = new FindCustomer(gateway)

    def setup() {
        gateway.findOne("joseph@gmail.com") >> Optional.of(new Customer([
                accountId: "555",
                shaPassword: "xyz",
                customerIdentifier: "joseph@gmail.com"
        ]))

        gateway.findOne("greg@gmail.com") >> Optional.empty()
    }

    def "Find a customer already created" () {
        given: "customer joseph"
        def customerIdentifier = "joseph@gmail.com"
        when: "find customer by identifier"
        def opCustomer = findCustomer.execute(customerIdentifier, "xyz")
        then: "customer data are founded"
        opCustomer.isPresent()
        def customer = opCustomer.get()
        customer.customerIdentifier == customerIdentifier
    }

    def "Find an unknown customer" () {
        given: "customer joseph"
        def customerIdentifier = "greg@gmail.com"
        when: "find customer by identifier"
        def opCustomer = findCustomer.execute(customerIdentifier, "xyz")
        then: "no customer founded"
        !opCustomer.isPresent()
    }

}
