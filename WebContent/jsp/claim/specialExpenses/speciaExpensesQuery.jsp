<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊费用提报</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;
当前位置： 售后服务管理&gt;特殊费用管理&gt;特殊费用提报&nbsp;&nbsp;<font color="red">(新增费用时,请先选择费用类型)</font></div>
  
  <form method="post" name="fm" id="fm">
  
  <!-- 车系列表 -->
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
  
   <!-- 查询条件 begin -->
    <table class="table_query" >
          <tr>
            <td width="7%" align="right" nowrap>费用编号：</td>
            <td colspan="6">
            	<input name="feeNo" id="feeNo" type="text" size="18"  class="middle_txt" value="" >
            </td>
			<td align="right">生产基地：</td>
			<td align="left">
				 <select style="width: 152px;" name="yieldly" id="yieldly">
				 <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="Area" items="${Area}" >
 				  <option value="${Area.areaId}" >
    				<c:out value="${Area.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select>
			</td>
          </tr>                  
          <tr>
            <td align="right" nowrap="nowrap" >上报时间：</td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            	<input class="short_txt" id="t1" name="beginTime" datatype="1,is_date,10"
           		 maxlength="10" group="t1,t2"/>
        	 	<input class="time_ico" value=" " onclick="showcalendar(event, 't1', false);" type="button"/>
          		 至
	        	 <input class="short_txt" id="t2" name="endTime" datatype="1,is_date,10"
	           	  maxlength="10" group="t1,t2"/>
	        	 <input class="time_ico" value=" " onclick="showcalendar(event, 't2', false);" type="button"/>
            	</div>
            </td>
            <td align="right" nowrap="nowrap" >费用类型：</td>
            <td colspan="6" nowrap="nowrap">
              	<script type="text/javascript">
 					 genSelBoxExp("feeType",<%=Constant.FEE_TYPE%>,"",true,"short_sel","","false",'');
			  	</script>
            </td>
          </tr>
          <tr>
            
            <td align="center" colspan="10" nowrap>
            	<input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);">
				<input class="normal_btn" type="button" name="button1" value="新增"  onClick="addSpceia()">
			</td>
            <td colspan="2" align="left" >&nbsp;</td>
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
	var url = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/querySpeciaExpenses.json";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "特殊费用单号", dataIndex: 'FEE_NO', align:'center',renderer:mySelect},
				{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "产地", dataIndex: 'YIELD', align:'center'},
				{header: "费用类型", dataIndex: 'FEE_TYPE', align:'center',renderer:getItemValue},
				{header: "申报金额(元)", dataIndex: 'DECLARE_SUM1', align:'center',renderer:amountFormat},
				{header: "工单状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//跳转新增页面
	function addSpceia()
	{
		var val = document.getElementById("feeType").value;
		if(val==""||val==null)
		{
			MyAlert("请选择要新增费用的类型");
			return;
		}
		fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/addSpeciaExpenses.do?uuFlag=1";
		fm.submit();
	}
	
	
	
	//修改的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='updateSpecialExpenses(\""+record.data.ID+"\",\""+ record.data.FEE_TYPE +"\")'>[修改]</a>"+"<a href=\"#\" onclick=\"deleteById("+record.data.ID+")\">[删除]</a>");
  	}
	//删除 zyw 2014-9-16
	function deleteById(id){
		MyConfirm("是否确认删除？",deleteByIdSure,[id]);
	}
	function deleteByIdSure(id){
		makeNomalFormCall('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/deleteByIdSure.json?id='+id,deleteByIdSureBack,'fm','');
	}
	function deleteByIdSureBack(json){
		if(json.succ=="1"){
			__extQuery__(1);
			MyAlert("提示：删除成功！");
		}else{
			MyAlert("提示：删除失败！");
		}
	}
	function subChecked(str)
	{
		MyConfirm("是否确认提交？",SpecialExpensesManagecomit,[str]);
	}
	function SpecialExpensesManagecomit(str)
	{
		makeNomalFormCall('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/comitSpecialExpenses.json?id='+str,returnBack,'fm','');
	}
	function returnBack()
	{
		var comit = json.returnValue;
		if(comit==1){
			__extQuery__(1);
			MyAlert("提交成功！");
		}else{
			MyAlert("提交失败！请联系管理员！");
		}
	}
	
	//修改的超链接设置
	function specialPrint(value)
	{
		var tarUrl ='<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/specialPrint.do?id='+value ;
		window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
	}
	
	//修改的超链接设置
	function updateSpecialExpenses(val1,val2)
	{
		fm.action = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/updateSpecialExpenses.do?id=' + val1 + "&feeType=" + val2;
	 	fm.submit();
	}
	
	//设置超链接
	function mySelect(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='sel(\""+record.data.ID+"\",\""+record.data.YIELD+"\",\""+ record.data.FEE_TYPE +"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(val1,val2,val3)
	{
		OpenHtmlWindow('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/speciaExpensesInfo.do?id='+val1+"&feeType="+val3+"&yield="+val2,800,500);
	}
	
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>