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

        Context ctx = new Context();
        ctx.setVariable("invoice", invoice);
        String processedHtml = templateEngine.process("email/pdf/invoice", ctx);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processedHtml, baseUrl);
        renderer.layout();

        String fileName = UUID.randomUUID().toString();
        FileOutputStream os = null;
        try {
            final File outputFile = File.createTempFile(fileName, ".pdf");
            os = new FileOutputStream(outputFile);
            renderer.createPDF(os, false);
            renderer.finishPDF();
            return outputFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
