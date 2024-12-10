package ru.vaschenko.deal.repositories;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import ru.vaschenko.deal.models.Statement;

public interface StatementRepositories extends CrudRepository<Statement, UUID> {}
