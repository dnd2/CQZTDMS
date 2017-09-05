<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>抱怨总数统计</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 报告管理 &gt;抱怨总数统计</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />抱怨总数统计</th>
			
			<tr>
				<td align="right" nowrap="true">受理年份：</td>
				<td align="left" nowrap="true">
					<input type="text" name="dateYear" id="dateYear"
		             value="" type="text" class="short_txt" 
		             datatype="1,is_digit,4"  maxlength="4"/>
		                                 
				</td>
				<td align="right" nowrap="true">省市：</td>
				<td align="left" nowrap="true">
					<select id="tmOrgSmall" name="tmOrgSmall" class="short_sel">
						<option value="">--请选择--</option>
						<c:forEach var="os" items="${proviceList}">
							<option value="${os.REGION_CODE}" title="${os.REGION_NAME}">${os.REGION_NAME}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealName" name="dealName"/>
				</td>
				<td align="right" nowrap="true">日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
				</td>			
			</tr>
			<tr>
				<td align="right" nowrap="true">抱怨对象：</td>
				<td align="left" nowrap="true">
					<input type="text" value="" id="cpObject" name="cpObject" />
				</td>

				<td align="right" nowrap="true">报表类型：</td>
				<td align="left" nowrap="true">
					<select id="reportType" name="reportType" class="short_sel">
						<c:forEach var="rep" items="${reportTypeList}">
							<option value="${rep.value}" title="${rep.key}">${rep.key}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<span style="color: red; font-weight: bold;">
						需考核抱怨总数=实际受理抱怨总数+当月前受理当月转交当月考核数+当月前受理当月转交下月考核数+移入上月考核数量-移出至下月考核数量-当月未转交或次月转交数量 
					</span>
        		</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<input align="right" class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="search();" />
					<input id="downExcel" name="downExcel" type="button" value="导出Excel" class="normal_btn" onclick="downExcelQuery();" />
        		</td>
			</tr>
		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	function downExcelQuery(){
		fm.action = '<%=contextPath%>/customerRelationships/reportformsManage/ComplaintTotalReportforms/complaintTotalReportformsExcel.do';
		fm.submit();
	}
	
	function check(){
		var msg ="";
		if(""==document.getElementById('dateStart').value){
			msg+="开始日期不能为空!</br>"
		}
		if(""==document.getElementById('dateEnd').value){
			msg+="结束日期不能为空!</br>"
		}
		if(msg!=""){
			MyAlert(msg);
			return false;
		}else{
			return true;
		}
	}
	
	function search(){
		if(check()){
			setColumns();
			__extQuery__(1);
		}
	}
	
	function setColumns(){
		var reportValue = document.getElementById('reportType').value;
		if(reportValue == '1'){
			columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "受理商家",dataIndex: 'TYPENAME',align:'center'},
				{header: "需考核抱怨总数",dataIndex: 'TOTAL',align:'center'},
				{header: "实际受理抱怨总数",dataIndex: 'ACCTOTAL',align:'center'},
				{header: "移入上月考核数量",dataIndex: 'INTOTAL',align:'center'},
				{header: "移出至下月考核数量",dataIndex: 'OUTTOTAL',align:'center'},
				{header: "当月未转交或次月转交数量",dataIndex: 'UNTURNTOTAL',align:'center'},
				{header: "当月前受理当月转交当月考核数",dataIndex: 'YACCTOTAL',align:'center'},
				{header: "当月前受理当月转交下月考核数",dataIndex: 'NACCTOTAL',align:'center'},
				{header: "当月前跨月数量",dataIndex: 'UPTOTAL',align:'center'},
				{header: "正在处理中数量", dataIndex: 'DOINGC', align:'center'},
				{header: "已关闭数量",dataIndex: 'CLOSEDC',align:'center'},
				{header: "强制关闭数量",dataIndex: 'FORCECLOSEDC',align:'center'},
				{header: "用户满意数量",dataIndex: 'GOODC',align:'center'},
				{header: "抱怨关闭率", dataIndex: 'CLOSEDR', align:'center'},
				{header: "抱怨处理满意率", dataIndex: 'GOODR', align:'center'},
				{header: "平均处理时长[小时]", dataIndex: 'AVGTOTAL', align:'center'},
				{header: "平均处理时长(1天)[小时]", dataIndex: 'AVGONEDAY', align:'center'},
				{header: "平均处理时长(3天)[小时]", dataIndex: 'AVGTHREEDAY', align:'center'},
				{header: "平均处理时长(7天)[小时]", dataIndex: 'AVGSEVENDAY', align:'center'}
		      ];
		}else if(reportValue == '2'){
			columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "省份",dataIndex: 'TYPENAME',align:'center'},
				{header: "需考核抱怨总数",dataIndex: 'TOTAL',align:'center'},
				{header: "实际受理抱怨总数",dataIndex: 'ACCTOTAL',align:'center'},
				{header: "移入上月考核数量",dataIndex: 'INTOTAL',align:'center'},
				{header: "移出至下月考核数量",dataIndex: 'OUTTOTAL',align:'center'},
				{header: "当月未转交或次月转交数量",dataIndex: 'UNTURNTOTAL',align:'center'},
				{header: "当月前受理当月转交当月考核数",dataIndex: 'YACCTOTAL',align:'center'},
				{header: "当月前受理当月转交下月考核数",dataIndex: 'NACCTOTAL',align:'center'},
				{header: "当月前跨月数量",dataIndex: 'UPTOTAL',align:'center'},
				{header: "正在处理中数量", dataIndex: 'DOINGC', align:'center'},
				{header: "已关闭数量",dataIndex: 'CLOSEDC',align:'center'},
				{header: "强制关闭数量",dataIndex: 'FORCECLOSEDC',align:'center'},
				{header: "用户满意数量",dataIndex: 'GOODC',align:'center'},
				{header: "抱怨关闭率", dataIndex: 'CLOSEDR', align:'center'},
				{header: "抱怨处理满意率", dataIndex: 'GOODR', align:'center'},
				{header: "平均处理时长[小时]", dataIndex: 'AVGTOTAL', align:'center'},
				{header: "平均处理时长(1天)[小时]", dataIndex: 'AVGONEDAY', align:'center'},
				{header: "平均处理时长(3天)[小时]", dataIndex: 'AVGTHREEDAY', align:'center'},
				{header: "平均处理时长(7天)[小时]", dataIndex: 'AVGSEVENDAY', align:'center'}
		      ];
		}else if(reportValue == '3'){
			columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "受理部门",dataIndex: 'TYPENAME',align:'center'},
				{header: "需考核抱怨总数",dataIndex: 'TOTAL',align:'center'},
				{header: "实际受理抱怨总数",dataIndex: 'ACCTOTAL',align:'center'},
				{header: "移入上月考核数量",dataIndex: 'INTOTAL',align:'center'},
				{header: "移出至下月考核数量",dataIndex: 'OUTTOTAL',align:'center'},
				{header: "当月未转交或次月转交数量",dataIndex: 'UNTURNTOTAL',align:'center'},
				{header: "当月前受理当月转交当月考核数",dataIndex: 'YACCTOTAL',align:'center'},
				{header: "当月前受理当月转交下月考核数",dataIndex: 'NACCTOTAL',align:'center'},
				{header: "当月前跨月数量",dataIndex: 'UPTOTAL',align:'center'},
				{header: "正在处理中数量", dataIndex: 'DOINGC', align:'center'},
				{header: "已关闭数量",dataIndex: 'CLOSEDC',align:'center'},
				{header: "强制关闭数量",dataIndex: 'FORCECLOSEDC',align:'center'},
				{header: "用户满意数量",dataIndex: 'GOODC',align:'center'},
				{header: "抱怨关闭率", dataIndex: 'CLOSEDR', align:'center'},
				{header: "抱怨处理满意率", dataIndex: 'GOODR', align:'center'},
				{header: "平均处理时长[小时]", dataIndex: 'AVGTOTAL', align:'center'},
				{header: "平均处理时长(1天)[小时]", dataIndex: 'AVGONEDAY', align:'center'},
				{header: "平均处理时长(3天)[小时]", dataIndex: 'AVGTHREEDAY', align:'center'},
				{header: "平均处理时长(7天)[小时]", dataIndex: 'AVGSEVENDAY', align:'center'}
		      ];
		}else if(reportValue == '4'){
			columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "类别",dataIndex: 'TYPENAME',align:'center'},
				{header: "需考核抱怨总数",dataIndex: 'TOTAL',align:'center'},
				{header: "实际受理抱怨总数",dataIndex: 'ACCTOTAL',align:'center'},
				{header: "移入上月考核数量",dataIndex: 'INTOTAL',align:'center'},
				{header: "移出至下月考核数量",dataIndex: 'OUTTOTAL',align:'center'},
				{header: "当月未转交或次月转交数量",dataIndex: 'UNTURNTOTAL',align:'center'},
				{header: "当月前受理当月转交当月考核数",dataIndex: 'YACCTOTAL',align:'center'},
				{header: "当月前受理当月转交下月考核数",dataIndex: 'NACCTOTAL',align:'center'},
				{header: "当月前跨月数量",dataIndex: 'UPTOTAL',align:'center'},
				{header: "正在处理中数量", dataIndex: 'DOINGC', align:'center'},
				{header: "已关闭数量",dataIndex: 'CLOSEDC',align:'center'},
				{header: "强制关闭数量",dataIndex: 'FORCECLOSEDC',align:'center'},
				{header: "用户满意数量",dataIndex: 'GOODC',align:'center'},
				{header: "抱怨关闭率", dataIndex: 'CLOSEDR', align:'center'},
				{header: "抱怨处理满意率", dataIndex: 'GOODR', align:'center'},
				{header: "平均处理时长[小时]", dataIndex: 'AVGTOTAL', align:'center'},
				{header: "平均处理时长(1天)[小时]", dataIndex: 'AVGONEDAY', align:'center'},
				{header: "平均处理时长(3天)[小时]", dataIndex: 'AVGTHREEDAY', align:'center'},
				{header: "平均处理时长(7天)[小时]", dataIndex: 'AVGSEVENDAY', align:'center'}
		      ];
		}else if(reportValue == '5'){
			columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "年份",dataIndex: 'TYPENAME',align:'center'},
				{header: "需考核抱怨总数",dataIndex: 'TOTAL',align:'center'},
				{header: "实际受理抱怨总数",dataIndex: 'ACCTOTAL',align:'center'},
				{header: "移入上月考核数量",dataIndex: 'INTOTAL',align:'center'},
				{header: "移出至下月考核数量",dataIndex: 'OUTTOTAL',align:'center'},
				{header: "当月未转交或次月转交数量",dataIndex: 'UNTURNTOTAL',align:'center'},
				{header: "当月前受理当月转交当月考核数",dataIndex: 'YACCTOTAL',align:'center'},
				{header: "当月前受理当月转交下月考核数",dataIndex: 'NACCTOTAL',align:'center'},
				{header: "当月前跨月数量",dataIndex: 'UPTOTAL',align:'center'},
				{header: "正在处理中数量", dataIndex: 'DOINGC', align:'center'},
				{header: "已关闭数量",dataIndex: 'CLOSEDC',align:'center'},
				{header: "强制关闭数量",dataIndex: 'FORCECLOSEDC',align:'center'},
				{header: "用户满意数量",dataIndex: 'GOODC',align:'center'},
				{header: "抱怨关闭率", dataIndex: 'CLOSEDR', align:'center'},
				{header: "抱怨处理满意率", dataIndex: 'GOODR', align:'center'},
				{header: "平均处理时长[小时]", dataIndex: 'AVGTOTAL', align:'center'},
				{header: "平均处理时长(1天)[小时]", dataIndex: 'AVGONEDAY', align:'center'},
				{header: "平均处理时长(3天)[小时]", dataIndex: 'AVGTHREEDAY', align:'center'},
				{header: "平均处理时长(7天)[小时]", dataIndex: 'AVGSEVENDAY', align:'center'}
		      ];
		}else if(reportValue == '6'){
			columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "大区",dataIndex: 'TYPENAME',align:'center'},
				{header: "需考核抱怨总数",dataIndex: 'TOTAL',align:'center'},
				{header: "实际受理抱怨总数",dataIndex: 'ACCTOTAL',align:'center'},
				{header: "移入上月考核数量",dataIndex: 'INTOTAL',align:'center'},
				{header: "移出至下月考核数量",dataIndex: 'OUTTOTAL',align:'center'},
				{header: "当月未转交或次月转交数量",dataIndex: 'UNTURNTOTAL',align:'center'},
				{header: "当月前受理当月转交当月考核数",dataIndex: 'YACCTOTAL',align:'center'},
				{header: "当月前受理当月转交下月考核数",dataIndex: 'NACCTOTAL',align:'center'},
				{header: "当月前跨月数量",dataIndex: 'UPTOTAL',align:'center'},
				{header: "正在处理中数量", dataIndex: 'DOINGC', align:'center'},
				{header: "已关闭数量",dataIndex: 'CLOSEDC',align:'center'},
				{header: "强制关闭数量",dataIndex: 'FORCECLOSEDC',align:'center'},
				{header: "用户满意数量",dataIndex: 'GOODC',align:'center'},
				{header: "抱怨关闭率", dataIndex: 'CLOSEDR', align:'center'},
				{header: "抱怨处理满意率", dataIndex: 'GOODR', align:'center'},
				{header: "平均处理时长[小时]", dataIndex: 'AVGTOTAL', align:'center'},
				{header: "平均处理时长(1天)[小时]", dataIndex: 'AVGONEDAY', align:'center'},
				{header: "平均处理时长(3天)[小时]", dataIndex: 'AVGTHREEDAY', align:'center'},
				{header: "平均处理时长(7天)[小时]", dataIndex: 'AVGSEVENDAY', align:'center'}
		      ];
		}
		
	}
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/reportformsManage/ComplaintTotalReportforms/queryComplaintTotalReportforms.json";
				
	var title = null;
	var columns = null;

		      

</script>
</body>
</html>