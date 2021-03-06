/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.ui.common.component.AbstractExcelResource;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.CurrencyCellStyle;
import net.rrm.ehour.ui.common.report.excel.StaticCellStyle;
import net.rrm.ehour.ui.report.TreeReportElement;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Abstract aggregate excel report
 **/
public abstract class AbstractExcelReport extends AbstractExcelResource
{
	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger.getLogger(AbstractExcelReport.class);

	private ReportConfig	reportConfig;

	public AbstractExcelReport(ReportConfig reportConfig)
	{
		this.reportConfig = reportConfig;
	}

	@Override
	public byte[] getExcelData(Report report) throws IOException
	{
		logger.trace("Creating excel report");
		HSSFWorkbook workbook = createWorkbook(report);

		return PoiUtil.getWorkbookAsBytes(workbook);
	}

	/**
	 * Create the workbook
	 */
	protected HSSFWorkbook createWorkbook(Report treeReport)
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet 	sheet = wb.createSheet(getExcelReportName().getObject());
		int			rowNumber = 0;
		short		column;

		for (column = 0; column < 4; column++)
		{
			sheet.setColumnWidth(column, 5000);
		}

		for (; column < 7; column++)
		{
			sheet.setColumnWidth(column, 3000);
		}

		rowNumber = createHeaders(rowNumber, sheet, treeReport, wb);

		rowNumber = addColumnHeaders(rowNumber, sheet, wb);

		fillReportSheet(treeReport, sheet, rowNumber, wb);

		return wb;
	}

	protected abstract IModel<String> getExcelReportName();

	protected abstract IModel<String> getHeaderReportName();

	private int addColumnHeaders(int rowNumber, HSSFSheet sheet, HSSFWorkbook workbook)
	{
		HSSFRow		row;
		int			cellNumber = 0;
		IModel<String> headerModel;

		row = sheet.createRow(rowNumber++);

		for (ReportColumn reportColumn : reportConfig.getReportColumns())
		{
			if (reportColumn.isVisible())
			{
				headerModel = new ResourceModel(reportColumn.getColumnHeaderResourceKey());

				CellFactory.createCell(row, cellNumber++, headerModel, workbook, StaticCellStyle.HEADER);
			}
		}

		return rowNumber;
	}

	@SuppressWarnings("unchecked")
	protected void fillReportSheet(Report reportData, HSSFSheet sheet, int rowNumber, HSSFWorkbook workbook)
	{
		List<TreeReportElement> matrix = (List<TreeReportElement>)reportData.getReportData().getReportElements();
		ReportColumn[]	columnHeaders = reportConfig.getReportColumns();
		HSSFRow				row;

		for (TreeReportElement element : matrix)
		{
			row = sheet.createRow(rowNumber++);

			addColumns(workbook, columnHeaders, row, element);
		}
	}

	private void addColumns(HSSFWorkbook workbook, ReportColumn[] columnHeaders, HSSFRow row, TreeReportElement element)
	{
		int	i = 0;
		int cellNumber = 0;

        CurrencyCellStyle currencyCellStyle = new CurrencyCellStyle();

        // add cells for a row
		for (Serializable cellValue : element.getRow())
		{
			if (columnHeaders[i].isVisible())
			{
				if (cellValue != null)
				{
                    switch (columnHeaders[i].getColumnType())
                    {
                        case HOUR:
                            CellFactory.createCell(row, cellNumber++, cellValue, workbook, StaticCellStyle.DIGIT);
                            break;
                        case TURNOVER:
                        case RATE:
                            CellFactory.createCell(row, cellNumber++, cellValue, workbook, currencyCellStyle);
                            break;
                        case DATE:
                            CellFactory.createCell(row, cellNumber++, cellValue, workbook, StaticCellStyle.DATE);
                            break;
                        default:
                            CellFactory.createCell(row, cellNumber++, cellValue, workbook, StaticCellStyle.NORMAL);
                            break;
                    }
				}
				else
				{
					cellNumber++;
				}
			}

			i++;
		}
	}

	@Override
	protected String getFilename()
	{
		return getExcelReportName().getObject().toLowerCase().replace(' ', '_') + ".xls";
	}


	protected int createHeaders(int rowNumber, HSSFSheet sheet, Report report, HSSFWorkbook workbook)
	{
		HSSFRow		row;

		row = sheet.createRow(rowNumber++);
		CellFactory.createCell(row, 0, getHeaderReportName(), workbook, StaticCellStyle.BOLD);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

		row = sheet.createRow(rowNumber++);
		CellFactory.createCell(row, 0, new ResourceModel("report.dateStart"), workbook, StaticCellStyle.BOLD);

		if (report.getReportRange() == null ||
				report.getReportRange().getDateStart() == null)
		{
			CellFactory.createCell(row, 1, "--", workbook, StaticCellStyle.BOLD);
		}
		else
		{
			CellFactory.createCell(row, 1, report.getReportCriteria().getReportRange().getDateStart(), workbook, StaticCellStyle.BOLD, StaticCellStyle.DATE);
		}

		CellFactory.createCell(row, 3, new ResourceModel("report.dateEnd"), workbook, StaticCellStyle.BOLD);

		if (report.getReportRange() == null || report.getReportRange().getDateEnd() == null)
		{
			CellFactory.createCell(row, 4, "--", workbook, StaticCellStyle.BOLD);
		}
		else
		{
			CellFactory.createCell(row, 4, report.getReportCriteria().getReportRange().getDateEnd(), workbook, StaticCellStyle.BOLD, StaticCellStyle.DATE);
		}

		rowNumber++;

		return rowNumber;
	}
}
