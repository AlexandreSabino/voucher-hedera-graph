package com.biscoito.voucher.usecases

import com.biscoito.voucher.domains.Customer
import com.biscoito.voucher.gateways.VoucherGateway
import spock.lang.Specification

class GetBalanceSpec extends Specification {

    def voucherGateway = Mock(VoucherGateway)
    def findCustomer = Mock(FindCustomer)
    def calc = new TinybarsCalculator()
    def getBalance = new GetBalance(voucherGateway, findCustomer, calc)

    def setup() {
        findCustomer.execute("joseph@gmail.com", _) >> Optional.of(new Customer([
                accountId: "555",
                shaPassword: "xyz",
                customerIdentifier: "joseph@gmail.com"
        ]))

        findCustomer.execute("invalid", _) >> Optional.empty()
    }

    def "Get balance from a valid customer" () {
        given: "joseph customer"
        def customerIdentifier = "joseph@gmail.com"
        when: "get balance of joseph"
        def balance = getBalance.execute(customerIdentifier, "xyz")
        then: "balance equal to 500,00 reais"
        balance == 50000l
        voucherGateway.getBalance("555") >> calc.toTinybars(50000l)
    }

    def "Get zero balance from a valid customer" () {
        given: "joseph customer"
        def customerIdentifier = "joseph@gmail.com"
        when: "get balance of joseph"
        def balance = getBalance.execute(customerIdentifier, "xyz")
        then: "balance equal to 500,00 reais"
        balance == 0l
        voucherGateway.getBalance("555") >> calc.toTinybars(0l)
    }

    def "Get balance from a invalid customer" () {
        given: "invalid customer"
        def customerIdentifier = "invalid"
        when: "get balance of joseph"
        getBalance.execute(customerIdentifier, "xyz")
        then: "an error occurs because customer not founded"
        thrown RuntimeException
    }

}
