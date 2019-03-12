package com.example.slabiak.appointmentscheduler.util;

import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Component
public class PdfGeneratorUtil {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public File generatePdfFromInvoice(Invoice invoice) {

        Context ctx = new Context();
        ctx.setVariable("invoice",invoice);
        String processedHtml = templateEngine.process("email/pdf/invoice", ctx);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processedHtml,"http://localhost:8080");
        renderer.layout();

        String fileName = UUID.randomUUID().toString();
        FileOutputStream os = null;
        try {
            final File outputFile = File.createTempFile(fileName, ".pdf");
            os = new FileOutputStream(outputFile);
            renderer.createPDF(os, false);
            renderer.finishPDF();
            System.out.println("PDF created successfully");
            return outputFile;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if(os !=null){
                try{
                    os.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
