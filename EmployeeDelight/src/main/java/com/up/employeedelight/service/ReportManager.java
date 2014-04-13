package com.up.employeedelight.service;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.up.employeedelight.controller.ReportController;
import com.up.employeedelight.domain.Report;
import com.up.employeedelight.domain.Vote;
import com.up.employeedelight.repository.ReportRepository;
import com.up.employeedelight.repository.VoteRepository;

@Component
public class ReportManager {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ReportRepository reportRepo;

	@Autowired
	private VoteRepository voteRepo;

	public String generateReport(Integer budget) {
		Report report = createReport(budget);
		reportRepo.save(report);

		// update votes
		// voteRepo.setAllSubmitted();
		Iterable<Vote> votes = voteRepo.findAll();
		for (Vote v : votes) {
			v.setIsSubmitted(true);
		}
		voteRepo.save(votes);
		return report.getFilePath();
	}

	private Report createReport(Integer totalBudget) {
		JasperReportBuilder report = DynamicReports.report();
		// create columns
		TextColumnBuilder<Integer> rowNumberColumn = col.reportRowNumberColumn("No.").setFixedColumns(2)
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
		TextColumnBuilder<Integer> pointsCol = col.column("Points", "Points", DataTypes.integerType());
		TextColumnBuilder<String> productCol = col.column("Product", "Product", DataTypes.stringType());
		TextColumnBuilder<String> categoryCol = col.column("Category", "Category", DataTypes.stringType());
		TextColumnBuilder<Integer> costCol = col.column("Cost", "Cost", DataTypes.integerType());
		TextColumnBuilder<BigDecimal> pointPercCol = col.column("Points %", "Points %", DataTypes.bigDecimalType());
		TextColumnBuilder<BigDecimal> budgetCol = pointPercCol.multiply(totalBudget / 100).setTitle("Allocated budget");
		// col.column("Budget", DataTypes.floatType());

		report.columns(rowNumberColumn, categoryCol, productCol, costCol, pointsCol, pointPercCol, budgetCol);

		report.title(cmp.text("Employee delight - voting report"));
		report.pageFooter(cmp.pageXofY());

		// Style
		StyleBuilder boldStyle = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle);
		StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle).setBorder(stl.pen1Point())
				.setBackgroundColor(Color.LIGHT_GRAY);
		report.setColumnTitleStyle(columnTitleStyle).highlightDetailEvenRows()
				.pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle));

		/*
		 * report.subtotalsAtSummary(sbt.sum(pointsCol), sbt.sum(budgetCol),
		 * sbt.sum(pointPercCol));
		 */
		/*
		 * report.subtotalsAtFirstGroupFooter(sbt.sum(pointsCol),
		 * sbt.sum(budgetCol), sbt.sum(pointPercCol));
		 */

		BarChartBuilder itemChart = cht.barChart().setTitle("Votes by product category").setCategory(categoryCol)
				.addSerie(cht.serie(pointsCol));

		/*
		 * BarChartBuilder itemChart2 = cht.barChart()
		 * .setTitle("Votes by product") .setCategory(itemColumn)
		 * .setUseSeriesAsCategory(true) .addSerie( cht.serie(unitPriceColumn),
		 * cht.serie(priceColumn));
		 */
		Report myReport = null;
		try {
			Date currentDate = new Date();
			String name = createReportName(currentDate);
			String xlsFileName = name + ".xls";
			myReport = new Report(currentDate, "", name + ".xls", name);
			report.setDataSource(
					"SELECT pc.name AS 'Category', p.name AS 'Product', p.cost AS 'Cost', vp.points AS 'Points' , (100 * vp.points/s.total) AS 'Points %'  FROM product_categories pc ,	products p ,	(SELECT v.productId, sum(points) AS 'points' FROM votes v WHERE v.isSubmitted=0 GROUP BY v.productId) vp, (SELECT sum(points)   AS 'total' FROM  votes WHERE isSubmitted=0) s WHERE	p.category = pc.id and	p.id = vp.productId;",
					getConnection());

			// save the reports
			File xlsFile = new File(ReportController.reportFolderPath + "/" + xlsFileName);
			try {
				xlsFile.createNewFile();
				report.toXls(new FileOutputStream(xlsFile));

				/*
				 * report.groupBy(categoryCol); File pdfFile = new
				 * File(ReportController.reportFolderPath + "/" + "report" +
				 * ".pdf"); pdfFile.createNewFile();
				 * report.summary(itemChart).toPdf(new
				 * FileOutputStream(pdfFile));
				 */
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DRException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return myReport;
	}

	private String createReportName(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY_hh-mm");
		return "report" + sdf.format(date);
	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();

	}
}
