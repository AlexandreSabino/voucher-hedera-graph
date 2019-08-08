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
    def transferToNetshoes = new TransferToNetshoes(
            hederaHelper,
            findOrCreateCustomer,
            new VoucherEventGatewayInMemory(),
            voucherGateway)

    def setup() {
        hederaHelper.getOperatorId() >> "NS123"
    }

    def "Transfer 10 from client Joseph to Netshoes"() {
        given: "a customer Joseph"
        def customerIdentifier = "joseph@gmail.com"
        and: "amount of 10"
        def amount = 10l
        when: "transfer 10 to joseph"
        def event = transferToNetshoes.execute(customerIdentifier, "xyz", amount)
        then: "event credit was created"
        event.type == VoucherEvent.VoucherType.DEBIT
        event.amount == 10l
        event.customerIdentifier == customerIdentifier
        event.description == "test debit"

        findOrCreateCustomer.execute(_, _) >> new Customer([
                accountId: "555",
                shaPassword: "xyz",
                customerIdentifier: "joseph@gmail.com"
        ])
        voucherGateway.getBalance("NS123") >> 100000
        voucherGateway.getBalance("555") >> 100000
        voucherGateway.transfer(_, _, _, _) >>
                new TransactionRecord(com.hedera.hashgraph.sdk.proto.TransactionRecord.newBuilder()
                        .setTransactionFee(1l)
                        .setMemo("test debit")
                        .build())
    }

    def "Transfer 10 from Joseph to Netshoes when no balance"() {
        given: "a customer Joseph"
        def customerIdentifier = "joseph@gmail.com"
        and: "amount of 10"
        def amount = 10l
        when: "transfer 10 to joseph"
        transferToNetshoes.execute(customerIdentifier, "xyz", amount)
        then: "an error occurs because no balance"
        thrown InsuficientFundsException

        findOrCreateCustomer.execute(_, _) >> new Customer([
                accountId: "555",
                shaPassword: "xyz",
                customerIdentifier: "joseph@gmail.com"
        ])
        voucherGateway.getBalance("555") >> 0
    }
}
