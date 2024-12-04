package ru.vaschenko.deal.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vaschenko.deal.models.Credit;

public interface CreditRepositories extends JpaRepository<Credit, UUID> {}
