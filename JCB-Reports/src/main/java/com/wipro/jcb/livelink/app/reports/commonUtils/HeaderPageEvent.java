package com.wipro.jcb.livelink.app.reports.commonUtils;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class HeaderPageEvent extends PdfPageEventHelper{
	
	private PdfTemplate t;
	private Image total;
	private String iconLocation;
	private String fromDate;
	private String toDate;
	private String model;
	
	public HeaderPageEvent(String iconLocation,String fromDate,String toDate,String model) {
		this.iconLocation = iconLocation;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.model=model;
	}


}
