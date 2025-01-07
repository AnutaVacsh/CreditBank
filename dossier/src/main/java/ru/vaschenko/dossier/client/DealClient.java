package ru.vaschenko.dossier.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vaschenko.dossier.dto.enums.ApplicationStatus;
import ru.vaschenko.dossier.util.ApiPath;

@FeignClient(value = "deal-mc", url = "${client.deal.url}")
public interface DealClient {

    @PutMapping(ApiPath.BASE_URL+ApiPath.STATEMENT_STATUS)
    void documentCreated(@PathVariable UUID statementId, @RequestParam ApplicationStatus status);
}
