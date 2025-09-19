package org.formation.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * This class can read, replace text and export a DOCX
 * @author comtet
 *
 */
public class DocxToPDF extends XWPFDocument {
	
	/**
	 * Source is the PATH of the model
	 * 
	 * @param source
	 * @throws IOException
	 */
	public DocxToPDF(String source) throws IOException {
    	super(new FileInputStream(new File(source)));
	}
	
	/**
	 * Remplace findText by replaceText in the document
	 * 
	 * @param findText
	 * @param replaceText
	 */
	public void replaceText(String findText, String replaceText) {
    	for (XWPFParagraph p : this.getParagraphs()) {
    	    List<XWPFRun> runs = p.getRuns();
    	    
    	    if (runs != null) {
    	        for (XWPFRun r : runs) {
    	            String text = r.getText(0);
    	            if (text != null && text.contains(findText)) {
    	                text = text.replace(findText, replaceText);
    	                r.setText(text, 0);
    	            }
    	        }
    	    }
    	}
    	
    	for (XWPFTable tbl : this.getTables())
    	   for (XWPFTableRow row : tbl.getRows())
    	      for (XWPFTableCell cell : row.getTableCells())
    	         for (XWPFParagraph p : cell.getParagraphs())
    	            for (XWPFRun r : p.getRuns()) {
    	              String text = r.getText(0);
    	              
    	              if (text.contains(findText)) {
    	                text = text.replace(findText, replaceText);
    	                r.setText(text);
    	              }
    	            }
    }
	
	/**
	 * Save the document as PDF in destination
	 * 
	 * @param destination
	 */
	public void savePDF(String destination) {
    	try {
            // 2) Prepare Pdf options
            PdfOptions options = PdfOptions.create();
 
            // 3) Convert XWPFDocument to Pdf
            OutputStream out = new FileOutputStream(new File(destination));
            PdfConverter.getInstance().convert(this, out, options);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
