<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="<%=FileConstant.regionJsUrl%>"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style>.audit-form{margin-top: 10px}</style>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" >
	var $J = jQuery.noConflict();

	var myPage; 
	var auditingUrl = "<%=contextPath%>/OutMaintainAction/auditing.json";
	var downloadExcelUrl = "<%=contextPath%>/OutMaintainAction/downloadExcel.json";
	// 保存信息反馈类型
	function agreeAuditing() {
		MyConfirm("确定同意？",agreeAuditingDo);
	}
	function agreeAuditingDo(){
		var tUrl = auditingUrl+"?status=97511004";
		makeNomalFormCall(tUrl, message, 'fm');
	}

	function returnAuditing() {
		MyConfirm("确定退回？",returnAuditingDo);
	}
	function returnAuditingDo(){
		var tUrl = auditingUrl+"?status=97511006";
		makeNomalFormCall(tUrl, message, 'fm');
	}

	function refuseAuditing() {
		MyConfirm("确定拒绝？",refuseAuditingDo);
	}
	function refuseAuditingDo(){
		var tUrl = auditingUrl+"?status=97511005";
		makeNomalFormCall(tUrl, message, 'fm');
	}

	function message(json){
		if(json.message == "success"){
			MyAlert("操作成功！",backInit);
		}else{
			MyAlert("操作失败 ");
		}
	}


	function downloadExcel(){
		var id = document.getElementById("id").value;
		window.location.href = downloadExcelUrl+"?id="+id;
	}

	function printPage(id) { 
		var url = "<%=contextPath%>/OutMaintainAction/addOrCheckMaintainApplication.do?id="+id+"&check=4";
		window.open(url,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
	}


	function backInit(){		
		window.location.href = "<%=contextPath%>/OutMaintainAction/MaintainSelect.do";		
	}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>外出维修申请查看</title>
</head>
<body id="printTable">
<div class="wbox">
   <div class="navigation"><font style="font-size: 13px;font-family:serif;"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 车辆信息管理管理&gt; 外出救援申请查看</font></div>
  <form method="post" name="fm" id="fm">
    <!--隐藏表单域 -->
    <input type="hidden" value="${info.ID }" name="id" id="id"/>
    <input type="hidden" value="${type }" id="type"/>
    <div class="form-panel">
       <h2>外出救援申请查看</h2>
       <div class="form-body">
          <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
            <tbody>
                <tr>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 单据编码：</td>
                  <td align="left">
                    ${info.EGRESS_NO }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 制单人姓名：</td>
                  <td align="left">
                    ${info.CREATE_BY }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 制单日期：</td>
                  <td align="left">
                ${info.CREATE_DATE }
                  </td>
                </tr>
                <tr >
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 服务站编码：</td>
                  <td align="left">
                    ${info.DEALER_CODE }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 服务站：</td>
                  <td align="left">
                      ${info.DEALER_NAME }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 服务站电话：</td>
                  <td align="left">
                    ${info.PHONE }
                  </td>
                </tr>
              <tr >
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> VIN码：</td>
                  <td align="left">
                    ${info.VIN }
                  </td>
                  <%-- <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 发动机号：</td>
                  <td align="left" id="engineNo">${info.ENGINE_NO }</td> --%>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车系名称：</td>
                  <td align="left" id="seName">${info.SE_NAME }</td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车型名称：</td>
                  <td align="left" id="vehicleName">${info.GROUP_NAME }</td>
                </tr>
                
                <tr >      	
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 配置：</td>
                  <td align="left" id="pzName">${info.PZ_NAME }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车牌号：</td>
                  <td align="left" id="plateNum">${info.LICENSE_NO }
                  </td>  
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车主名字：</td>
                  <td align="left" id="userName">${info.CTM_NAME }
                  </td> 
                </tr>
                
                <tr >   
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 出厂日期：</td>
              <td align="left" id="outDate2">${info.FACTORY_DATE }
              </td>  	      
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 购车日期：</td>
                  <td align="left" id="buyDate2">${info.PURCHASED_DATE }
                  </td>       
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 用户地址：</td>
                  <td align="left" id="userAddress">${info.ADDRESS }
                  </td>
                </tr>
                
                <tr >       
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 客户名字：</td>
                  <td align="left">
                    ${info.CUSTOMER_NAME }
                  </td> 
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 客户电话：</td>
                  <td align="left">
                    ${info.TELEPHONE }
                  </td>           
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 行驶里程：</td>
                  <td align="left">
                    ${info.MILEAGE }
                  </td>
                </tr>
                    
                <tr >     	        
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 单程救急里程(公里)：</td>
                  <td align="left">
                    ${info.RELIEF_MILEAGE }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">救急所在省：</td>
                  <td align="left">
                    ${info.PROVINCE }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在市：</td>
                  <td align="left">
                    ${info.CITY }
                  </td>
                  </tr>
                  <tr >       
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在县(区)：</td>
                  <td align="left">
                    ${info.COUNTY}
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">详细地址：</td>
                  <td align="left">
                    ${info.TOWN }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急开始时间：</td>
                  <td align="left">
                    ${info.E_START_DATE }
                  </td>
                </tr>
                <tr >        
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急结束时间：</td>
                  <td align="left">
                    ${info.E_END_DATE }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 派车车牌号：</td>
                  <td align="left">
                    ${info.E_LICENSE_NO }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急人数：</td>
                  <td align="left">
                    ${info.E_NUM }
                  </td>
                </tr>
                <tr >
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急人名字：</td>
                  <td align="left" colspan="5">
                    ${info.E_NAME }
                  </td>
                </tr>
                <%-- <tr >
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 审批人：</td>
                  <td align="left">
                    ${info.MINISTER_AUDITING_BY }
                  </td>
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 审批时间：</td>
                  <td align="left">
                    ${info.MINISTER_AUDITING_DATE }
                  </td>
                  <td colspan="2"></td>
                
                </tr> --%>
                
                <tr >
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 申请内容：</td>
                  <td align="left" colspan="5">
                    ${info.EGRESS_REAMRK }
                  </td>
                </tr>  
                <%-- <tr >
                  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 申请意见：</td>
                  <td align="left" colspan="5">
                    ${info.MINISTER_AUDITING_REAMRK }
                  </td>
                </tr>    --%>	
              </tbody>
              </table>
              <div align="center">
                  <input class="u-button" type="button" name="button1"  id="backBtn" value="打印" onclick="printPage(${info.ID });"/>
                  <input class="u-button" type="button" name="button1"  id="backBtn" value="导出" onclick="downloadExcel();"/>
                  <input class="u-button" type="button" name="button1"  id="backBtn" value="返回" onclick="backInit();"/>
              </div>
       </div>
    </div>
  <!-- 审核记录 -->
    <div class="form-panel audit-form">
    <h2>审核记录</h2>
      <div class="form-body">
      <table border="0" id="detailTable" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
      <tr class="table_list_th">
          <th style="text-align:center" width="20%" class="noSort">审核时间</th>
        <th style="text-align:center" width="40%" class="noSort">审核意见</th>
        <th style="text-align:center" width="20%" class="noSort">审核人</th>
        <th style="text-align:center" width="20%" class="noSort">审核状态</th>
        </tr>
      <c:forEach items="${rList}" var="applyR">
        <tr class="table_list_row2">
          <td style="background-color:#FFFFFF;">${applyR.AUDIT_DATE}</td>
          <td style="background-color:#FFFFFF;">${applyR.AUDIT_RECORD}</td>
          <td style="background-color:#FFFFFF;">${applyR.NAME}</td>
          <td style="background-color:#FFFFFF;">${applyR.OPERA_STSTUS}</td> 
        </tr>
      </c:forEach>
      </table>
      </div>
    </div>
  </form>
</div>  
</body>
</html>