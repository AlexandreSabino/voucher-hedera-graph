package com.biscoito.voucher.usecases

import com.biscoito.voucher.domains.Customer
import spock.lang.Specification

class FindOrCreateCustomerAccountSpec extends Specification {

    def findCustomer = Mock(FindCustomer)
    def createCustomer = Mock(CreateCustomer)
    def findOrCreateCustomerAccount = new FindOrCreateCustomerAccount(findCustomer, createCustomer)

    def "Find a customer already created" () {
        given: "customer joseph"
        def customerIdentifier = "joseph@gmail.com"
        when: "find or create customer account"
        def customer = findOrCreateCustomerAccount.execute(customerIdentifier, "xyz")
        then: "customer are founded"
        customer != null
        customer.customerIdentifier == customerIdentifier

        findCustomer.execute(customerIdentifier, _) >> Optional.of(new Customer([
                accountId: "555",
                shaPassword: "xyz",
                customerIdentifier: "joseph@gmail.com"
        ]))

        0 * createCustomer.execute(customerIdentifier, _)
    }

    def "Find a customer after creation" () {
        given: "new customer greg"
        def customerIdentifier = "greg@gmail.com"
        when: "find or create customer account"
        def customer = findOrCreateCustomerAccount.execute(customerIdentifier, "ppp")
        then: "customer are founded"
        customer != null
        customer.customerIdentifier == customerIdentifier

        findCustomer.execute(customerIdentifier, _) >> Optional.empty()

        1 * createCustomer.execute(customerIdentifier, _) >> new Customer([
                accountId: "444",
                shaPassword: "ppp",
                customerIdentifier: "greg@gmail.com"
        ])
    }
}
