package ru.vaschenko.dossier.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vaschenko.dossier.util.ApiPatch;

/** API for managing loan applications */
@Tag(name = "Dossier API", description = "API for managing loan applications")
@RequestMapping(ApiPatch.BASE_URL)
public interface DossierApi {

}
