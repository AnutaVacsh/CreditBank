package ru.vaschenko.deal.repositories;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import ru.vaschenko.deal.models.Credit;

public interface CreditRepositories extends CrudRepository<Credit, UUID> {}
