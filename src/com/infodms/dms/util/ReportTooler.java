package com.infodms.dms.util;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ReportTooler
{
  String sheetName;
  HttpServletResponse response;
  WritableWorkbook wwb;
  WritableSheet sheet;
  WritableSheet sheet2;
  WritableCellFormat format;
  Label label;

  public ReportTooler()
  {
  }

  public ReportTooler(String sheetName, HttpServletResponse response)
  {
    this.sheetName = sheetName;
    this.response = response;
  }

  public void ReportInit(boolean singleSheet)
  {
    this.response.setContentType("application/x-msdownload");
    this.response.setContentType("application/msexcel");
    try
    {
      this.response.setHeader("Content-Disposition", "attachment; filename=" + new String(this.sheetName.getBytes("gb2312"), "ISO-8859-1") + ".xls");
      this.wwb = Workbook.createWorkbook(this.response.getOutputStream());
      this.sheet = this.wwb.createSheet(this.sheetName, 0);
      if (!(singleSheet)) {
        this.sheet2 = this.wwb.createSheet(this.sheetName + "详细", 1);
      }

      this.format = new WritableCellFormat();
      this.format.setAlignment(Alignment.JUSTIFY);
      this.format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GRAY_50);

      this.format.setWrap(true);
    }
    catch (UnsupportedEncodingException e1)
    {
      e1.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (WriteException e)
    {
      e.printStackTrace();
    }
    System.out.println("ReportInit:sheet:"+sheet+",sheet2:"+sheet2);
  }

  public void ReportInit() {
    ReportInit(true);
  }

  public void ReportWithMapList(List list, String topic, String[] title, String datformat, String[] fields)
    throws RowsExceededException, WriteException
  {
    setHeadFoot(topic, title);
    try {
      SimpleDateFormat sf = new SimpleDateFormat(datformat);
      int col = 0; int row = 2;
      for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();

        String content = "";
        Class c = o.getClass();
        if (!(o instanceof HashMap)) {
          System.out.println("It's not a map");
        }
        else
        {
          HashMap m = (HashMap)o;
          for (int k = 0; k < fields.length; k++) {
            Object value = m.get(fields[k]);
            content = (value == null) ? "" : value.toString().trim();

            this.label = new Label(col, row, content, this.format);
            if ((!(content.equals(""))) && (content.length() >= 10))
            {
              this.sheet.setColumnView(col, content.length());
            }
            this.sheet.addCell(this.label);
            col++;
          }
          row++;
          col = 0;
        }
      }
      this.wwb.write();

      this.wwb.close();
    } catch (Exception e) {
      e.printStackTrace(); }
  }

  public void writeToExcel(List list, String[] title, String datformat, String[] fileds) {
	  
    writeToExcel(this.sheet, list, title, datformat, fileds);
  }

  public void writeToExcel(WritableSheet sheet, List list, String[] title, String datformat, String[] fileds)
  {
	  System.out.println("writeToExcel:+"+this.sheet);
    int filelength = fileds.length;
    try
    {
      SimpleDateFormat sf = new SimpleDateFormat(datformat);
      int col = 0; int row = 2;
      for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();

        String content = "";
        Class c = o.getClass();
        for (int k = 0; k < filelength; k++)
        {
          Field fi = c.getDeclaredField(fileds[k]);
          if (fi == null) {
            content = fileds[k] + "未找到";
          }
          else {
            fi.setAccessible(true);
            Object fildobject = fi.get(o);
            if (fildobject instanceof Date)
            {
              Date date = (Date)o;
              content = (date == null) ? "" : sf.format(date);
            }
            else
            {
              content = (fildobject == null) ? "" : fildobject.toString();
              content = ((content == null) || (content.trim().length() == 0)) ? "" : content;
            }

          }

          this.label = new Label(col, row, content, this.format);
          if ((!(content.equals(""))) && (content.length() >= 10))
          {
            sheet.setColumnView(col, content.length());
          }
          sheet.addCell(this.label);
          col++;
        }
        row++;
        col = 0;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void writeFinish() {
    try {
      this.wwb.write();
      this.wwb.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ReportWithTrafficList(List list, String topic, String[] title, String datformat, String[] fileds)
    throws RowsExceededException, WriteException
  {
    setTrafficHeadFoot(true, this.sheet, topic, title);
    writeToExcel(list, title, datformat, fileds);
    writeFinish();
  }
  
  public void ReportWithTendTrafficList(List list, String topic, String[] title, String datformat, String[] fileds)
  throws RowsExceededException, WriteException
{
  setTendTrafficHeadFoot(true, this.sheet, topic, title);
  writeToExcel(list, title, datformat, fileds);
  writeFinish();
}

  public void ReportWithCompareTrafficList(List list, List list2, String topic, String[] title, String datformat, String[] fileds)
    throws RowsExceededException, WriteException
  {
    ReportInit(false);
    System.out.println("ReportWithCompareTrafficList after Init :" + this.sheet + "," + this.sheet2);
    setCompareTrafficHeadFoot(this.sheet, topic, title);
    writeToExcel(this.sheet, list, title, datformat, fileds);

    setCompareTrafficHeadFoot(this.sheet2, topic, title);
    writeToExcel(this.sheet2, list2, title, datformat, fileds);

    writeFinish();
  }

  public void ReportWithList(List list, String topic, String[] title, String datformat, String[] fileds)
    throws RowsExceededException, WriteException
  {
    setHeadFoot(topic, title);
    writeToExcel(list, title, datformat, fileds);
    writeFinish();
  }

  public void ReportWithArrayList(List list, String topic, ArrayList<String> title)
    throws RowsExceededException, WriteException
  {
    setHeadFoot(topic, title);
    try {
      int col = 0; int row = 2;
      for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();

        ArrayList line = (ArrayList)o;
        String content = "";
        for (int i = 0; i < line.size(); i++) {
          content = (line.get(i) == null) ? "" : line.get(i).toString();
          this.label = new Label(col, row, content, this.format);
          if ((!(content.equals(""))) && (content.length() >= 10))
          {
            this.sheet.setColumnView(col, content.length());
          }
          this.sheet.addCell(this.label);
          col++;
        }
        row++;
        col = 0;
      }

      this.wwb.write();

      this.wwb.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ReportSnmpOidTemplate(List list, String topic, String[] title, String datformat, String[] fileds, List list_cobox)
    throws RowsExceededException, WriteException
  {
    try
    {
      setHeadFoot(topic, title);
      Label norFormat = null;
      norFormat = new Label(4, 2, "请选择设备模板");

      WritableCellFeatures ws = new WritableCellFeatures();
      ws.setDataValidationList(list_cobox);
      norFormat.setCellFeatures(ws);
      this.sheet.addCell(norFormat);

      this.sheet.setColumnView(0, 20);
      this.sheet.setColumnView(1, 20);
      this.sheet.setColumnView(2, 20);
      this.sheet.setColumnView(3, 20);
      this.sheet.setColumnView(4, 20);
      this.sheet.setColumnView(5, 20);

      this.wwb.write();

      this.wwb.close();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void ReportWithList(List list, String topic, String[] title, String datformat, String[] fileds, String[] secTop)
    throws RowsExceededException, WriteException
  {
    setHeadFoot(topic, title, secTop);
    int sourcesize = list.size();
    int filelength = fileds.length;
    try
    {
      SimpleDateFormat sf = new SimpleDateFormat(datformat);
      int col = 0; int row = 3;
      for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();

        String content = "";
        Class c = o.getClass();
        for (int k = 0; k < filelength; k++)
        {
          Field fi = c.getDeclaredField(fileds[k]);
          if (fi == null) {
            content = fileds[k] + "未找到";
          }
          else {
            fi.setAccessible(true);
            Object fildobject = fi.get(o);
            if (fildobject instanceof Date)
            {
              Date date = (Date)o;
              content = (date == null) ? "" : sf.format(date);
            }
            else
            {
              content = (fildobject == null) ? "" : fildobject.toString();
              content = ((content == null) || (content.trim().length() == 0)) ? "" : content;
            }

          }

          this.label = new Label(col, row, content, this.format);
          if ((!(content.equals(""))) && (content.length() >= 10))
          {
            this.sheet.setColumnView(col, content.length());
          }
          this.sheet.addCell(this.label);
          col++;
        }

        row++;
        col = 0;
      }

      this.wwb.write();

      this.wwb.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (SecurityException e) {
      e.printStackTrace();
    }
    catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public WritableCellFormat getTopicformat()
  {
    WritableCellFormat font = null;
    try
    {
      WritableFont wfont = new WritableFont(WritableFont.createFont("楷书"), 15);
      font = new WritableCellFormat(wfont);
      font.setAlignment(Alignment.CENTRE);
    }
    catch (WriteException e)
    {
      e.printStackTrace();
    }
    return font;
  }

  public WritableCellFormat getSecTopicformat()
  {
    WritableCellFormat font = null;
    try
    {
      WritableFont wfont = new WritableFont(WritableFont.createFont("楷书"), 12);
      font = new WritableCellFormat(wfont);
      font.setAlignment(Alignment.LEFT);
    }
    catch (WriteException e)
    {
      e.printStackTrace();
    }
    return font;
  }

  public WritableCellFormat getTitleformat(Colour bgColor, Alignment alignment)
  {
    WritableCellFormat font = null;
    try
    {
      WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), 12);
      font = new WritableCellFormat(wfont);
      wfont.setBoldStyle(WritableFont.BOLD);
      font.setBackground(bgColor);
      font.setAlignment(alignment);
      font.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GRAY_50);

      font.setWrap(true);
    }
    catch (WriteException e)
    {
      e.printStackTrace();
    }
    return font;
  }

  public WritableCellFormat getTitleformat()
  {
    return getTitleformat(Colour.YELLOW2, Alignment.JUSTIFY);
  }

  public boolean indatacol(int i, String[] datacol, String[] colname)
  {
    int len = datacol.length;

    for (int j = 0; j < len; j++)
    {
      if (colname[i].equalsIgnoreCase(datacol[j]))
      {
        return true;
      }
    }

    return false;
  }

  public boolean indoublecol(int i, String[] doublecol, String[] colname)
  {
    int len = doublecol.length;
    for (int j = 0; j < len; j++)
    {
      if (colname[i].equalsIgnoreCase(doublecol[j]))
      {
        return true;
      }
    }

    return false;
  }

  public boolean inSumcol(int i, String[] colname, String[] sumcol)
  {
    int len = sumcol.length;
    for (int j = 0; j < len; j++)
    {
      if (colname[i].equalsIgnoreCase(sumcol[j]))
      {
        return true;
      }
    }
    return false;
  }

  public String getChar(int i)
  {
    return String.valueOf((char)(i + 64));
  }

  public void setHeadFoot(String topic, ArrayList<String> title)
    throws RowsExceededException, WriteException
  {
    ReportInit();

    int len = title.size();
    this.sheet.mergeCells(0, 0, len - 1, 0);
    WritableCellFormat font = getTopicformat();

    this.label = new Label(0, 0, topic, font);
    this.sheet.addCell(this.label);
    WritableCellFormat titlef = getTitleformat();
    for (int i = 0; i < len; i++)
    {
      this.label = new Label(i, 1, (String)title.get(i), titlef);

      this.sheet.addCell(this.label);
    }
  }

  public void setCompareTrafficHeadFoot(WritableSheet sheet, String topic, String[] title)
    throws RowsExceededException, WriteException
  {
    WritableCellFormat titlef = getTitleformat(Colour.ICE_BLUE, Alignment.JUSTIFY);
    WritableCellFormat titlef1 = getTitleformat(Colour.ICE_BLUE, Alignment.CENTRE);
    WritableCellFormat titlef2 = getTitleformat(Colour.PALE_BLUE, Alignment.CENTRE);
    Label head = null;
    for (int i = 0; i < title.length; i++) {
      if (((i >= 0) && (i < 7)) || ((i >= 23) && (i < 25)))
        head = new Label(i, 0, title[i], titlef);
      else if ((i >= 7) && (i < 15))
        head = new Label(i, 1, title[i], titlef1);
      else if ((i >= 15) && (i < 23)) {
        head = new Label(i, 1, title[i], titlef2);
      }
      sheet.addCell(head);
    }

    for (int i = 0; i < 7; i++)
    {
      sheet.mergeCells(i, 0, i, 1);
    }
    for (int i = 23; i < 25; i++)
    {
      sheet.mergeCells(i, 0, i, 1);
    }
    Label head1 = new Label(7, 0, "入方向", titlef1);
    Label head2 = new Label(15, 0, "出方向", titlef2);
    sheet.addCell(head1);
    sheet.addCell(head2);
    sheet.mergeCells(7, 0, 14, 0);
    sheet.mergeCells(15, 0, 22, 0);
  }

  public void setTendTrafficHeadFoot(boolean initOk, WritableSheet sheet, String topic, String[] title) throws RowsExceededException, WriteException
  {
	System.out.println("setTendTrafficHeadFoot..........."+title.length+","+sheet);
    if (!(initOk)) {
      ReportInit();
    }
    WritableCellFormat titlef = getTitleformat(Colour.ICE_BLUE, Alignment.JUSTIFY);
    WritableCellFormat titlef1 = getTitleformat(Colour.ICE_BLUE, Alignment.CENTRE);
    WritableCellFormat titlef2 = getTitleformat(Colour.PALE_BLUE, Alignment.CENTRE);
    
    for(int i=0;i<title.length;i++){
    	String headTitle=title[i];
		Label head=null;
		/*
		if( (i>=0 && i<5) || (i>=15 &&i<17) ){
			head=new Label(i,0,headTitle,titlef2);
		}else if(i>=5 && i <10){
			head=new Label(i,1,headTitle,titlef);
		}else if(i>=10 && i <15){
			head=new Label(i,1,headTitle,titlef2);
		}*/
		if( (i>=0 && i<7) || (i>=9 &&i<11) ){
			head=new Label(i,0,headTitle,titlef2);
		}else if(i>=7 && i <8){
			head=new Label(i,1,headTitle,titlef);
		}else if(i>=8 && i <9){
			head=new Label(i,1,headTitle,titlef2);
		}
		//System.out.println(i+",head:"+head+","+headTitle);
		sheet.addCell(head);
	}
    
  //合并单元格，参数格式（开始列，开始行，结束列，结束行）
	
    for(int i=0; i<7 ;i++){
		sheet.mergeCells(i, 0, i, 1);
	}
	for(int i=9;i<11;i++){
		sheet.mergeCells(i, 0, i, 1);
	}
	
	Label head=new Label(7,0,"入方向",titlef);
	Label head2=new Label(8,0,"出方向",titlef2);
	sheet.addCell(head);
	sheet.addCell(head2);
	sheet.mergeCells(7, 0, 7, 0);
	sheet.mergeCells(8, 0, 8, 0);
  }
  public void setTrafficHeadFoot(boolean initOk, WritableSheet sheet, String topic, String[] title) throws RowsExceededException, WriteException
  {
	System.out.println("setTrafficHeadFoot..........."+title.length+","+sheet);
    if (!(initOk)) {
      ReportInit();
    }
    WritableCellFormat titlef = getTitleformat(Colour.ICE_BLUE, Alignment.JUSTIFY);
    WritableCellFormat titlef1 = getTitleformat(Colour.ICE_BLUE, Alignment.CENTRE);
    WritableCellFormat titlef2 = getTitleformat(Colour.PALE_BLUE, Alignment.CENTRE);
    
    for(int i=0;i<title.length;i++){
    	String headTitle=title[i];
		Label head=null;
		/*
		if( (i>=0 && i<5) || (i>=15 &&i<17) ){
			head=new Label(i,0,headTitle,titlef2);
		}else if(i>=5 && i <10){
			head=new Label(i,1,headTitle,titlef);
		}else if(i>=10 && i <15){
			head=new Label(i,1,headTitle,titlef2);
		}*/
		if( (i>=0 && i<8) || (i>=18 &&i<20) ){
			head=new Label(i,0,headTitle,titlef2);
		}else if(i>=8 && i <13){
			head=new Label(i,1,headTitle,titlef);
		}else if(i>=13 && i <18){
			head=new Label(i,1,headTitle,titlef2);
		}
		//System.out.println(i+",head:"+head+","+headTitle);
		sheet.addCell(head);
	}
    
  //合并单元格，参数格式（开始列，开始行，结束列，结束行）
	
    for(int i=0; i<8 ;i++){
		sheet.mergeCells(i, 0, i, 1);
	}
	for(int i=18;i<20;i++){
		sheet.mergeCells(i, 0, i, 1);
	}
	
	Label head=new Label(8,0,"入方向",titlef);
	Label head2=new Label(13,0,"出方向",titlef2);
	sheet.addCell(head);
	sheet.addCell(head2);
	sheet.mergeCells(8, 0, 12, 0);
	sheet.mergeCells(13, 0, 17, 0);
  }

  public void setHeadFoot(String topic, String[] title)
    throws RowsExceededException, WriteException
  {
    ReportInit();

    int len = title.length;
    this.sheet.mergeCells(0, 0, len - 1, 0);
    WritableCellFormat font = getTopicformat();

    this.label = new Label(0, 0, topic, font);
    this.sheet.addCell(this.label);
    WritableCellFormat titlef = getTitleformat();
    for (int i = 0; i < len; i++)
    {
      this.label = new Label(i, 1, title[i], titlef);

      this.sheet.addCell(this.label);
    }
  }

  public void setHeadFoot(String topic, String[] title, String[] secTop)
    throws RowsExceededException, WriteException
  {
    ReportInit();

    int len = title.length;
    this.sheet.mergeCells(0, 0, len - 1, 0);
    WritableCellFormat font = getTopicformat();

    this.label = new Label(0, 0, topic, font);
    this.sheet.addCell(this.label);
    WritableCellFormat secfont = getSecTopicformat();
    WritableCellFormat titlef = getTitleformat();
    boolean isaddsec = false;
    if (secTop != null)
    {
      int lent = secTop.length;
      isaddsec = lent > 0;
      for (int i = 0; i < lent; i++)
      {
        this.label = new Label(i, 1, secTop[i], secfont);

        this.sheet.addCell(this.label);
      }
    }

    int r = (isaddsec) ? 2 : 1;
    for (int i = 0; i < len; i++)
    {
      this.label = new Label(i, r, title[i], titlef);

      this.sheet.addCell(this.label);
    }
  }

  public String[] getDatecol(ResultSet rs)
    throws SQLException
  {
    ResultSetMetaData mt = rs.getMetaData();
    int colcount = mt.getColumnCount();
    String[] datecol = new String[colcount];

    int j = 0;
    for (int i = 1; i <= colcount; i++)
    {
      if (mt.getColumnTypeName(i).equalsIgnoreCase("DATE"))
      {
        datecol[j] = mt.getColumnName(i);
      }
      j++;
    }

    return datecol;
  }

  public String[] getDoubleCol(ResultSet rs)
    throws SQLException
  {
    ResultSetMetaData mt = rs.getMetaData();
    int colcount = mt.getColumnCount();
    String[] doucol = new String[colcount];
    int j = 0;
    for (int i = 1; i <= colcount; ++i)
    {
      String coltype = mt.getColumnTypeName(i);
      if ((coltype.equalsIgnoreCase("DOUBLE")) || (coltype.equalsIgnoreCase("FLOAT")))
      {
        doucol[j] = mt.getColumnName(i);
      }
      ++j;
    }
    return doucol;
  }

  public WritableCellFormat numberFormat(String format)
  {
    NumberFormat nf = new NumberFormat(format);
    WritableCellFormat formats = new WritableCellFormat(nf);
    try
    {
      formats.setBorder(Border.ALL, BorderLineStyle.THIN);
      formats.setAlignment(Alignment.CENTRE);
    }
    catch (WriteException e)
    {
      e.printStackTrace();
    }

    return formats;
  }

  public WritableCellFormat getCommFormat()
  {
    WritableCellFormat format = new WritableCellFormat();
    try
    {
      format.setBorder(Border.ALL, BorderLineStyle.THIN);
      format.setAlignment(Alignment.CENTRE);
    }
    catch (WriteException e)
    {
      e.printStackTrace();
    }

    return format;
  }

  public String getSheetName() {
    return this.sheetName;
  }

  public void setSheetName(String sheetName) {
    this.sheetName = sheetName;
  }

  public HttpServletResponse getResponse() {
    return this.response;
  }

  public void setResponse(HttpServletResponse response) {
    this.response = response;
  }
}