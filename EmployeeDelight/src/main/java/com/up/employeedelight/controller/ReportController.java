package com.up.employeedelight.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import thymeleaf.spring.support.Layout;

import com.up.employeedelight.service.ReportManager;

@Layout(value = "layouts/main_layout.html")
@Controller
@RequestMapping("/reports")
public class ReportController {
	private static final int BUFFER_SIZE = 1024;

	@Value("${reports.path}")
	public static String reportFolderPath;

	@Autowired
	ServletContext context;

	@Autowired
	ReportManager reportGenerator;

	public ReportController() {
		/* reportFolderPath = context.getRealPath("/reports"); */
	}

	@RequestMapping(value = "/show")
	public String showReports() {
		System.out.println(reportFolderPath);

		return "report-main-content.html";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showReportPage() {
		return "report-main-content.html";
	}

	@RequestMapping(value = "generate", method = RequestMethod.POST)
	public void generateReport(@RequestParam Integer budget, HttpServletResponse response) {
		String fileName = reportGenerator.generateReport(budget);

		File reportFile = new File(reportFolderPath + "/" + fileName);
		try {
			FileInputStream inputStream = new FileInputStream(reportFile);

			response.setContentLength((int) reportFile.length());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			OutputStream outStream = response.getOutputStream();
			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.close();

			// return "report-main-content.html";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setReportFolderPath(String reportFolderPath) {
		ReportController.reportFolderPath = reportFolderPath;
	}

}
