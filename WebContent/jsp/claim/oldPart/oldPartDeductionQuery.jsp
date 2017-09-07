<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>抵扣通知单查询</title>
</head>
<body onload="doInit();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;抵扣通知单查询</div>
  <form id="fm" name="fm">
      <input type="hidden" name="curPage" id="curPage" value="1" />
  	  <input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
  	  <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
    <TABLE class="table_query">
       <tr>
         <td class="right" >通知日期： </td>
         <td align="left" nowrap="true">
           <input id="updateDateStart" name="updateDateStart" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>	
           &nbsp;至&nbsp; 
		   <input id="updateDateEnd" name="updateDateEnd" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
		 </td>	
		  <td class="right" >抵扣单状态：</td>
          <td >
            <script type="text/javascript">
             genSelBoxExp("deductionStatus",<%=Constant.DEDUCTION_STATUS%>,"",true,"u-select","","false",'');
            </script>
          </td>  
      </tr>
       <tr>
         <td class="right" >抵扣单号：</td>
          <td ><input id="deductionNo" name="deductionNo" value="" type="text" class="middle_txt">
          </td>         
       </tr>
       <tr>
         <td class="center" colspan="4" nowrap="nowrap">
           <input class="u-button u-query" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);">
         </td>
       </tr>
  </table>
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
</div>
<br>
</body>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/oldPartDeductionQueryInit.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'DEDUCTION_ID',renderer:myLink,align:'center'},
  				{header: "抵扣单号",dataIndex: 'DEDUCTION_NO',align:'center'},
  				{header: "抵扣类型", dataIndex: 'DEDUCTION_TYPE', align:'center',renderer:getItemValue},
  				{header: "抵扣单状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
  				{header: "通知日期", dataIndex: 'UPDATE_DATE', align:'center'},
  				{header: "材料费扣款（元）", dataIndex: 'PART_DEDUCTION_AMOUNT', align:'center'},
  				{header: "工时费扣款（元）", dataIndex: 'HOURS_DEDUCTION_AMOUNT', align:'center'},
  				{header: "其他项目扣款(元)", dataIndex: 'OUTWARD_DEDUCTION_AMOUNT', align:'center'},
  				{header: "二次抵扣(元)", dataIndex: 'SECOND_DEDUCTION_AMOUNT', align:'center'},
  				{header: "二次抵扣备注", dataIndex: 'SECOND_DEDUCTION_REMARK', align:'center'},
  				{header: "总计(元)", dataIndex: 'TOTAL_DEDUCTION_AMOUNT', align:'center'},
  				{header: "开票单号", dataIndex: 'BALANCE_NO', align:'center'}
  		      ];
  		      
   //超链接设置
   function myLink(value,meta,record){  
	 var claimId = record.data.CLAIM_ID;
	 return String.format("<a href='#' onclick=check("+claimId+")>[查看]</a>");
   }
	
   /**
   * 查看
   * @param value
   */
   function check(value){
   	var id ="?claimId="+value;
   	OpenHtmlWindow("<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/oldPartDeductionCheck.do"+id,800,600);
   }
   function doInit(){
	   __extQuery__(1);
	  loadcalendar();
   }
</script>

</html>