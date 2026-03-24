package com.claim.action;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

import com.claim.birt.BirtUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ReportAction extends ActionSupport {
    private String claimNumber; 
    public String execute() {
        try {
            IReportEngine engine = BirtUtil.getEngine();
            String path = ServletActionContext.getServletContext()
                    .getRealPath("/reports/my_report.rptdesign");
            IReportRunnable design = engine.openReportDesign(path);
            IRunAndRenderTask task = engine.createRunAndRenderTask(design);
            //PASS PARAMETER
            task.setParameterValue("claimnumber", claimNumber);
            //PDF OPTION
            PDFRenderOption options = new PDFRenderOption();
            options.setOutputFormat("pdf");
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("application/pdf");
            options.setOutputStream(response.getOutputStream());
            task.setRenderOption(options);
            task.run();
            task.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	
   
}
