﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/jatools.tld" prefix="jatools" %>
<%@page import="org.apache.log4j.LogManager"%> 
<%@page import="org.apache.log4j.Logger"%>

<html>

<head>
<meta http-equiv="Content-Language" content="zh-cn">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>英孚网络报表</title>

<script type="text/javascript" src="jautils/js/menu.js"></script>
<link rel="stylesheet" type="text/css" href="jautils/js/menu.css" />

<style>
<!--
.f9 {FONT-SIZE: 9pt; LINE-HEIGHT: 16px}
.toolbutton {width: 24px; height: 24px; background-repeat: no-repeat">
-->
</style>
</head>
<body>
<% /* 报表标签开始,定义一张报表,最重要的属性是template ,相当于jrs中的file属性 ,其余属性:
	id    //是报表的变量,在下面的jsp中,可以引用这个变量
	pageBorder   // 每页的边框
	pageOffset   // 页与页之间的间距*/
	//用户权限控制
	String filePath=request.getParameter("file");//文件路径
	String fileName=null;//文件名称
	boolean aclPass=true;
    if(filePath!=null&& filePath.length()>0){
    	fileName=filePath.substring(filePath.indexOf("/")+1);//文件全名
    	fileName=fileName.substring(0,fileName.indexOf("."));//去文件后缀
    }
   
    aclPass=true;

%>
<%
long t1=System.currentTimeMillis();
%>
<jatools:report id="_report1" template='<%=request.getParameter("file")%>' pageBorder="border:1px solid blue" pageOffset="4" >
<% /*
	以下tag均需要置于报表tag之内,
	每个taq有一个id属性,对应一个jsp变量,你可以在以下的jsp中,引用变量实现特定功能
	报表容器, 用于在其中显示生成的页.
	报表容器只有一个方法, writeOut,也即在调用处显示报表.
	报表容器一般为一个可以滚动的 div对象,你可以用以下属性定制这个div的外观.
	width   // 宽度
	height  // 高度
	scroll  // 是否可以滚动,是则滚动,反之亦然
	backgroud // 背景色
	style    // 更多的css属性,可以用这个属性设置
	canDrag  // 是否可以拖动页面查看,默认为可以拖动( true);
*/
%>
<jatools:container id="_container" width='100%' height='92%' scroll='true' style='border:medium none thin threedhighlight inset;' background="#808080" />
<% /*
	缩放管理器, 用于缩小放大报表  ,注间,这个节点,必须置于容器之后.
	缩放管理器的有以下属性及方法

	属性
    fitWholePageAction;	 // 缩放到整页显示
	fitPageWidthAction;  // 缩放到页宽
	currentScale;		 // 当前缩放比例,100 表示不缩放.

	方法
	zoomToAction(string)   // 缩放到一个定值
*/
%>
<jatools:zoomer id="_zoomer" />
<% /*
	页面导航器, 用于前一页下一页等.
	页面导航器有以下属性

	属性
	currentPage;	// 当前页
	pageCount;	//总页数
    firstPageAction;  // 第一页
    previousPageAction;码 //第二页 
    nextPageAction;   // 下一页
    lastPageAction;   // 最后一页

	方法
	addPageChangedListenerAction(listener)   // 加入当前面变化临听器,此临听器实现 pageChanged()方法, 可用于显示当前页
	setCurrentPageAction(p)	// 改变当前页 
*/
%>
<jatools:navigator id="_navigator" />
<% /*
	打印机, 用于打印按钮中.
	页面导航器有以下属性

	属性
    printAction;  // 直接打印,不提示
    printPreviewAction; //打印预览
    printWithPromptAction;  // 先提示选择打印机对话框,再打印
*/
%>
<jatools:printer id="_printer" />

<% /*
	导出管理器, 用于导出按钮中.
	导出管理器有以下属性

	属性

    exportAsXlsAction;	// 导出成excel
*/
%>
<jatools:exporter id="_exporter"/>

<script>
function doZoom(z)
{
    if(z.indexOf('-')==-1)
    	<%=_zoomer.zoomToAction("z")%>;
    else if(z == '-100')
    	<%=_zoomer.fitWholePageAction%>;
    else if(z == '-200')
    	<%=_zoomer.fitPageWidthAction%>;
    zoomChooser.value = z;
}

function zoom(how)
{
  
  val = null;
  items = new Array(15,30,50,75,100,125,200,300,350,400);
  z = <%=_zoomer.currentScale%>;
  f = false;
  if(how=='+')
  {
   	z ++;
   	for(i=0;i < items.length ;i++)
   	{
   		if(items[i] > z)
   		{
   			z = items[i];
   			f = true;
   			break;
   		}
   	}
   			
  }else
  {
    z --;
    for(i=items.length-1;i>=0;i--)
   	{
   		if(items[i] < z)
   		{
   			z = items[i];
   			f = true;
   			break;
   		}
   	}
 }
 
 if(f)
 {
    doZoom(z+'');
 }
}
function PageListener()
{
	this.pageChanged = function()
	{
		_page_info.innerHTML = <%=_navigator.currentPage%> +"/"+ <%=_navigator.pageCount%>;
	};
}
listener = new PageListener();
<%= _navigator.addPageChangedListenerAction("listener")%>;
</script>
<p>&nbsp;
<button id='printbutton' style='disabled:true' onclick ='<%=_printer.printAction%>' title='打印'  class="toolbutton"  ><img src='jautils/images/print.gif' /></button>
<button id='previewbutton' style='disabled:true' onclick ='<%=_printer.printPreviewAction%>' title='打印预览...' class="toolbutton"  ><img src='jautils/images/preview.gif' /></button>
&nbsp;&nbsp;

<button onclick ='popmenu.show()' id='exportbutton' title='导出' name='exportbutton' class="toolbutton"   <% if(!aclPass){ %>style="display:none" <%} %> ><img src='jautils/images/export.gif' /></button>

&nbsp;&nbsp;
<button onclick ='<%=_navigator.firstPageAction%>' title='第一页' class="toolbutton" ><img src='jautils/images/first.gif' /></button>
<button onclick ='<%=_navigator.previousPageAction%>' title='前一页' class="toolbutton" ><img src='jautils/images/previous.gif' /></button><span id='_page_info'></span>
<button onclick ='<%= _navigator.nextPageAction%>' title='后一页' class="toolbutton"><img src='jautils/images/next.gif' /></button>
<button onclick ='<%= _navigator.lastPageAction%>' title='最后一页' class="toolbutton"  ><img src='jautils/images/last.gif' /></button>

&nbsp;&nbsp; <button onclick ='zoom("-")' title='缩小' class="toolbutton" ><img src='jautils/images/zoomout.gif' /></button>
<button onclick ='zoom("+")' title='放大' class="toolbutton" ><img src='jautils/images/zoomin.gif' /></button>
<select size="1" name="zoomChooser" onchange = 'doZoom(zoomChooser.value)'>
<option value="15">15%</option>
<option value="30">30%</option>
 <option value="50">50%</option>
 <option value="75">75%</option>
 <option value="100" selected>100%</option>
 <option value="125">125%</option>
 <option value="200">200%</option>
 <option value="300">300%</option>
 <option value="350">350%</option>
 <option value="400">400%</option>
 <option value="-100">整页</option>
 <option value="-200">页宽</option>

</select>&nbsp; <button onclick ='doZoom("100")' title='原始大小' class="toolbutton"  ><img src='jautils/images/zoom100.gif' /></button>
<button onclick ='doZoom("-100")' title='整页显示' class="toolbutton"  ><img src='jautils/images/wholepage.gif' /></button>
<button onclick ='doZoom("-200")' title='按页宽显示' class="toolbutton"  ><img src='jautils/images/zoom2w.gif' /></button>
&nbsp;&nbsp;&nbsp;&nbsp;
<button onclick="history.back()" title="返回" class="toolbutton"><img src='jautils/images/arrow_left.png' /> </button>
</p>
<% _container.writeOut(); %>
<div id=exportlist class='menulist' >
<a href="javascript:<%=_exporter.exportAsXlsAction%>">导出成 Excel </a>
<a href="javascript:<%=_exporter.exportAsPdfAction%>">导出成 Pdf </a>
</div>

<script>
popmenu =new JatoolsMenu(exportbutton,exportlist );
printbutton.style.disabled=false;
printbutton.style.disabled=false;
</script>
</jatools:report>
<%
long t2=System.currentTimeMillis();
String report_name=request.getParameter("file");
Logger logger = LogManager.getLogger("com.infoservice.framework.Channel");
logger.info("Excute action ["+report_name+"] use ["+(t2-t1)+"] ms.");
%>
</body>

</html>