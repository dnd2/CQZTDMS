<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>金牛星维修量统计报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
   		genLocSel2('txt1','','');//初始化省份
	}
	function clearInput() {
	 $('dealerCode').value = '';
	}

    function genCkb(id,type) {
    var str = "";
	     for(var i=0;i<codeData.length;i++){
			if(codeData[i].type == type){			   
				str += "<input type='checkbox' " + " value='" + codeData[i].codeId + "' name = '"+id+"' >" + codeData[i].codeDesc + "</input>";
			}
		}
	document.write(str);	  
    }
    
   function genLocSel2(proId,cityId,areaId){ //生成省份，城市，县
	_genPro2(proId, arguments[3]);	
 }
function _genPro2(proId){ //生成省份,删除默认”请选择“
	var opt;
	for(var i=0;i<regionData.length;i++){
		if(regionData[i].regionType == REGION_PROVINCE_TYPE){
			opt = addOption($(proId), regionData[i].regionName, regionData[i].regionName);
			if(arguments[1] && arguments[1] == regionData[i].regionCode) opt.selected = "selected";
		}
	}
}   
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;金牛星维修量统计报表</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
          <tr>
            <td align="right" >零件授权：</td>
            <td align="left">
            	<input  id="partYes" name="partYes" value="10041001" type="checkbox" checked="checked"/>是
            	<input  id="partNo" name="partNo" value="10041002" type="checkbox" />否
            	<input  id="NoNeed" name="NoNeed" value="NoNeed" type="checkbox" checked="checked"/>不需要授权
            </td>
            <td>&nbsp;</td>
            <td align="right" >&nbsp;</td>
            <td align="right" nowrap>起止时间： </td>
            <td align="left">
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
            <!-- <td align="right" nowrap>生产地基：</td>
             <td>
				<script type="text/javascript">
					genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",false,"short_sel",'',"false",'${yieldly}');
				</script>
            </td>  -->   
          </tr>
          <tr>
            <td align="right" colspan="5" >&nbsp;</td>
            <td align="left" nowrap>
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载"  onclick="exportExcel()" />
            </td>
          </tr>
  </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/report/jcafterservicereport/TaurusAmountReport/queryTaurusAmountReportReport.json";
				
	var title = null;

	var columns = [
				{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center'},
				{header: "维修车辆数", dataIndex: 'AMOUNT', align:'center'}
					
		      ];
		      
//设置超链接  begin      
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/TaurusAmountReport/getTaurusAmountReportReportExcel.do";
		fm.submit();
	}

  function showMonthFirstDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function showMonthLastDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthNextFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var MonthLastDay = new Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }

  $('beginTime').value=showMonthFirstDay();
  $('endTime').value=showMonthLastDay();
  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>