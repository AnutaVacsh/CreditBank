package ru.vaschenko.deal.repositories;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import ru.vaschenko.deal.models.Client;

public interface ClientRepositories extends CrudRepository<Client, UUID> {}
