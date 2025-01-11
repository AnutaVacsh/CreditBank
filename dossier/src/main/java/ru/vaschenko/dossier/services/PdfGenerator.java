package ru.vaschenko.dossier.services;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.vaschenko.dossier.dto.EmailMessageCredit;

/**
 * Сервис для генерации PDF документов.
 *
 * <p>Использует Thymeleaf для генерации HTML контента и iText для преобразования HTML в PDF.
 */
@Service
@RequiredArgsConstructor
public class PdfGenerator {
  private final SpringTemplateEngine engine;

  /**
   * Генерирует PDF документ на основе данных из {@link EmailMessageCredit}.
   *
   * <p>Этот метод получает данные из объекта {@link EmailMessageCredit}, используя шаблон Thymeleaf
   * credit-document.html, преобразует его в HTML, а затем в PDF, который возвращается в виде {@link
   * DataSource}.
   *
   * @param emailMessage объект {@link EmailMessageCredit}, содержащий данные для генерации
   *     документа
   * @return {@link DataSource}, содержащий сгенерированный PDF файл
   */
  public DataSource generatePdf(EmailMessageCredit emailMessage) {
    Context context = new Context();
    context.setVariables(Map.of("message", emailMessage));

    String content = engine.process("credit-document", context);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    writePdf(outputStream, content);

    return new ByteArrayDataSource(outputStream.toByteArray(), "application/pdf");
  }

  /**
   * Записывает HTML контент в PDF и сохраняет результат в {@link ByteArrayOutputStream}.
   *
   * @param outputStream поток, в который будет записан PDF
   * @param content HTML контент для преобразования в PDF
   */
  private void writePdf(ByteArrayOutputStream outputStream, String content) {
    PdfWriter writer = new PdfWriter(outputStream);
    Document document = HtmlConverter.convertToDocument(content, writer);
    document.close();
  }
}
