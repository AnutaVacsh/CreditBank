package ru.vaschenko.deal.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.deal.models.Credit;
import ru.vaschenko.deal.repositories.CreditRepositories;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private CreditRepositories creditRepositories;

    @InjectMocks
    private CreditService creditService;

    private Credit credit;

    @BeforeEach
    void setUp() {
        Credit credit = new Credit()
                .setAmount(new BigDecimal(1000))
                .setRate(new BigDecimal("5.0"))
                .setTerm(12);
    }

    @Test
    void testSafeCredit() {
        when(creditRepositories.save(credit)).thenReturn(credit);

        Credit savedCredit = creditService.safeCredit(credit);

        assertNotNull(savedCredit);
        assertEquals(new BigDecimal(1000), savedCredit.getAmount());
        assertEquals(new BigDecimal("5.0"), savedCredit.getRate());
        assertEquals(12, savedCredit.getTerm());

        verify(creditRepositories).save(credit);
    }
}
