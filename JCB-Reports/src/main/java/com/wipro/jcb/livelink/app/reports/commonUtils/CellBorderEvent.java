package com.wipro.jcb.livelink.app.reports.commonUtils;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

public class CellBorderEvent implements PdfPCellEvent {

	@Override
	public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvases) {
		PdfContentByte cb = canvases[PdfPTable.BACKGROUNDCANVAS];
		cb.roundRectangle(rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3, rect.getHeight() - 3, 3);
		cb.stroke();
	}

}
