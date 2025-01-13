package ru.vaschenko.deal.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vaschenko.deal.models.Client;

public interface ClientRepositories extends JpaRepository<Client, UUID> {}
