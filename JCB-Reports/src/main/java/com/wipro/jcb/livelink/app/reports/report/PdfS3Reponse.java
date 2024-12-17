package com.wipro.jcb.livelink.app.reports.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class PdfS3Reponse extends PdfReportData {
	
	String fileName;
	String filePath;

}
