package com.aa.utility;

import java.io.FileOutputStream;
import java.io.File;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
public class HtmlToPdfConverter {
	 public static boolean convertHtmlToPdf(String htmlPath, String pdfPath) {
	        try {
	            // Read HTML content from input file
	            String html = new String(Files.readAllBytes(Paths.get(htmlPath)), StandardCharsets.UTF_8);

	            // Clean/fix HTML using Jsoup
	            Document doc = Jsoup.parse(html);
	            doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml); // enforce well-formed XHTML
	            doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

	            // Overwrite the original HTML file with cleaned content
	            Files.write(Paths.get(htmlPath), doc.html().getBytes(StandardCharsets.UTF_8));

	            // Convert cleaned HTML to PDF
	            try (FileOutputStream os = new FileOutputStream(pdfPath)) {
	                PdfRendererBuilder builder = new PdfRendererBuilder();
	                builder.useFastMode();
	                builder.withFile(new File(htmlPath)); // use the same input file
	                builder.toStream(os);
	                builder.run();
	            }

	            System.out.println("PDF created successfully at: " + pdfPath);
	            return true; // success

	        } catch (Exception e) {
	            e.printStackTrace();
	            return false; // failed
	        }
	    }
	 
	
}
