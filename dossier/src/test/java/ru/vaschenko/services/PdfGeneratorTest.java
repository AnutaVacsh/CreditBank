package ru.vaschenko.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.vaschenko.dossier.dto.EmailMessageCredit;
import ru.vaschenko.dossier.services.PdfGenerator;

@ExtendWith(MockitoExtension.class)
class PdfGeneratorTest {

  @Mock private SpringTemplateEngine engine;

  @InjectMocks private PdfGenerator pdfGenerator;

  @Test
  void generatePdf() throws Exception {
    // Given
    EasyRandomParameters parameters =
        new EasyRandomParameters().stringLengthRange(5, 20).randomize(UUID.class, UUID::randomUUID);
    EasyRandom easyRandom = new EasyRandom(parameters);

    EmailMessageCredit emailMessageCredit = easyRandom.nextObject(EmailMessageCredit.class);

    String mockContent = "<html><body><h1>Test PDF</h1></body></html>";
    when(engine.process(any(String.class), any(Context.class))).thenReturn(mockContent);

    // When
    var dataSource = pdfGenerator.generatePdf(emailMessageCredit);

    // Then
    assertNotNull(dataSource);
    assertNotNull(dataSource.getInputStream());
  }
}
