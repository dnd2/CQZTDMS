<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartSignDetailListBean"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   ClaimApproveAndStoredReturnInfoBean detailBean = (ClaimApproveAndStoredReturnInfoBean)request.getAttribute("returnListBean");
   @SuppressWarnings("unchecked")
   List<TtAsWrOldPartSignDetailListBean> detailList = (List)request.getAttribute("detailList");
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件审批入库</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="i_back_id" id="i_back_id" value="" />
  <table class="table_edit">
    <tr>
       <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
    </tr>
    <tr bgcolor="F3F4F8">
       <td align="right">经销商代码：</td>
       <td><%=detailBean.getDealer_code()%></td>
       <td align="right">经销商名称：</td>
       <td>
          <%=detailBean.getDealer_name()%>              
       </td>
       <td align="right">所属区域：</td>
       <td>
         <%=detailBean.getAttach_area()%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">回运清单号：</td>
       <td><%=detailBean.getReturn_no()%></td>
       <td align="right">提报日期：</td>
       <td>
         <%=detailBean.getReturn_date()%>
       </td>
       <td align="right">建单日期：</td>
       <td>
         <%=detailBean.getCreate_date()%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">索赔申请单数：</td>
       <td><%=detailBean.getWr_amount()%></td>
       <td align="right">配件项数：</td>
       <td>
         <%=detailBean.getPart_item_amount()%>
       </td>
       <td align="right">配件数：</td>
       <td>
         <%=detailBean.getPart_amount()%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">货运方式：</td>
       <td>
         <%=detailBean.getTransport_desc()%>
       </td>
       <td align="right">回运类型：</td>
       <td>
         <%=detailBean.getReturn_desc()%>
       </td>
       <td align="right">装箱总数：</td>
       <td><%=detailBean.getParkage_amount()%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">索赔单提交时间段：</td>
       <td>
         <%=detailBean.getWr_start_date()%>
       </td>
       <td align="right"></td>
       <td>
       </td>
       <td align="right"></td>
       <td></td>
     </tr>
  </table>
  <!--分页 begin -->
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  <table class="table_list">
   <tr> 
     <td height="10" align="center" colspan="2">
     </td>
   </tr>
   <tr> 
     <td height="10" align="center" colspan="2">
      <input type="button" onclick="preChecked();" class="normal_btn" style="width=8%" value="签收"/>
       &nbsp;&nbsp;
      <input type="button" onclick="validateIsSign('<%=detailBean.getId()%>');" class="normal_btn" style="width=8%" value="签收完成"/>
       &nbsp;&nbsp;
      <input type="button" onclick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
    </td>
   </tr>
  </table>
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage.json?CLAIM_ID="
	+ <%=detailBean.getId()%>;
var title = null;
var all_ids="";//存放所有要入库的配件id
var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{id:'action',header: "<input type='checkbox' id='checkAll' name='checkAll' onclick='selectAll(this,\"orderIds\")'>", width:'8%',sortable: false,dataIndex: 'id',renderer:myCheckBox},
				{header: "索赔申请单", dataIndex: 'claim_no', align:'center'},
				{header: "VIN", dataIndex: 'vin', align:'center'},
				{header: "配件代码", dataIndex: 'part_code', align:'center'},
				{header: "配件名称", dataIndex: 'part_name', align:'center',renderer:hiddenPartName},
				{header: "回运数", dataIndex: 'return_amount', align:'center',renderer:hiddenReturnAmount},
				{header: "签收数", dataIndex: 'sign_amount', align:'center',renderer:editSignNum},
				{header: "装箱单号", dataIndex: 'box_no', align:'center'},
				{header: "库区", dataIndex: 'warehouse_region', align:'center',renderer:editWrHouse},
				{header: "抵扣原因", dataIndex: 'deduct_remark', align:'center',renderer:editDeductCon}
		      ];
//全选checkbox
function myCheckBox(value,metaDate,record){
	  all_ids+=value+",";
	  return String.format("<input type='checkbox' id='orderIds' name='orderIds' value='" + value + "' />");
}
//选中预检查
function preChecked() {
	var str="";
	var chk = document.getElementsByName("orderIds");
	var len = chk.length;
	var cnt = 0;
	for(var i=0;i<len;i++){        
		if(chk[i].checked){            
			str = chk[i].value+","+str; 
			cnt++;
		}
	}
	if(cnt==0){
        MyAlert("请选择要签收的回运配件！");
        return;
    }else{
        var i=validateSelectedId(str.substring(0,str.length-1));
        if(i==1){
        	MyConfirm("确认签收？",sign,[str.substring(0,str.length-1)]);
        }
    }
}
//获取json数据
function window.onload(){
	makeNomalFormCall(url,showList,'fm','createOrdBtn');
}
function showList(json){
	var ps;
	if(Object.keys(json).length>0){
		keys = Object.keys(json);
		for(var i=0;i<keys.length;i++){
		   if(keys[i] =="ps"){
			   ps = json[keys[i]];
			   break;
		   }
		}
	}
	if(ps.records != null){
		$("_page").hide();
		$('myGrid').show();
		new reCreateGrid(title,columns,$("myGrid"),ps).load();			
		//分页
		myPage = new showPages("myPage",ps,url);
		myPage.printHtml();
		hiddenDocObject(2);
	}else{
		$("_page").show();
		$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
		$("myPage").innerHTML = "";
		removeGird('myGrid');
		$('myGrid').hide();
		hiddenDocObject(1);
	}
}
//格式化日期
function formatDate(value) {
	 if (value==""||value==null) {
		document.write("");
	 }else {
		document.write(value.substr(0,10));
	 }
}
//签收数量编辑
function editSignNum(value,metaDate,record){
	var id=record.data.id;
	var prototype_value=record.data.return_amount;//数据库中的配件签收数量
	return String.format("<input type=\"text\" id=\"signNum"+id+"\" name=\"signNum"+id+"\" class=\"short_txt\" datatype='0,is_digit,10' value=\"" + value + "\"/>");
}
//配件名称,用于校验弹出名称提示
function hiddenPartName(value,metaDate,record){
	var id=record.data.id;
	return String.format(value+"<input type=\"hidden\" id=\"partName"+id+"\" name=\"partName"+id+"\" value=\"" + value + "\"/>");
}
//隐藏需回运数量
function hiddenReturnAmount(value,metaDate,record){
	var id=record.data.id;
	var prototype_value=record.data.return_amount;//数据库中的配件签收数量
	return String.format(prototype_value+"<input type=\"hidden\" id=\"returnAmount"+id+"\" name=\"returnAmount"+id+"\" value=\"" + prototype_value + "\"/>");
}
//库区编辑
function editWrHouse(value,metaDate,record){
	var id=record.data.id;
	return String.format("<input type=\"text\" id=\"wrHouse"+id+"\" name=\"wrHouse"+id+"\" class=\"short_txt\"  value=\"" + value + "\" />");
}
//抵扣原因编辑
function editDeductCon(value,metaDate,record){
	var id=record.data.id;
	var str=genSelBoxStrExp("deduct"+id,<%=Constant.OLDPART_DEDUCT_TYPE%>,value,true,"","","true",'');
	return String.format(str);
}
//签收操作
function sign(_str){
	fm.i_back_id.value='<%=detailBean.getId()%>';
	var checkIdStr=_str;
	var str="?";
	var flag=0;
	str+="idStr="+_str;
	str+="&return_ord_id="+'<%=detailBean.getId()%>';
	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored.json"+str;
	makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
}
//签收回调处理
function afterCall(json){
	var retCode=json.updateResult;
    if(retCode!=null&&retCode!=''){
      if(retCode=="updateSuccess"){
   	    var chk = document.getElementsByName("orderIds");
   	    document.getElementById("checkAll").checked=false;
   		var len = chk.length;
   		var cnt = 0;
   		for(var i=0;i<len;i++){        
   			chk[i].checked=false;
   		}
    	MyAlert("签收成功!");
    	showList(json);
      }else if(retCode=="updateFailure"){
    	MyAlert("签收失败!");
     }
   }
}
//签收完成前校验是否完成签收操作
function validateIsSign(id){
	var temp=all_ids;
	temp=temp.substring(0,temp.length-1);
	var submit_url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/validateIsSignOper.json?id="+id+"&idStr="+temp;
	makeNomalFormCall(submit_url,signPreFinish,'fm','createOrdBtn');
}
//签收完成
function signPreFinish(json){
	var id=json.id;
	var finishFlag=json.finishFlag;
	var temp=all_ids;
	temp=temp.substring(0,temp.length-1);
	//finishFlag=validateSelectedId(temp);
	var transNo=document.getElementById("transNo");
	//if(transNo.value==''){
	//	MyAlert("请填写货运单号!");
	//	return;
	//}
	if(finishFlag==1){
	    MyConfirm("您确认完成索赔配件的签收工作了？",signFinish,[id]);
	}else{
		MyAlert("您还没有完成全部索赔配件的签收工作!");
		return;
	}
}
//签收完成
function signFinish(id){
	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndFinish.json?id="+id;
	makeNomalFormCall(url,aferFinish,'fm','createOrdBtn');
}
//签收完成回调处理
function aferFinish(json){
	var retCode=json.finishResult;
	if(retCode!=null&&retCode!=''){
	   if(retCode=="finishSuccess"){
		  parent.window.MyAlert("签收完成操作成功!");
		  fm.action= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/queryListPage.do";
		  fm.submit();
	   }else{
		  parent.window.MyAlert("签收完成操作失败!");
	   }
	}
}
//点击签收动作，只对勾选的入库配件进行数量和抵扣原因校验
function validateSelectedId(selectStr){
	var retCode=1;
	if(selectStr==null||selectStr==''){
       return 0;//没有勾选入库配件
	}
	var selectArr=selectStr.split(",");
	for(var i=0;i<selectArr.length;i++){
		var requiredNum=document.getElementById("returnAmount"+selectArr[i]).value;
		var backNum=document.getElementById("signNum"+selectArr[i]).value;
		var deductReason=document.getElementById("deduct"+selectArr[i]).value;
		var partName=document.getElementById("partName"+selectArr[i]).value;
		var diffNum=requiredNum-backNum;
		if(backNum==null||backNum==''){
			parent.window.MyAlert("配件名称'"+partName+"'的签收数不能为空！");
			retCode=0;
			break;
		}
		if(diffNum<0){
			parent.window.MyAlert("配件名称'"+partName+"'的签收数不能超过回运数！");
			retCode=0;
			break;
		}
		if(diffNum>0&&deductReason==''){
			parent.window.MyAlert("配件名称'"+partName+"'的签收存在数量差异，请选择抵扣原因！");
			retCode=0;
			break;
		}
		if(diffNum==0&&deductReason>0){
			parent.window.MyAlert("配件名称'"+partName+"'的签收数量并没有差异，无需选择抵扣原因！");
			retCode=0;
			break;
		}
	}
	return retCode;
}
</script>
</body>
</html>
