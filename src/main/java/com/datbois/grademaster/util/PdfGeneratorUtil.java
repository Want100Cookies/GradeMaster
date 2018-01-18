package com.datbois.grademaster.util;

import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.util.Map;

@Component
public class PdfGeneratorUtil {

    @Autowired
    private TemplateEngine templateEngine;

    public void renderPdf(String templateName, Map<String, Object> parameters, OutputStream outputStream) throws DocumentException {
        Context context = new Context();

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        String html = templateEngine.process(templateName, context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream, true);
    }
}
