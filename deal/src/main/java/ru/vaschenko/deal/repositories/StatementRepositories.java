package ru.vaschenko.deal.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vaschenko.deal.models.Statement;

public interface StatementRepositories extends JpaRepository<Statement, UUID> {}
