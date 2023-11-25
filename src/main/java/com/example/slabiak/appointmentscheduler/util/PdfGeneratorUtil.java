package com.example.slabiak.appointmentscheduler.util;

import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class PdfGeneratorUtil {

    private final SpringTemplateEngine templateEngine;
    private final String baseUrl;

    public PdfGeneratorUtil(SpringTemplateEngine templateEngine, @Value("${base.url}") String baseUrl) {
        this.templateEngine = templateEngine;
        this.baseUrl = baseUrl;
    }

    public File generatePdfFromInvoice(Invoice invoice) {
        Context ctx = createContextWithInvoice(invoice);
        String processedHtml = processHtmlTemplate(ctx);

        ITextRenderer renderer = createAndInitializeRenderer(processedHtml);

        try {
            return createPdfFile(renderer);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Context createContextWithInvoice(Invoice invoice) {
        Context ctx = new Context();
        ctx.setVariable("invoice", invoice);
        return ctx;
    }

    private String processHtmlTemplate(Context ctx) {
        return templateEngine.process("email/pdf/invoice", ctx);
    }

    private ITextRenderer createAndInitializeRenderer(String processedHtml) {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processedHtml, baseUrl);
        renderer.layout();
        return renderer;
    }

    private File createPdfFile(ITextRenderer renderer) throws IOException, DocumentException {
        String fileName = UUID.randomUUID().toString();
        File outputFile = File.createTempFile(fileName, ".pdf");
        
        try (FileOutputStream os = new FileOutputStream(outputFile)) {
            renderer.createPDF(os, false);
            renderer.finishPDF();
        }

        return outputFile;
    }

}
