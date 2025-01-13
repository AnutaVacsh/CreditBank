package ru.vaschenko.gateway.client.deal;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vaschenko.gateway.dto.FinishRegistrationRequestDto;
import ru.vaschenko.gateway.util.ApiPath;

@FeignClient(value = "deal-mc", url = "${client.deal.url}")
public interface DealClient {
  @PostMapping(ApiPath.CALCULATE_ID)
  void calculate(
      @PathVariable UUID statementId,
      @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto);

  @PostMapping(ApiPath.CALCULATE_ID_SEND)
  void sendCodeDocument(@PathVariable UUID statementId);

  @PostMapping(ApiPath.CALCULATE_ID_SIGN)
  void signCodeDocument(@PathVariable UUID statementId);

  @PostMapping(ApiPath.CALCULATE_ID_CODE)
  void codeDocument(@PathVariable UUID statementId, @RequestParam String sesCode);
}
