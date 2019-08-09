package com.biscoito.voucher.usecases

import com.biscoito.voucher.domains.Customer
import com.biscoito.voucher.domains.VoucherEvent
import com.biscoito.voucher.exceptions.InsuficientFundsException
import com.biscoito.voucher.gateways.HederaHelper
import com.biscoito.voucher.gateways.VoucherGateway
import com.biscoito.voucher.gateways.inmemory.VoucherEventGatewayInMemory
import com.hedera.hashgraph.sdk.TransactionRecord
import spock.lang.Specification

class TransferToCustomerSpec extends Specification {

    def hederaHelper = Mock(HederaHelper)
    def findOrCreateCustomer = Mock(FindOrCreateCustomerAccount)
    def voucherGateway = Mock(VoucherGateway)
    def calc = new TinybarsCalculator()
    def transferToCustomer = new TransferToCustomer(
            hederaHelper,
            findOrCreateCustomer,
            new VoucherEventGatewayInMemory(),
            voucherGateway,
            calc)

    def setup() {
        hederaHelper.getNetshoesAccountId() >> "NS123"
        findOrCreateCustomer.execute(_, _) >> new Customer([
                accountId: "555",
                shaPassword: "xyz",
                customerIdentifier: "joseph@gmail.com"
        ])
        transferToCustomer.costTransaction = 100
    }

    def "Transfer 100,00 reais from Netshoes to new client Joseph"() {
        given: "a new customer"
        def customerIdentifier = "joseph@gmail.com"
        and: "amount of 100,00 reais"
        def amount = 10000l
        when: "transfer 100,00 reais to joseph"
        def event = transferToCustomer.execute(customerIdentifier, "xyz", amount)
        then: "event credit was created"
        event.type == VoucherEvent.EventType.CREDIT
        event.amountInCents == 10100l
        event.amountInCentsTinybar == 21041666666l
        event.customerIdentifier == customerIdentifier
        event.description == "test credit"

        voucherGateway.getBalance("NS123") >> calc.toTinybars(10000000)
        voucherGateway.getBalance("555") >> 0
        voucherGateway.transfer(_, _, _, _) >>
          new TransactionRecord(com.hedera.hashgraph.sdk.proto.TransactionRecord.newBuilder()
                  .setTransactionFee(1l)
                  .setMemo("test credit")
                  .build())
    }

    def "Transfer 100,00 reais from Netshoes to new client Joseph when no funds"() {
        given: "a new customer"
        def customerIdentifier = "joseph@gmail.com"
        and: "amount of 100,00 reais"
        def amount = 10000l
        when: "transfer 100,00 reais to joseph"
        transferToCustomer.execute(customerIdentifier, "xyz", amount)
        then: "an error occurs because no funds"
        thrown InsuficientFundsException

        voucherGateway.getBalance("NS123") >> 0
    }

    def "Transfer 100,00 reais from Netshoes to new client Joseph when insuficient funds"() {
        given: "a new customer"
        def customerIdentifier = "joseph@gmail.com"
        and: "amount of 100,00 reais"
        def amount = 10000l
        when: "transfer 100,00 reais to joseph"
        transferToCustomer.execute(customerIdentifier, "xyz", amount)
        then: "an error occurs because no funds"
        thrown InsuficientFundsException

        voucherGateway.getBalance("NS123") >> calc.toTinybars(5000)
    }

}
