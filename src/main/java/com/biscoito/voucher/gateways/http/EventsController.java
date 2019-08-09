package com.biscoito.voucher.gateways.http;

import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.domains.VoucherEvent.EventType;
import com.biscoito.voucher.gateways.VoucherEventGateway;
import com.biscoito.voucher.gateways.http.json.VoucherEventOutput;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventsController {

  private final VoucherEventGateway gateway;

  @GetMapping
  @ResponseStatus(value = HttpStatus.OK)
  public Collection<VoucherEventOutput> findByType(@RequestParam(name = "type") final EventType type) {
    final Collection<VoucherEvent> events = gateway.findAllByTypeSortable(type);

    return events.stream()
        .map(voucherEvent -> VoucherEventOutput.builder()
          .id(voucherEvent.getId())
          .customerIdentifier(voucherEvent.getCustomerIdentifier())
          .description(voucherEvent.getDescription())
          .when(voucherEvent.getWhen())
          .amount(voucherEvent.getAmountInCents())
          .build())
        .collect(Collectors.toList());
  }

  @GetMapping(path = "/user")
  @ResponseStatus(value = HttpStatus.OK)
  public Collection<VoucherEventOutput> findByUser(
      @RequestParam(name = "customerIdentifier") final String customerIdentifier) {
    final Collection<VoucherEvent> events = gateway.findAllByCustomerSortable(customerIdentifier);

    return events.stream()
        .map(voucherEvent -> VoucherEventOutput.builder()
            .id(voucherEvent.getId())
            .customerIdentifier(voucherEvent.getCustomerIdentifier())
            .description(voucherEvent.getDescription())
            .when(voucherEvent.getWhen())
            .amount(voucherEvent.getAmountInCents())
            .build())
        .collect(Collectors.toList());
  }

}
