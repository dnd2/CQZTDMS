<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.PunishmentApplyMantainBean"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>

<head>
<% String contextPath = request.getContextPath(); %>
<%
PunishmentApplyMantainBean MantainBean = (PunishmentApplyMantainBean)request.getAttribute("MantainBean");
List<PunishmentApplyMantainBean> MantainList = (List)request.getAttribute("MantainList");
 %>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>奖惩审批表明细</title>
</head>

<body onload="javascript:changBand();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置：信息反馈管理&gt;信息反馈提报&gt;奖惩审批表</div>
 <form method="post" name ="fm">
  <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
        <tr>
          <th colspan="4"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 基本信息</th>
        </tr>
        <tr bgcolor="F3F4F8">
          <td width="18%" align="right">工单号：</td>
          <td width="30%"><%=MantainBean.getOrderId()%></td>
          <td width="13%"  align="right">&nbsp;</td>
          <td width="39%">&nbsp;</td>
        </tr>
        <tr bgcolor="F3F4F8">
          <td  align="right">经销商：</td>
          <td><%=MantainBean.getDealerName()==null?"":MantainBean.getDealerName()%></td>
          <td  align="right">类型：</td>
          <td>
   
          <script type='text/javascript'>
		       var rewardType=getItemValue('<%=MantainBean.getRewardType()%>');
		       document.write(rewardType) ;
		 </script>  
          </td>
        </tr>
        <tr bgcolor="F3F4F8">
          <td align="right">申请单位：</td>
          <td><%=MantainBean.getLinkMan()==null?"":MantainBean.getLinkMan()%></td>
          <td align="right">联系电话：</td>
          <td><%=MantainBean.getTel()==null?"":MantainBean.getTel()%></td>
        </tr>
        <tr id="01"  bgcolor="FFFFFF" style="display:inline">
          <td align="right">奖励方式：</td>
          <td >
           <script type='text/javascript'>
		       var rewardMode=getItemValue('<%=MantainBean.getRewardMode()%>');
		       document.write(rewardMode) ;
		 </script>  
		 <input type="hidden" name="rewardMode" id="rewardMode" value="<%=MantainBean.getRewardMode()%>"/>
          </td>
          <td id="03" style="display:none" align="right" bgcolor="F3F4F8" >奖励金额：</td>
          <td bgcolor="F3F4F8" id="rewardMoney">
          <%=MantainBean.getRewardMoney()==null?"":MantainBean.getRewardMoney()%>
          </td>
        </tr>
        <tr id="02"  style="display:none" bgcolor="FFFFFF" >
          <td align="right">处罚方式：</td>
          <td>
           <script type='text/javascript'>
		       var rewardModePunish=getItemValue('<%=MantainBean.getRewardMode()%>');
		       document.write(rewardModePunish) ;
		 </script>  
		 <input type="hidden" name="rewardModePunish" id="rewardModePunish" value="<%=MantainBean.getRewardMode()%>"/>
          </td>
          <td id="04" style="display:none" align="right" bgcolor="F3F4F8" >处罚金额：</td>
          <td bgcolor="F3F4F8" id="rewardMoneyPunish">
         <%=MantainBean.getRewardMoney()==null?"":MantainBean.getRewardMoney()%>
          </td>
        </tr>
         <script type="text/javascript">
          if('<%=MantainBean.getRewardType()%>' == '<%=Constant.PUNISHMENT_01%>'){
  			document.getElementById("01").style.display = "inline";
  			document.getElementById("02").style.display = "none";
  			var rewardMode = document.getElementById("rewardMode").value;
  			if(rewardMode=='<%=Constant.REWARD_02%>'){
  				document.getElementById("03").style.display = "inline";
				document.getElementById("rewardMoney").style.display = "inline";
		    }else{
			        document.getElementById("03").style.display = "none";
				    document.getElementById("rewardMoney").style.display = "none";
				 }
  		}else if('<%=MantainBean.getRewardType()%>' == '<%=Constant.PUNISHMENT_02%>'){
  			document.getElementById("01").style.display = "none";
  			document.getElementById("02").style.display = "inline";
  			var rewardModePunish = document.getElementById("rewardModePunish").value;
  			if(rewardModePunish=='<%=Constant.PUNISH_03%>'){
				document.getElementById("04").style.display = "inline";
				document.getElementById("rewardMoneyPunish").style.display = "inline";
			}else{
				document.getElementById("04").style.display = "none";
				document.getElementById("rewardMoneyPunish").style.display = "none";
				 }
  		   }
          </script>
        <tr bgcolor="FFFFFF">
          <td   align="right" bgcolor="FFFFFF">申请日期：</td>
          <td bgcolor="FFFFFF"   >
          <%=MantainBean.getRewardDate()%>
          </td>
          <td align="right" bgcolor="FFFFFF">&nbsp;</td>
          <td bgcolor="FFFFFF" >&nbsp;</td>
        </tr>
        <tr bgcolor="FFFFFF">
          <td  align="right">申请内容：</td>
          <td  colspan="3" align="left" ><span class="tbwhite">
            <%=MantainBean.getRewardContent()==null?"":MantainBean.getRewardContent()%>
          </span></td>
        </tr>
      </table>
    <tr bgcolor="F3F4F8"> 
      <td></td>
    </tr>
    <tr> 
      <td  height=10> 
   <br/>
   <table class="table_list" style="border-bottom:1px solid #DAE0EE">
		   <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" alt=""/> 审批明细</th>
          <tr>
            <th> 审批时间</th>
            <th> 审批人员</th>
            <th> 人员部门</th>
            <th> 审批状态</th>
            <th> 审批意见</th>
           </tr>
            <c:forEach var="MantainList" items="${MantainList}">
		          <tr class="table_list_row1">
			            <td> <c:out value="${MantainList.auditDate}"></c:out>
			            </td>
			            <td><span class="tbwhite"> 
			            <c:out value="${MantainList.name}"></c:out>
			            </span></td>
			            <td><span class="tbwhite">
			            <c:out value="${MantainList.orgName}"></c:out>
			            </span>
			            </td>
			            <td>
			           <script type='text/javascript'>
						       var name=getItemValue('${MantainList.auditStatus}');
						       document.write(name) ;
						</script>
			            </td>
			            <td>
			            <c:out value="${MantainList.auditContent}"></c:out>
			            </td>
		           </tr>
	        </c:forEach>
   </table>         
    <table class="table_list">
       <tr > 
         <th height="12" align=center>
          <input type="button" onClick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
           &nbsp;&nbsp;
          </th>
       </tr>
     </table>
    <!-- 资料显示区结束 -->
</form>
</body>
</html>