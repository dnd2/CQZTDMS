<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.TtIfMarketDetailBean"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>市场问题处理工单修改页面</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   TtIfMarketDetailBean detailBean = (TtIfMarketDetailBean)request.getAttribute("marketOrderDetailBean");
%>
</head>
<script type="text/javascript">
function window.onload(){
   loadcalendar();
   document.getElementById("send_date").value='<%=detailBean.getOrder_date()%>';
   var comp_type='<%=detailBean.getComp_type()%>';
   var retCode='<%=request.getAttribute("approveResult")%>';
   if(comp_type!=null&&comp_type!=""){
      if(comp_type.indexOf("F")!=-1){
          document.getElementById("Check_Flag_F").checked=true;
      }
      if(comp_type.indexOf("C")!=-1){
          document.getElementById("Check_Flag_C").checked=true;
      }
      if(comp_type.indexOf("B")!=-1){
          document.getElementById("Check_Flag_B").checked=true;
      }
   }
   
   if(retCode!=null||retCode!=''){
     if(retCode=="approveFailure"){
        MyAlert("工单修改失败!");
        goSearchPage();
     }
   }
}
function goSearchPage(){
	fm.action = "<%=contextPath%>/feedbackmng/apply/MarketQuestionOrderApplyManager/marketQuesApplyInit.do";
	fm.submit();
}
</script>
<BODY>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：信息反馈管理 &gt;信息反馈提报 &gt;市场问题处理工单修改</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="curSysDate" id="curSysDate" value="<%=request.getAttribute("curSysDate")%>" /> 
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
	      <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
          <tr bgcolor="F3F4F8">
            <td align="right">工单号：</td>
            <td colspan="5"><%=detailBean.getOrderId()%></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td align="right">服务中心经办人：</td>
            <td><input type="text" name="DEALER_NAME" id="DEALER_NAME" size='18' datatype="0,is_letter_cn,5" value="<%=detailBean.getLinkMan()%>" class="middle_txt"/></td>
            <td align="right">联系电话：</td>
            <td><input type="text" name="link_tel"  id="link_tel" size="22" datatype="0,is_phone,11" value="<%=detailBean.getTel()%>" class="middle_txt" /></td>
            <td height="27" align="right" bgcolor="FFFFFF">申报金额：</td>
            <td align="left" bgcolor="FFFFFF" ><input type="text"  name="apply_money" id="apply_money" value="<%=detailBean.getMoney()%>" datatype="0,is_yuan,5"  class="middle_txt"/>(元)</td>
          </tr>
          <tr bgcolor="FFFFFF">
            <td align="right" ><span class="style1">*</span>车辆识别码(VIN)：</td>
            <td>
            <input class="middle_txt" id="vin" value='<%=detailBean.getVin()==null?"":detailBean.getVin()%>' datatype="0,is_null,17" onclick="showVIN()" style="cursor: pointer;" name="vin" type="text"/>
            </td>
            <td height="27" align="right">车系：</td>
            <td align="left" ><%=detailBean.getGroup_name()==null?"":detailBean.getGroup_name()%></td>
            <td align="right" bgcolor="FFFFFF">发动机号：</td>
            <td bgcolor="FFFFFF"   ><%=detailBean.getEngine_no()==null?"":detailBean.getEngine_no()%></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td height="27"  align="right" bgcolor="F3F4F8">出厂日期：</td>
            <td bgcolor="F3F4F8"   ><%=detailBean.getFactory_date()==null?"":detailBean.getFactory_date()%></td> 
            <td align="right" bgcolor="F3F4F8">购车日期：</td>
            <td bgcolor="F3F4F8" ><%=detailBean.getDelivery_date()==null?"":detailBean.getDelivery_date()%></td>
            <td width="12%" align="right" bgcolor="F3F4F8" >行驶里程(KM)：</td>
            <td ><%=detailBean.getHistory_mile()==null?"":detailBean.getHistory_mile()%></td>
          </tr>
          
          <tr bgcolor="FFFFFF">
            <td align="right">客户姓名：</td>
            <td><%=detailBean.getCustomerName()==null?"":detailBean.getCustomerName()%></td>
            <td align="right">客户联系电话：</td>
            <td align="left"><%=detailBean.getMobile()==null?"":detailBean.getMobile()%></td>
            <td align="right">&nbsp;</td>
            <td align="left">&nbsp;</td>
          </tr>
		  <tr bgcolor="FFFFFF">
            <td height="27" align="right">客户地址：</td>
            <td colspan="5" align="left" ><%=detailBean.getAddress_desc()==null?"":detailBean.getAddress_desc()%></td>
          </tr>
        </table>
    <tr bgcolor="F3F4F8"> 
      <td></td>
   </tr>
    <tr> 
      <td  height=10> 
        <table width="100%" border="0" cellspacing="1">
         
        </table>
      </td>
    </tr>
    <tr> 
      <td > <table width=100% border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="91908E"  class="table_edit">
	    <th colspan="4"><img class="nav" src="../../../img/subNav.gif" /> 申请内容</th>
        <tr bgcolor="F3F4F8">
          <td align="right"><div align="right">信息类别：</div></td>
          <td><script type="text/javascript">
	              genSelBoxExp("info_type",<%=Constant.MARKET_ORDER_INFO_TYPE%>,"<%=detailBean.getInfo_type()%>",false,"short_sel","","false",'');
	           </script></td>
          <td align="right" id="blank1">发出时间：</td>
      
          <td bgcolor="F3F4F8" class="tbwhite" id="blank2">
            <input type='text'  name="send_date"  id="send_date" class="short_txt" datatype="0,is_date,10"/>
            <input class="time_ico" type="button" onClick="showcalendar(event, 'send_date', false);" value=" " />
          </td>
          
        </tr>
        <tr bgcolor="F3F4F8">
          <td align="right"><div align="right"><span class="style1"><font color="red">*</font></span>投诉类型：</div></td>
          <td colspan="3"  class="tbwhite"><input name="Check_Flag_F" id="Check_Flag_F" type="checkbox"/>
                                   服务&nbsp;&nbsp;&nbsp;&nbsp;
            <input name="Check_Flag_C" id="Check_Flag_C" type="checkbox"/>
                                  产品质量&nbsp;&nbsp;&nbsp;&nbsp;
            <input name="Check_Flag_B" id="Check_Flag_B" type="checkbox"/>
                                 备件&nbsp;&nbsp;&nbsp;&nbsp;</td>
        </tr>
        <tr bgcolor="FFFFFF">
          <td align="right" bgcolor="FFFFFF">问题描述：
            <div align="right"></div></td>
         	 <td  class="tbwhite" height="15">
             <textarea  name="question_content" id="question_content" rows="3" cols="80" datatype="1,is_textarea,200"><%=detailBean.getProblemDescribe()%>
             </textarea><font color="red">*</font>
          </td>
        </tr>
        <tr bgcolor="FFFFFF">
          <td align="right" bgcolor="FFFFFF">用户要求如何：
            <div align="right"></div></td>
         	 <td  class="tbwhite" height="15">
             <textarea  name="user_content" id="user_content" rows="3" cols="80" datatype="1,is_textarea,200"><%=detailBean.getUserRequest()%>
             </textarea><font color="red">*</font>
          </td>
        </tr>
        <tr bgcolor="FFFFFF">
          <td align="right" bgcolor="FFFFFF">建议处理方式：
            <div align="right"></div></td>
         	 <td  class="tbwhite" height="15">
             <textarea  name="deal_content" id="deal_content" rows="3" cols="80" datatype="1,is_textarea,200"><%=detailBean.getAdviceDealMode()%>
             </textarea><font color="red">*</font>
          </td>
        </tr>
   </table>
        <table class="table_list">
          <tr > 
            <th height="12" align=center>
			<input type="button" onClick="checkForm('<%=contextPath%>/feedbackmng/apply/MarketQuestionOrderApplyManager/modifyMarkerOrder.do?ORDER_ID=');" class="normal_btn" style="width=8%" value="确定"/>
			&nbsp;&nbsp;
			<input type="button" onclick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/></th>
		  </tr>
        </table>
      </td>
    </tr>
    <!-- 资料显示区结束 -->
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
//表单提交前的验证：
function checkForm(url){
	submitForm('fm') == true ? modifyMarketOrder(url) : "";
}
function modifyMarketOrder(url){
	var compType="";
	if(document.getElementById("Check_Flag_F").checked==true){
		compType+="F|";
    }
	if(document.getElementById("Check_Flag_C").checked==true){
		compType+="C|";
    }
	if(document.getElementById("Check_Flag_B").checked==true){
		compType+="B|";
    }
    
    var content = document.getElementById('question_content').value;
	var le = document.getElementById('question_content').value.length;
	var user_content = document.getElementById('user_content').value;
	var user_le = document.getElementById('user_content').value.length;
	var deal_content = document.getElementById('deal_content').value;
	var deal_le = document.getElementById('deal_content').value.length;
	if(compType.length<=0){
		MyAlert("投诉类型是必选项！");
	}else if(content==null||content=='' ){
		MyAlert("问题描述是必填项！");
	}else if(user_content==null||user_content==''){
		MyAlert("用户要求如何是必填项！");
	}else if(deal_content==null||deal_content==''){
		MyAlert("建议处理方式是必填项！");
	}else if(le>200){
		MyAlert("问题描述不能超过200个字符！");
	}else if(user_le>200){
		MyAlert("用户要求如何不能超过200个字符！");
	}else if(deal_le>200){
		MyAlert("建议处理方式不能超过200个字符！");
	}else{
		fm.action = url+"&compType="+compType;
		MyConfirm("确认修改？",fm.submit);
		//fm.submit();
	}
}
//通过VIN返回客户信息和车辆信息
function returnVinInfo(){
	var query_vin=document.getElementById("vin").value;
	if(query_vin!=null&&query_vin!=''){
        

    }
}
//调用VIN码的公用方法
function showVIN(){
	OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/showVinList.do',800,500);
}
</script>
</BODY>
</html>
