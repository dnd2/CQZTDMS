function $(element)
{
return document.getElementById(element);
}
function JatoolsNavigator()
{
this.for_report="";
this.currentPage = -1;
this.pageCount =-1;
this.container = null;
this.listeners = new Array();
this.setCurrentPage=function(p)
{
if(p>0 && p<=this.pageCount && p!= this.currentPage)
{
this.currentPage=p;
if(this.listeners)
{
for(i=0;i< this.listeners.length;i++)
{
listener = this.listeners[i];
listener.pageChanged();
}
}
}
};
this.previousPage=function()
{
this.setCurrentPage(this.currentPage-1);
}
;
this.nextPage=function()
{
this.setCurrentPage(this.currentPage+1);
};
this.lastPage	=	function()
{
this.setCurrentPage(this.pageCount );
};
this.firstPage = function()
{
this.setCurrentPage(1);
};
this.addListener = function(listener)
{
this.listeners[this.listeners.length] = listener;
listener.nav = this;
};
}
function GridNavigatorListener()
{
this.nav = null;
this.activePage = null;
this.activePageBorder = null;
this.pageChanged=function()
{
if(this.nav.container)
{
if(this.activePage )
{
this.activePage.style.border='1px solid blue';
}
this.activePage=$(this.nav.for_report+"_page_"+this.nav.currentPage);
if(this.activePage)
{
this.activePageBorder = '1px solid blue';
this.activePage.style.border='5px solid red';
scrollToVisible(this.activePage);
this.activePage.parentElement.activePage= this.activePage;
}
}
};
}
function CardNavigatorListener()
{
this.nav = null;
this.maxIndexPages=20;
this.pageNumber = -1;
this.activePage = null;
this.buildPagesIndexHtml=function()
{
from=1;
to=nav.pageCount;
if(nav.pageCount>this.maxIndexPages)
{
half=this.maxIndexPages/2;
if(nav.currentPage-half<1)
{
from=1;
to=Math.min(this.maxIndexPages,nav.pageCount);
}
else if(nav.currentPage+half>nav.pageCount)
{
to=nav.pageCount;
from=Math.max(nav.currentPage-half,1);
}
else
{
from=nav.currentPage-half;
to=nav.currentPage+half;
}
}
text="";
for(i=from;i<=to;i++)
{
this.pageNumber=i;
text+=this.pageHtml();
}
return text;
};
this.pageChanged=function()
{
if(nav.container&&this.refresh)
{
if(this.activePage)
{
this.activePage.style.display='none';
}
this.activePage=$(nav.for_report+"_page_"+this.currentPage);
if(this.activePage)
{
this.activePage.style.display='block';
}
html=this.refresh();
$(nav.container).innerHTML=html;
c=1;
next=$(nav.container+"__"+c);
while(next)
{
next.innerHTML=html;
c++;
next=$(nav.container+"__"+c);
}
}
};
}
function scrollToVisible(e)
{
var z = e.parentElement.currentStyle.zoom;
if(z && z!='normal')
{
z = parseInt(z.substr(0,z.length -1)) / 100.0;
}else
z = 1.0;
var m = e.currentStyle.margin;
if(m)
{
m = parseInt(m);
}else
m = 0;
e.parentElement.parentElement.scrollTop = (e.offsetTop-m) * z;
e.parentElement.parentElement.scrollLeft =(e.offsetLeft -m) * z;
}
function JatoolsZoomer(c)
{
this.container=c;
this.current=100;
this.step=10;
this.max=500;
this.min=10;
this.zoomIn=function()
{
this.zoomTo(this.current+this.step);
}
;
this.zoomOut=function()
{
this.zoomTo(this.current-this.step);
}
;
this.zoomNo=function()
{
this.zoomTo(100);
}
;
this.fitPageWidth = function()
{
if(this.container && $(this.container).activePage)
{
e = $(this.container).activePage;
var z = e.parentElement.currentStyle.zoom;
if(z)
{
z = parseInt(z.substr(0,z.length -1)) / 100.0;
}else
z = 1.0;
var m = e.currentStyle.margin;
if(m)
{
m = parseInt(m);
}else
m = 0;
this.zoomTo(e.parentElement.parentElement.clientWidth * 100/ (e.offsetWidth + 3 *m));
scrollToVisible(e);
}
};
this.fitWholePage = function()
{
if(this.container && $(this.container).activePage)
{
e = $(this.container).activePage;
var z = e.parentElement.currentStyle.zoom;
if(z)
{
z = parseInt(z.substr(0,z.length -1)) / 100.0;
}else
z = 1.0;
var m = e.currentStyle.margin;
if(m)
{
m = parseInt(m);
}else
m = 0;
zx = e.parentElement.parentElement.clientWidth * 100/ (e.offsetWidth + 3 *m);
zy = e.parentElement.parentElement.clientHeight * 100/ (e.offsetHeight + 3 *m);
if(zx < zy)
this.zoomTo(zx);
else
this.zoomTo(zy);
scrollToVisible(e);
}
};
this.zoomTo=function(z)
{
if(this.container&&z>=this.min&&z<=this.max)
{
$(this.container).style.zoom=z+"%";
this.current=z;
c=1;
next=$(this.container+"__"+c);
while(next)
{
next.style.zoom=z+"%";
c++;
next=$(this.container+"__"+c);
}
}
}
}
;
function JatoolsPrinter()
{
this.for_report='';
this.setProperties=function(printedReport)
{
if(this.print_settings)
{
if(!printedReport.print_settings)
printedReport.print_settings=new Object();
if(this.print_settings.printer)
printedReport.print_settings.printer=this.print_settings.printer;
if(this.print_settings.pageWidth)
printedReport.print_settings.pageWidth=this.print_settings.pageWidth;
if(this.print_settings.pageHeight)
printedReport.print_settings.pageHeight=this.print_settings.pageHeight;
if(this.print_settings.orientation)
printedReport.print_settings.orientation=this.print_settings.orientation;
}
if(this.print_settings_id)
printedReport.print_settings_id=this.print_settings_id;
if(this.documents)
printedReport.documents=this.documents;
if(this.load_print_settings_if_exists)
printedReport.load_print_settings_if_exists=this.load_print_settings_if_exists;
if(this.save_print_settings_after_print)
printedReport.save_print_settings_after_print=this.save_print_settings_after_print;
}
;
this.print=function(prompt)
{
if(!this.checkPrinterLoaded())
return;
printedReport=eval(this.for_report);
this.setProperties(printedReport);
$("_jatoolsPrinter").print(printedReport,prompt);
}
;
this.printPreview=function()
{
if(!this.checkPrinterLoaded())
return;
printedReport=eval(this.for_report);
this.setProperties(printedReport);
$("_jatoolsPrinter").printPreview(printedReport);
}
;
this.checkPrinterLoaded=function()
{
if(!this.my_printer_d)
{
loadPrinter();
}
if(typeof($("_jatoolsPrinter").page_div_prefix)=='undefined')
{
alert("您的系统还没有安装打印程序,请在另一个弹出的安装对话框中,选择'安装'.\n如果没有提示安装对话框,请按以下步骤设置ie后,重新打开本页.\n\n工具-> internet 选项->安全->自定义级别,设置 ‘下载未签名的 ActiveX ’为'启用'状态.  ");
return false;
}
else
return true;
}
function loadPrinter()
{
this.my_printer_d=document.createElement('div')
this.my_printer_d.style.display='none'
document.body.appendChild(this.my_printer_d)
var object = '<OBJECT ID="_jatoolsPrinter" CLASSID="CLSID:B43D3361-D975-4BE2-87FE-057188254255"';
object += 'codebase="jatoolsPrinter/jatoolsP.cab#version=1,1,6,5"></OBJECT>';
this.my_printer_d.innerHTML = object;
}
};
function JatoolsExporter()
{
this.report_server='server';
this.exportAs=function(as)
{
cachedId=eval(this.for_report).cached_id;
window.location=this.report_server+"?do_export=1&call_cache="+cachedId+"&as="+as;
}
}
