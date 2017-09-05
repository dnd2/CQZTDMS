<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
		genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县
	}
</script>

</head>
<body onunload="javascript:destoryPrototype();">
	<div id="loader" style="position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;"></div>
		<input type="hidden" id="sys_date__" name="sys_date__" value="2012,09,10,11,01,29" />
			

<title>网络专员行程日志新增</title>
<div class="wbox" id="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 行程管理 > 网络专员行程日志 >网络专员行程日志新增</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1"/> 

<div id="customerInfoId">
<table class="table_edit" align="center" id="ctm_table_id" border="1">
	<tbody>
		<tr>
			<th colspan="10"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>网络专员行程日志信息</th>
		</tr>
		<tr>
		<td colspan="2" align="left">
			省份：
			<input id="province" name="province" value="" type="text" size="20"  maxlength="50"/> 
		</td>
		
			<td width="right" align="right" id="tcmtd">目标市场：</td>
			<td width="left" align="left" >
				<input id="mubiao" name="mubiao" value="" type="text" size="20"  maxlength="50"/> 
			</td>
			<td align="right">出差日期：</td>
			<td align="left"  colspan="2" >
				<input name="drivedate" id="drivedate" value="" type="text" class="short_txt"  hasbtn="true" callFunction="showcalendar(event, 'drivedate', false);" />
			</td>
			
			<td width="right" align="right" id="tcmtd">出差人：</td>
			<td width="left" align="left"  >
				<input id="drivename" name="drivename" value="" type="text" size="20"  maxlength="50"/> 
			</td>
		</tr>
		<tr>
			<td align="right">出差目的：</td>
			<td colspan="9" align="left">
				<input id="drivemd" name="drivemd" type="text" size="80" value=""  maxlength="200"/>
			</td>
		</tr>
		<tr>
			<td rowspan="3" align="right">当日出差主要工作：</td>
		</tr>
			<tr>
				<td align="right">上午：</td>
				<td colspan="9" align="left">
					<input id="amwork" name="amwork" type="text" size="80" value=""  maxlength="200"/>
				</td>
			</tr>
			<tr>
				<td align="right">下午：</td>
				<td colspan="9" align="left">
					<input id="pmwork" name="pmwork" type="text" size="80" value=""  maxlength="200"/>
				</td>
			</tr>
		<tr>
			<td align="right">作业项目（勾选）：</td>
			<td align="right">新商业资源拜访：</td>
			<td align="left">
				<input type="checkbox" name="operatItem"   value="60221001"  />
			</td>
			<td align="right">潜客进阶拜访：</td>
			<td  align="left">
				<input type="checkbox" name="operatItem"  value="60221002" />
			</td>
			<td align="right">其他：</td>
			<td align="left" colspan="3">
				<input type="checkbox" name="operatItem"  value="60221003"  />
			</td>
		</tr>
		<tr>
			<td align="right">作业方式（勾选）：</td>
			<td align="right">到店拜访：</td>
			<td align="left">
				<input type="checkbox" name="operatWay" value="60222001"  />
			</td>
			<td align="right">电话跟进：</td>
			<td  align="left">
				<input type="checkbox" name="operatWay" value="60222002"  />
			</td>
			<td align="right">场地勘测：</td>
			<td align="left" colspan="3">
				<input type="checkbox" name="operatWay" value="60222003"  />
			</td>
		</tr>
</tbody></table>


<!-- 添加附件 开始  -->
        <table id="add_file" style="display:block" width="100%" class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
	    		<tr>
	        		<th>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息(添加行程票据照片、商圈、店面拜访照片,支持格式包括：pdf,图片或是压缩文件等)
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn"  onclick="showUploadReport('<%=contextPath%>')" value ='添加附件'/><font color="red">*</font>
					</th>
				</tr>
				<tr>
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  					<%if(fileList!=null){for(int i=0;i<fileList.size();i++) { %>
	 					 <script type="text/javascript">
	 	 					 addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 				</script>
					<%}}%>
			</table> 
	
  		<!-- 添加附件 结束 -->


<table class="table_edit" align="center" id="ctm_table_id" border="1">
	<tbody>
		<tr>
			<th colspan="8"  ><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>作业实施情况</th>
		</tr>
		<tr>
			<td  align="right" id="tcmtd" nowrap="nowrap">拜访公司名称：</td>
			<td  align="left" colspan="3">
				<input id="companyname" name="companyname" value="" type="text" size="50" maxlength="50"/> 
			</td>
			
				<td align="right" >意向等级判定/等级进阶：</td>
				<td  align="left" colspan="3">
					<input id="intent" name="intent" value="" type="text" size="20"  maxlength="20"/> 
			   </td>
	
		</tr>
		<tr>
		<td>序号</td>
		<td>项目</td>
		<td colspan="6">内容</td>
		</tr>
		<tr>
		<td>1</td>
		<td colspan="7">网络开发潜客等级：O级、进入面试程序。H级、距离进入面试程序1个月。A级、距离进入面试程序3个月，B级、距离进入面试程序6个月。C级、备份资源</td>
		</tr>
	
		<tr >
			<td rowspan ="7">2</td>
		</tr>
		
		<tr>	
			<td rowspan ="6" >拜访单位基本情况介绍</td>
		</tr>
		<tr>	
			<td >公司性质</td>
			<td  align="left">
				<input id="companyxz" name="companyxz" value="" type="text" size="20"  maxlength="20"/> 
			</td>
			<td nowrap="nowrap" >所在商圈</td>
			<td  align="left" colspan="5">
				<input id="shanquan" name="shanquan" value="" type="text" size="20"  maxlength="20"/> 
			</td>
		</tr>
			<tr>	
			<td >现有经营汽车品牌及其他主营业务</td>
			<td  align="left" colspan="5">
				<input id="carbuesess" name="carbuesess" value="" type="text" size="100"  maxlength="100"/> 
			</td>
		</tr>
		<tr>	
			<td nowrap="nowrap">受访人姓名</td>
			<td  align="left">
				<input id="vistname" name="vistname" value="" type="text" size="20"  maxlength="20"/> 
			</td>
			<td >职务</td>
			<td  align="left">
				<input id="vistzw" name="vistzw" value="" type="text" size="20"  maxlength="20"/> 
			</td>
			<td >联系方式</td>
			<td  align="left">
				<input id="vistphone" name="vistphone" value="" type="text" size="20"  maxlength="20"/> 
			</td>
		</tr>
		<tr>	
			<td nowrap="nowrap">受访人姓名</td>
			<td  align="left">
				<input id="vistnameone" name="vistnameone" value="" type="text" size="20"  maxlength="20"/> 
			</td>
			<td >职务</td>
			<td  align="left">
				<input id="vistzwone" name="vistzwone" value="" type="text" size="20"  maxlength="20"/> 
			</td>
			<td >联系方式</td>
			<td  align="left">
				<input id="vistphoneone" name="vistphoneone" value="" type="text" size="20"  maxlength="20"/> 
			</td>
		</tr>
		<tr>
			<td  >公司地址</td>
			<td  align="left" colspan="7">
				<input id="companyaddress" name="companyaddress" value="" type="text" size="100"  maxlength="100"/> 
			</td>
		</tr>
		
		<tr>
			<td>3</td>
			<td>经销商洽谈情况</td>
			<td colspan="6">
			<textarea rows="10" cols="100" width="100%" id="dealertalk" name="dealertalk">
			
			</textarea>
			
			</td>
		</tr>
		
		<tr>
			<td rowspan ="4">4</td>
		</tr>
		<tr>
			<td rowspan ="3">后期跟进计划</td>
		</tr>
		<tr>
			<td width="6%" align="right">下次跟进时间：</td>
					<td width="22%">
						<div align="left">
							<input name="next_follow_date" id="next_follow_date" readonly="readonly" value="" type="text" class="short_txt"  group="startDate,endDate" />
							<input id="next_follow_datetwo" name="next_follow_datetwo" style="margin-left: -4px;" class="time_ico" type="button" onclick="changeEvent()";  />
						</div>
					</td>
					<td width="6%" align="right">跟进方式：</td>
					<td width="22%" colspan="5">
						<input  id="follow_type" name="follow_type" value="" type="text" size="20"  maxlength="20"/>
		      			
					</td>
		</tr>
		<tr>
			<td >谈判推进内容</td>
			<td  align="left" colspan="7">
				<input id="context" name="context" value="" type="text" size="100"  maxlength="100"/> 
			</td>
			
		</tr>
			
</tbody></table>

<div id="ConfirmVehicleTable0" style="font-size: 13px;color: black"></div>
<div id="ConfirmVehicleTable1" style="font-size: 13px;color: black"></div>
<div id="ConfirmVehicleTable2" style="font-size: 13px;color: black"></div>
<div id="ConfirmVehicleTable3" style="font-size: 13px;color: black"></div>
<div id="ConfirmVehicleTable4" style="font-size: 13px;color: black"></div>
<div  style="font-size: 13px;color: black">
<input id="" type="button" onclick="addRow()" value="新增" />
<input id="" type="button" onclick="deleRow()" value="删除" />
</div>

</div>
<div id="ynewbody">

</div>
<table class="table_query" id="submitTable">
	<tbody><tr align="center">
		<td>		
			<input type="button" id="re_Id" value="保存" class="normal_btn" onclick="save();"/> 
			<input type="button" value="返 回" class="normal_btn" onclick="toGoBack();"/> 
		</td>
	</tr>
</tbody></table>
</form>
</div>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar25", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar21", "topbar")</script>
<script type="text/javascript">

var snum=0;
function addRow() {
	var addtable = document.getElementById("ConfirmVehicleTable"+snum);

	var str ="<table class='table_edit' align='center' id='ctm_table_id' border='1'>";

	
	str  +="<tbody>";
	str  +="<tr>";
	str  +="<th colspan='8'  ><img class='nav' src='<%=contextPath%>/img/subNav.gif'/>作业实施情况</th>";
	str  +="</tr>";

	str  +="<tr><td  align='right' id='tcmtd' nowrap='nowrap'>拜访公司名称：</td>";
	str +="<td  align='left' colspan='3'>";
	str +="	<input id='companyname"+snum+"' name='companyname"+snum+"' value='' type='text' size='50' datatype='0,is_textarea,50' maxlength='50'/>"; 
	str +="</td>";
	
	str +="	<td align='right'  >意向等级判定/等级进阶：</td>";
	str +="<td  align='left' colspan='3'>";
	str +="<input id='intent"+snum+"' name='intent"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>"; 
	str +="</td>";
	str +="</tr>";



	str +="<tr>";
	str +="<td>序号</td>";
	str +="<td>项目</td>";
	str +="<td colspan='6'>内容</td>";
	str +="</tr>";
	str +="<tr>";
	str +="<td>1</td>";
	str +="<td colspan='7'>网络开发潜客等级：O级、进入面试程序。H级、距离进入面试程序1个月。A级、距离进入面试程序3个月，B级、距离进入面试程序6个月。C级、备份资源</td>";
	str +="</tr>";

	str +="<tr >";
	str +=	"<td rowspan ='7'>2</td>";
	str +="</tr>";
	
	str +="<tr>";	
	str +=	"<td rowspan ='6'>拜访单位基本情况介绍</td>";
	str +="</tr>";
	str +="<tr>";	
	str +=	"<td >公司性质</td>";
	str +=	"<td  align='left'>";
	str +=		"<input id='companyxz"+snum+"' name='companyxz"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>"; 
	str +=	"</td>";
	str +=	"<td nowrap='nowrap'>所在商圈</td>";
	str +=	"<td  align='left' colspan='5'>";
	str +=		"<input id='shanquan"+snum+"' name='shanquan"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>"; 
	str +=	"</td>";
	str +="</tr>";
	str +=	"<tr>";	
	str +=	"<td >现有经营汽车品牌及其他主营业务</td>";
	str +=	"<td  align='left' colspan='5'>";
	str +=		"<input id='carbuesess"+snum+"' name='carbuesess"+snum+"' value='' type='text' size='100' datatype='0,is_textarea,50' maxlength='100'/>"; 
	str +=	"</td>";
	str +="</tr>";
	str +="<tr>";	
	str +=	"<td nowrap='nowrap' >受访人姓名</td>";
	str +=	"<td  align='left'>";
	str +=		"<input id='vistname"+snum+"' name='vistname"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>"; 
	str +=	"</td>";
	str +=	"<td >职务</td>";
	str +=	"<td  align='left'>";
	str +=		"<input id='vistzw"+snum+"' name='vistzw"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>"; 
	str +=	"</td>";
	str +=	"<td >联系电话</td>";
	str +=	"<td  align='left'>";
	str +=		"<input id='vistphone"+snum+"' name='vistphone"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>"; 
	str +=	"</td>";
	str +="</tr>";
	str +="<tr>";	
	str +=	"<td nowrap='nowrap'>受访人姓名</td>";
	str +=	"<td  align='left'>";
	str +=		"<input id='vistnameone"+snum+"' name='vistnameone"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>"; 
	str +=	"</td>";
	str +=	"<td >职务</td>";
	str +=	"<td  align='left'>";
	str +=		"<input id='vistzwone"+snum+"' name='vistzwone"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>"; 
	str +=	"</td>";
	str +=	"<td >联系电话</td>";
	str +=	"<td  align='left'>";
	str +=		"<input id='vistphoneone"+snum+"' name='vistphoneone"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>"; 
	str +=	"</td>";
	str +="</tr>";
	str +="<tr>";
	str +=	"<td >公司地址</td>";
	str +=	"<td  align='left' colspan='7'>";
	str +=		"<input id='companyaddress"+snum+"' name='companyaddress"+snum+"' value='' type='text' size='100' datatype='0,is_textarea,50' maxlength='100'/>"; 
	str +=	"</td>";
	str +="</tr>";
	
	str +="<tr>";
	str +=	"<td>3</td>";
	str +=	"<td>经销商洽谈情况</td>";
	str +=	"<td colspan='6'>";
	str +=	"<textarea rows='10' cols='100' width='100%' id='dealertalk"+snum+"' name='dealertalk"+snum+"'>";

	str +=		"</textarea>";
		
	str +=		"</td>";
	str +=	"</tr>";


	str +="<tr>";
	str +="<td rowspan ='4'>4</td>";
	str +="</tr>";
	str +="<tr>";
	str +="<td rowspan ='3'>后期跟进计划</td>";
	str +="</tr>";
	str +="<tr>";
	str +="<td width='6%' align='right'>下次跟进时间：</td>";
	str +=		"<td width='22%'>";
	str +=			"<div align='left'>";
	str +=				"<input name='next_follow_date"+snum+"' id='next_follow_date"+snum+"' readonly='readonly' value='' type='text' class='short_txt' datatype='1,is_date,10' group='startDate,endDate' />";
	str +=				"<input id='next_follow_datetwo"+snum+"' name='next_follow_datetwo"+snum+"' style='margin-left: -4px;' class='time_ico' type='button' onclick='changeEvent1("+snum+")';  />";
	str +=			"</div>";
	str +=		"</td>";
	
	str +=		"<td width='6%' align='right'>跟进方式：</td>";
	str +=		"<td width='22%' colspan='5'>";		
	str += 		"<input  id='follow_type"+snum+"' name='follow_type"+snum+"' value='' type='text' size='20' datatype='0,is_textarea,50' maxlength='20'/>";
		
	str +=		"</td>";
	str +="</tr>";
	str +="<tr>";
	str +="<td >谈判推进内容</td>";
	str +="<td  align='left' colspan='7'>";
	str +=	"<input id='context"+snum+"' name='context"+snum+"' value='' type='text' size='100' datatype='0,is_textarea,50' maxlength='100'/>"; 
	str +="</td>";
	
	str +="</tr>";
	str  +="</tbody>";
	str +="</table>";
	addtable.innerHTML = str;
	snum+=1;
}

function deleRow() {
	if(snum==0){
		MyAlert("没有可删除的作业实施情况！");
		return;
		}
	snum=snum-1;
	var deletable = document.getElementById("ConfirmVehicleTable"+snum);
	deletable.innerHTML ="";
	
}






	//欲购车型内容清空
	function clrTxt(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	
	//添加物料信息，获取物料id MATERIAL_ID
	function materialShow(){
		showMaterial2('materialCode','materialId',' ','false',''); 
	}
	//作业项目选择一个
	function chooseOne(chk){ 
		//先取得同name的chekcBox的集合物件 
		var obj = document.getElementsByName("operatItem"); 
		for (i=0; i<obj.length; i++){ 
		//判斷obj集合中的i元素是否為cb，若否則表示未被點選 
		if (obj[i]!=chk) obj[i].checked = false; 
		//若要至少勾選一個的話，則把上面那行else拿掉，換用下面那行 
		else obj[i].checked = true; 
		} 
		} 
	//作业方式选择一个
	function chooseOne1(chk){ 
		//先取得同name的chekcBox的集合物件 
		var obj = document.getElementsByName("operatWay"); 
		for (i=0; i<obj.length; i++){ 
		//判斷obj集合中的i元素是否為cb，若否則表示未被點選 
		if (obj[i]!=chk) obj[i].checked = false; 
		//若要至少勾選一個的話，則把上面那行else拿掉，換用下面那行 
		else obj[i].checked = true; 
		} 
		} 

			
	//检测是否选择了checkBox
	function isSelectCheckBox(cbArray){
		if(cbArray!=null && cbArray.length>0){
			for(var i=0;i<cbArray.length;i++){
				if(cbArray[i].checked)
					return true;
			}
		}else{
			return false;
		}
		return false;
	}
	//试乘试驾新增信息保存
	function save(){
		var province = document.getElementById("province").value;//省份
		var mubiao = document.getElementById("mubiao").value;//目标市场
		var drivedate = document.getElementById("drivedate").value;//出差日期
		var drivename = document.getElementById("drivename").value;//出差人
		var drivemd = document.getElementById("drivemd").value;//出差目的
		var amwork = document.getElementById("amwork").value;//上午工作
		var pmwork=document.getElementById("pmwork").value; //下午工作
		var selectArray=document.getElementsByName("operatItem");
		var operatItem = '';
		var operatWay= '';
			for(var i=0;i<selectArray.length;i++) {
				if(selectArray[i].checked) {
					selectArray[i].checked=true;
					operatItem = operatItem + selectArray[i].value+",";
				}
			}
			var selectArray=document.getElementsByName("operatWay");
			var originType = '';
				for(var i=0;i<selectArray.length;i++) {
					if(selectArray[i].checked) {
						selectArray[i].checked=true;
						operatWay = operatWay+ selectArray[i].value+",";
					}
				}

			var tab =document.getElementById("fileUploadTab");
			var rows = tab.rows.length;
			if(rows==1){
				 MyAlert("请上传附件信息！");
				 return;
				 }
			var uploadFileName = document.getElementById("uploadFileName").value;//上传附件的名称
			if(uploadFileName==null||uploadFileName=="")
			{
				MyAlert("请上传附件信息！");
				return;
			}

				var url = "<%=contextPath%>/crm/travel/NetSpeciaTravel/doSave.json?operatItem="+operatItem+"&operatWay="+operatWay+"&snum="+snum;
				makeFormCall(url, doTask, "fm") ;
				function doTask(json) {
					MyAlert("保存成功!");
			
					$('fm').action = "<%=contextPath%>/crm/travel/NetSpeciaTravel/doInit.do";
					$('fm').submit();
					
				}
		
	

	
	}

	//返回
	function toGoBack() {
		window.location = "<%=contextPath%>/crm/travel/NetSpeciaTravel/doInit.do";

	}
	
	var dd1 = new Date(); 
	var data= dd1.Format("yyyy-M-d");
	function changeEvent(){
		var nextDate=document.getElementById("next_follow_datetwo");
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data));
		
	}
	function changeEvent1(s){
		var nextDate=document.getElementById("next_follow_datetwo"+s);
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date'+s, false,data));
		
	}
</script>
</body>
</html>
