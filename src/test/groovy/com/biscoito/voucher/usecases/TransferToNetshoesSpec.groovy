package com.biscoito.voucher.usecases

import com.biscoito.voucher.domains.Customer
import com.biscoito.voucher.domains.VoucherEvent
import com.biscoito.voucher.exceptions.InsuficientFundsException
import com.biscoito.voucher.gateways.HederaHelper
import com.biscoito.voucher.gateways.VoucherGateway
import com.biscoito.voucher.gateways.inmemory.VoucherEventGatewayInMemory
import com.hedera.hashgraph.sdk.TransactionRecord
import spock.lang.Specification

class TransferToNetshoesSpec extends Specification {

    def hederaHelper = Mock(HederaHelper)
    def findOrCreateCustomer = Mock(FindOrCreateCustomerAccount)
    def voucherGateway = Mock(VoucherGateway)
    def calc = new TinybarsCalculator()
    def transferToNetshoes = new TransferToNetshoes(
            hederaHelper,
            findOrCreateCustomer,
            new VoucherEventGatewayInMemory(),
            voucherGateway,
            calc)

    def setup() {
        hederaHelper.getOperatorId() >> "NS123"
        findOrCreateCustomer.execute(_, _) >> new Customer([
                accountId: "555",
                shaPassword: "xyz",
                customerIdentifier: "joseph@gmail.com"
        ])
    }

    def "Transfer 100,00 reais from client Joseph to Netshoes"() {
        given: "a customer Joseph"
        def customerIdentifier = "joseph@gmail.com"
        and: "amount of 100,00 reais"
        def amount = 10000l
        when: "transfer 100,00 reais to joseph"
        def event = transferToNetshoes.execute(customerIdentifier, "xyz", amount)
        then: "event credit was created"
        event.type == VoucherEvent.EventType.DEBIT
        event.amountInCents == 10000l
        event.amountInCentsTinybar == 20833333333l
        event.customerIdentifier == customerIdentifier
        event.description == "test debit"

        voucherGateway.getBalance("555") >> calc.toTinybars(10000000)
        voucherGateway.transfer(_, _, _, _) >>
                new TransactionRecord(com.hedera.hashgraph.sdk.proto.TransactionRecord.newBuilder()
                        .setTransactionFee(1l)
                        .setMemo("test debit")
                        .build())
    }

    def "Transfer 100,00 reais from Joseph to Netshoes when no balance"() {
        given: "a customer Joseph"
        def customerIdentifier = "joseph@gmail.com"
        and: "amount of 100,00 reais"
        def amount = 10000l
        when: "transfer 100,00 reais to joseph"
        transferToNetshoes.execute(customerIdentifier, "xyz", amount)
        then: "an error occurs because no balance"
        thrown InsuficientFundsException

        voucherGateway.getBalance("555") >> 0
    }

    def "Transfer 100,00 reais from Joseph to Netshoes when  insuficient funds"() {
        given: "a new customer"
        def customerIdentifier = "joseph@gmail.com"
        and: "amount of 100,00 reais"
        def amount = 10000l
        when: "transfer 100,00 reais to joseph"
        transferToNetshoes.execute(customerIdentifier, "xyz", amount)
        then: "an error occurs because no funds"
        thrown InsuficientFundsException

        voucherGateway.getBalance("NS123") >> calc.toTinybars(5000)
    }
}
