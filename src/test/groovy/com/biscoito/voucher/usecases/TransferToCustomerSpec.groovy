package com.biscoito.voucher.usecases

import com.biscoito.voucher.domains.Customer
import com.biscoito.voucher.domains.VoucherEvent
import com.biscoito.voucher.gateways.HederaHelper
import com.biscoito.voucher.gateways.VoucherGateway
import com.biscoito.voucher.gateways.inmemory.VoucherEventGatewayInMemory
import com.biscoito.voucher.gateways.inmemory.VoucherGatewayInMemory
import com.hedera.hashgraph.sdk.TransactionRecord
import spock.lang.Specification

class TransferToCustomerSpec extends Specification {

    def hederaHelper = Mock(HederaHelper)
    def findOrCreateCustomer = Mock(FindOrCreateCustomerAccount)
    def voucherGateway = Mock(VoucherGateway)
    def transferToCustomer = new TransferToCustomer(
            hederaHelper,
            findOrCreateCustomer,
            new VoucherEventGatewayInMemory(),
            voucherGateway)

    def setup() {
        hederaHelper.getOperatorId() >> "NS123"
    }

    def "Transfer 10 from netshoes to new client joseph"() {
        given: "a new customer"
        def newCustomer = new Customer()
        newCustomer.accountId = "555"
        newCustomer.shaPassword = "xyz"
        newCustomer.customerIdentifier = "joseph@gmail.com"
        def customerIdentifier = "joseph@gmail.com"
        and: "amount of 10"
        def amount = 10l
        when: "transfer 10 to joseph"
        def event = transferToCustomer.execute(customerIdentifier, "xyz", amount)
        then: "event credit was created"
        event.type == VoucherEvent.VoucherType.CREDIT
        event.amount == 10l
        event.customerIdentifier == customerIdentifier
        event.description == "test credit"

        findOrCreateCustomer.execute(_, _) >> newCustomer
        voucherGateway.getBalance("NS123") >> 100000
        voucherGateway.getBalance("555") >> 0
        voucherGateway.credit(_, _) >>
          new TransactionRecord(com.hedera.hashgraph.sdk.proto.TransactionRecord.newBuilder()
                  .setTransactionFee(1l)
                  .setMemo("test credit")
                  .build())
    }

}
