<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TtIfRewardPO"%>
<%@page import="com.infodms.dms.bean.PunishmentApplyMantainBean"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>

<head>
<title>奖惩审批表修改</title>
<% String contextPath = request.getContextPath(); 
PunishmentApplyMantainBean  RewardPO =(PunishmentApplyMantainBean)request.getAttribute("RewardPO");
%>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：信息反馈管理 &gt;信息反馈提报 &gt;奖惩审批表修改</div>
  <form method="post" name ="fm" id="fm">
 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
	  		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息</th>
            <tr bgcolor="F3F4F8">
              <td align="right">工单号：</td>
              <td><input type="hidden" id="orderId" name="orderId" value="<%=RewardPO.getOrderId()%>"><%=RewardPO.getOrderId()%></td>
              <td height="27" align="right">&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          <tr bgcolor="F3F4F8">
            <td height="27" align="right">经销商：</td>
        	<td>
			<textarea  rows="2" cols="35" id="dealerCode" name="dealerCode"><%=RewardPO.getDealerCode() %></textarea>
			<input type="hidden" id="dealerId" name="dealerId" value="<%=RewardPO.getDealerId() %>"/>
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showNewOrgDealer('dealerCode','dealerId','true');" value="..." />        
       		 </td>
            <td height="27" align="right">类型：</td>
            <td>
            	<script type="text/javascript">
   					genSelBoxExp("rewardType",<%=Constant.PUNISHMENT%>,"<%=RewardPO.getRewardType()%>",false,"short_sel","onchange='changType(document.fm.rewardType.options[document.fm.rewardType.selectedIndex].value)'","true",'');
  				</script>
            </td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td align="right">申请单位：</td>
            <td><input type='text'  name='linkMan'  id='linkMan' size='18' class="middle_txt" value="<%=RewardPO.getLinkMan()%>" datatype="0,is_digit_letter_cn,18"></td>
            <td align="right">联系电话：</td>
            <td><input type='text' name='tel' id='tel' size='18'  class="middle_txt" value="<%=RewardPO.getTel()==null?"":RewardPO.getTel()%>" datatype="0,is_phone,18"></td>
          </tr>
          <tr id="01" bgcolor="FFFFFF" style="display:inline">
            <td align="right" style="width:300px">奖励方式：</td>
            <td>
	            <script type="text/javascript">
	              genSelBoxExp("rewardMode",<%=Constant.REWARD%>,"<%=RewardPO.getRewardMode()%>",false,"short_sel","onchange='changFangShi(document.fm.rewardMode.options[document.fm.rewardMode.selectedIndex].value)'","true",'');
	            </script>
          	</td>
            <td align="right" bgcolor="F3F4F8" id="03" style="display:none">奖励金额：</td>
            <td bgcolor="F3F4F8"><input type='text'  value="<%=RewardPO.getRewardMoney()==null?"":RewardPO.getRewardMoney()%>"  name='rewardMoney'  id='rewardMoney' size='18'  class="middle_txt" datatype="0,is_digit,18"></td>
          </tr>
          <tr id="02" bgcolor="FFFFFF" style="display:none">
            <td align="right" style="width:300px">处罚方式：</td>
            <td>
                <script type="text/javascript">
	              genSelBoxExp("rewardModePunish",<%=Constant.PUNISH%>,"<%=RewardPO.getRewardMode()%>",false,"short_sel","onchange='changFangShi(document.fm.rewardModePunish.options[document.fm.rewardModePunish.selectedIndex].value)'","true",'');
	            </script>
            </td>
            <td align="right" bgcolor="F3F4F8" id="04" style="display:none">处罚金额：</td>
            <td bgcolor="F3F4F8"><input type='text' value="<%=RewardPO.getRewardMoney()==null?"":RewardPO.getRewardMoney()%>" name='rewardMoneyPunish' id='rewardMoneyPunish'  size='18' class="middle_txt" datatype="0,is_digit,18"> </td>
          </tr>
          <script type="text/javascript">
          if('<%=RewardPO.getRewardType()%>' == '<%=Constant.PUNISHMENT_01%>'){
  			document.getElementById("01").style.display = "inline";
  			document.getElementById("02").style.display = "none";
  			if(document.fm.rewardMode.options[document.fm.rewardMode.selectedIndex].value=='<%=Constant.REWARD_02%>'){
  				document.getElementById("03").style.display = "inline";
				document.getElementById("rewardMoney").style.display = "inline";
				}else{
					document.getElementById("03").style.display = "none";
				    document.getElementById("rewardMoney").style.display = "none";
					}
  		}else if('<%=RewardPO.getRewardType()%>' == '<%=Constant.PUNISHMENT_02%>'){
  			document.getElementById("01").style.display = "none";
  			document.getElementById("02").style.display = "inline";
  			if(document.fm.rewardModePunish.options[document.fm.rewardModePunish.selectedIndex].value=='<%=Constant.PUNISH_03%>'){
				document.getElementById("04").style.display = "inline";
				document.getElementById("rewardMoneyPunish").style.display = "inline";
				}else{
				document.getElementById("04").style.display = "none";
				document.getElementById("rewardMoneyPunish").style.display = "none";
					}
  		}
          </script>
          <tr bgcolor="FFFFFF">
            <td height="27"  align="right" bgcolor="FFFFFF">申请日期：</td>
           <td bgcolor="FFFFFF">
           <div align="left">
            		<input name="rewardDate" id="t1" value="<%=RewardPO.getRewardDate()%>" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
            	</div>
            <td align="right" bgcolor="FFFFFF">&nbsp;</td>
            <td bgcolor="FFFFFF" >&nbsp;</td>
          </tr>
          <tr bgcolor="FFFFFF">
            <td height="27" align="right">申请内容：</td>
            <td height="27" colspan="3" align="left" ><span class="tbwhite">
              <textarea  name='rewardContent' id='rewardContent' rows='5' cols='80' ><%=RewardPO.getRewardContent()==null?"":RewardPO.getRewardContent()%></textarea>
            </span></td>
          </tr>
      </table>
    <tr bgcolor="F3F4F8"> 
      <td></td>
    </tr>
       <table class="table_list">
          <tr > 
            <th height="12" align=center>
			<input type="button" class="normal_btn" style="width=8%" value="确定"  onclick="subChecked('up');"/>
			&nbsp;&nbsp;
			<input type="button" onclick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/></th>
		  </tr>
        </table>
</form>
  <script type="text/javascript">
  //更新
	function punishmentApplyMantainUpdate(){
		var orderId = document.getElementById("orderId").value;
			fm.action = "<%=contextPath%>/feedbackmng/apply/PunishmentApplyMantain/punishmentApplyMantainUpdate.do?orderId="+orderId;
			fm.submit();
	}
	//局部文本框隐藏
	function changType(type){
		if(type == '<%=Constant.PUNISHMENT_01%>'){
			document.getElementById("01").style.display = "inline";
			document.getElementById("02").style.display = "none";
			if(document.fm.rewardMode.options[document.fm.rewardMode.selectedIndex].value=='<%=Constant.REWARD_02%>'){
				document.getElementById("03").style.display = "inline";
				document.getElementById("rewardMoney").style.display = "inline";
				}else{
					document.getElementById("03").style.display = "none";
				document.getElementById("rewardMoney").style.display = "none";
					}
		}else if(type == '<%=Constant.PUNISHMENT_02%>'){
			document.getElementById("01").style.display = "none";
			document.getElementById("02").style.display = "inline";
			if(document.fm.rewardModePunish.options[document.fm.rewardModePunish.selectedIndex].value=='<%=Constant.PUNISH_03%>'){
				document.getElementById("04").style.display = "inline";
				document.getElementById("rewardMoneyPunish").style.display = "inline";
				}else{
				document.getElementById("04").style.display = "none";
				document.getElementById("rewardMoneyPunish").style.display = "none";
					}
		}
	}
function changFangShi(mode){//金额框，是否显示
		
		if(mode=='<%=Constant.REWARD_02%>'||mode=='<%=Constant.PUNISH_03%>'){
			document.getElementById("03").style.display = "inline";
			document.getElementById("rewardMoney").style.display = "inline";
			document.getElementById("04").style.display = "inline";
			document.getElementById("rewardMoneyPunish").style.display = "inline";
			document.getElementById("rewardMoney").value="";//回显金额，清空
			document.getElementById("rewardMoneyPunish").value="";//回显金额，清空
			}else{
			document.getElementById("03").style.display = "none";
			document.getElementById("rewardMoney").style.display = "none";
			document.getElementById("04").style.display = "none";
			document.getElementById("rewardMoneyPunish").style.display = "none";
				}
		}


//修改 开始
function subChecked(action) {
	var str="";
	var dealerId=document.getElementById("dealerId").value;
	if(dealerId==0){
	        MyAlert("请选择经销商！");
	        return;
	    }else{
	     if(action == 'up'){
	     MyConfirm("确认修改？",punishmentApplyMantainUpdate,[str]);
	     
	     }
	    }
	} 
//修改 结束
 </script>
</body>
</html>
