<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map" %>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商维护</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{  
    var dl=document.getElementById("DEALERLEVEL").value;
    if(dealerLevel==dl)
    {
        document.getElementById("sJDealerCode").disabled="true";
        //document.getElementById("sJDealerCode").disabled="true";
        //document.getElementById("orgCode").disabled="";
        document.getElementById("orgCode").disabled="";
        
    }else
    {
        document.getElementById("sJDealerCode").disabled="";
        //document.getElementById("sJDealerCode").disabled="";
        //document.getElementById("orgCode").disabled="true";
        document.getElementById("orgCode").disabled="true";     
    }
    genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县
}

function changeDealerlevel(value)
{
//  debugger;
    if(dealerLevel==value)
    {
        document.getElementById("sJDealerCode").disabled="true";
        //document.getElementById("sJDealerCode").disabled="true";
        //document.getElementById("orgCode").disabled="";
        document.getElementById("orgCode").disabled="";
        document.getElementById("sJDealerCode").value="";
        document.getElementById("sJDealerId").value="";
        
    }else
    {
        document.getElementById("sJDealerCode").disabled="";
        //document.getElementById("sJDealerCode").disabled="";
        //document.getElementById("orgCode").disabled="true";
        document.getElementById("orgCode").disabled="true";
        document.getElementById("orgCode").value="";
        document.getElementById("orgId").value="";      
    }
}


//验证输入经销商代码是否已存在
    function chkDLR(dlrCode) {
        var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/chkDlr.json" ;
        makeCall(url, printErr, {dlrCode : dlrCode}) ;
    }
    function printErr(json) {
        if(json.errInfo == 1) {
            setText("DEALER_CODE") ;
            MyAlert("输入经销商代码已存在，请重新输入") ;
        }
    }
    function setText(obj,setValue) {
        if(!setValue) {
            setValue = "" ;
        }
        document.getElementById(obj).value = setValue ;
    }
    
    
    function goBack()
    {
        document.getElementById("fm").action='<%=contextPath%>/sysmng/dealer/DealerInfo/querySalesDealerInfoInit.do';
        document.getElementById("fm").submit();
    }
    
    
    function saveDealerInfo()
    {    
        var text1=document.getElementById("txt1").value;
        var text2=document.getElementById("txt2").value;
        var DEALERTYPE=document.getElementById("DEALERTYPE").value;
        var DEALERSTATUS=document.getElementById("DEALERSTATUS").value;
        var DEALERCLASS=document.getElementById("DEALERCLASS").value;
        
        var dl=document.getElementById("DEALERLEVEL").value;
        if(DEALERTYPE==""){
            MyAlert("经销商类型不能为空！");
            return;
        }
        if(dealerLevel==dl)
        {
            var orgId=document.getElementById("orgId").value;
            if(orgId=="")
            {
                MyAlert("请选择上级组织！");
                return;
            }
        }else
        {
            var sjId=document.getElementById("sJDealerId").value;
            if(sjId=="")
            {
                MyAlert("请选择上级经销商！");
                return; 
            }
        }
        var companyId=document.getElementById("COMPANY_ID").value;
        if(companyId=="")
        {
            MyAlert("请选择经销商公司！");
            return; 
        }
/*      if(DEALERTYPE==""){
            MyAlert("经销商类型不能为空！");
            return;
        } */
        if(DEALERSTATUS==""){
            MyAlert("经销商状态不能为空！");
            return;
        }
        //if(DEALERCLASS==""){
            //MyAlert("经销商评级不能为空！");
            //return;
        //}
        if(text1==""){
                MyAlert('省份输入不能为空！');
                return;
        }
         if (<%=Constant.DEALER_TYPE_DVS%> != DEALERTYPE)
        {
            //结算等级
            var BALANCE_LEVEL=document.getElementById("BALANCE_LEVEL").value;
            //开票等级
            var INVOICE_LEVEL=document.getElementById("INVOICE_LEVEL").value;
            if ("" == BALANCE_LEVEL)
            {
                MyAlert('请选择结算等级！');
                return;
            }
            if ("" == INVOICE_LEVEL)
            {
                MyAlert('请选择开票等级！');
                return;
            }
        } 
         if (<%=Constant.DEALER_TYPE_DVS%> != DEALERTYPE)
        {
            //结算等级
            var BALANCE_LEVEL=document.getElementById("BALANCE_LEVEL").value;
            //开票等级
            var INVOICE_LEVEL=document.getElementById("INVOICE_LEVEL").value;
            if ("" == BALANCE_LEVEL)
            {
                MyAlert('请选择结算等级！');
                return;
            }
            if ("" == INVOICE_LEVEL)
            {
                MyAlert('请选择开票等级！');
                return;
            }
        } 
         //整车销售判断
         if (<%=Constant.DEALER_TYPE_DVS%> == DEALERTYPE || <%=Constant.DEALER_TYPE_DP%> == DEALERTYPE)
        {
        	 var SALE_BILLING_TYPE = document.getElementById("SALE_BILLING_TYPE").value;
        	 var SALE_BILLING_UNIT = document.getElementById("SALE_BILLING_UNIT").value;
        	 var SALE_TAX_NO = document.getElementById("SALE_TAX_NO").value;
        	 var SALE_BANK = document.getElementById("SALE_BANK").value;
        	 var SALE_ACCOUNT = document.getElementById("SALE_ACCOUNT").value;
             if ("" == SALE_BILLING_TYPE)
             {
                 MyAlert('请选择开票类型！');
                 return;
             }
             if ("" == SALE_BILLING_UNIT)
             {
                 MyAlert('请填写开票单位！');
                 return;
             }
             if ("" == SALE_TAX_NO)
             {
                 MyAlert('请填写纳税人识别号！');
                 return;
             }
             if ("" == SALE_BANK)
             {
                 MyAlert('请填写开户行！');
                 return;
             }
             if ("" == SALE_ACCOUNT)
             {
                 MyAlert('请填写账号！');
                 return;
             }
        }
         //售后服务判断
         if (<%=Constant.DEALER_TYPE_DWR%> == DEALERTYPE || <%=Constant.DEALER_TYPE_DP%> == DEALERTYPE)
        {
        	 var AFTE_BILLING_TYPE=document.getElementById("AFTE_BILLING_TYPE").value;
        	 var AFTE_BILLING_UNIT=document.getElementById("AFTE_BILLING_UNIT").value;
        	 var AFTE_TAX_NO=document.getElementById("AFTE_TAX_NO").value;
        	 var AFTE_BANK=document.getElementById("AFTE_BANK").value;
        	 var AFTE_ACCOUNT=document.getElementById("AFTE_ACCOUNT").value;
        	 if ("" == AFTE_BILLING_TYPE)
             {
                 MyAlert('请选择开票类型！');
                 return;
             }
             if ("" == AFTE_BILLING_UNIT)
             {
                 MyAlert('请填写开票单位！');
                 return;
             }
             if ("" == AFTE_TAX_NO)
             {
                 MyAlert('请填写纳税人识别号！');
                 return;
             }
             if ("" == AFTE_BANK)
             {
                 MyAlert('请填写开户行！');
                 return;
             }
             if ("" == AFTE_ACCOUNT)
             {
                 MyAlert('请填写账号！');
                 return;
             }
        }
        
        
        if(submitForm('fm'))
        {
            //sendAjax('<%=contextPath%>/sysmng/dealer/DealerInfo/checkDealerCode.json',showResultCodeCheck,'fm')
            MyConfirm("确认添加新的经销商信息吗?", submitDealer) ;
        }
    }
    
    function submitDealer(){
        document.getElementById("fm").action= '<%=contextPath%>/sysmng/dealer/DealerInfo/saveSalesDealerInfo.do';
        document.getElementById("fm").submit();
    }
   /*  $("table #DEALERTYPE").change(function(){
    	MyAlert("选中的值为："+$(this).val());
    }); */ 
    $(function(){
    	$("#DEALERTYPE").change(function(){
            var dataname = $(this).val();
            if (dataname == "10771001"){
            	$("#Sale").show();
            	$("#afterSales").hide();  
            }
            if (dataname == "10771002"){
            	$("#afterSales").show();
            	$("#Sale").hide();  
            }
            if (dataname == "10771005"){
            	$("#Sale").show();  
            	$("#afterSales").show();
            }
        });
    });
    
    

</script>
</head>
<body>
<div class="wbox" id="wbox" >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;新增经销商</div>
 <form method="post" name = "fm" id="fm">
 <input type="hidden" id="COMPANY_ID" name="COMPANY_ID" />
 <input type="hidden"  name="orgId"  value=""  id="orgId" />
 <input type="hidden"  name="sJDealerId"  value=""  id="sJDealerId" />
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
          <tr>
            <td class="right">经销商类型：</td>
            <td colspan = "5">
                <label>
                    <script type="text/javascript">
                        genSelBoxExp("DEALERTYPE",<%=Constant.DEALER_TYPE%>,"",true,"","onchange='dealerType(); '","false","<%=Constant.DEALER_TYPE_JSZX%>");
                    </script>
                    <!-- <select id="DEALERTYPE" name="DEALERTYPE" class="u-select" >
                        <option selected="" value="">-请选择-</option>
                        <option value="10771001" title="整车销售">整车销售</option>
                        <option value="10771002" title="售后服务">售后服务</option>
                        <option value="10771005" title="一体化">一体化</option>
                    </select> -->
                </label>
                <span style="font-size: 9pt; color: red; padding-left: 2px; height: 18px;">*</span>
            </td>
            <!-- <td> </td>
            <td> </td>
            <td> </td>
            <td> </td> -->
          </tr>
          <tr>
            <td class="right">经销商代码：</td>
            <td>
                  <input type='text'  class="middle_txt" name="DEALER_CODE"  id="DEALER_CODE" datatype="0,is_name,20"  value="" maxlength="20" onchange="chkDLRA(this.value);"/>
            </td>
            
            <td class="right">经销商名称：</td>
            <td>
                  <input type='text'  class="middle_txt" name="DEALER_NAME"  id="DEALER_NAME" datatype="0,is_null,150"  value="" maxlength="150"/>
            </td>
            <td class="right">经销商简称：</td>
             <td>
                  <input type='text'  class="middle_txt" name="SHORT_NAME"  id="SHORT_NAME" datatype="0,is_null,75"  value="" maxlength="75"/>
             </td>
          </tr>
           
           <tr>
             

            <td class="right">经销商公司：</td>
            <td>
                <input class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" value="" readonly="readonly" onclick="showCompany('<%=contextPath %>')"/>
                <%-- <input class="mark_btn" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/> --%>
            </td>
            <td class="right">经销商状态：</td>
            <td>
                  <label>
                        <script type="text/javascript">
                            genSelBoxExp("DEALERSTATUS",<%=Constant.STATUS%>,"",true,"","","false",'');
                        </script>
                  </label>
            </td>
            <td class="right">经销商等级：</td>
             <td>
                  <label>
                    <script type="text/javascript">
                        genSelBoxExp("DEALERLEVEL",<%=Constant.DEALER_LEVEL%>,"",'',"","onchange='changeDealerlevel(this.value); '","false",'');
                    </script>
                  </label>
            </td>
          </tr>
          <tr>
             
            <td class="right">上级组织：</td>
            <td>
                <input type="text"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" readonly="readonly" onclick="showOrg('orgCode','orgId','false','')"/>
                <!-- <input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','orgId','false','')" value="&hellip;" /> -->
            </td>
            <td class="right">上级经销商：</td>
            <td>
                <input type="text"  name="sJDealerCode" size="15" value=""  id="sJDealerCode" class="middle_txt" readonly="readonly" onclick="showOrgDealer('sJDealerCode','sJDealerId','false','','true')"/>
                <!-- <input name="dealerbu"  id="dealerbu" type="button" class="mark_btn" onclick="showOrgDealer('sJDealerCode','sJDealerId','false','','true')" value="&hellip;" /> -->
            </td>
            <td class="right">维修资源：</td>
            <td>
                <label>
                        <script type="text/javascript">
                            genSelBoxExp("MAIN_RESOURCES",<%=Constant.MAIN_RESOURCES%>,"",true,"",'',"false",'');
                        </script>
                </label>
            </td>
          </tr>
          <tr>
              <td class="right">省份：</td>
              <td>
                   <select class="u-select" id="txt1" name="province" onchange="_regionCity(this,'txt2')"></select> 
              </td>    
              <td class="right">地级市：</td>
              <td>
                   <select class="u-select" id="txt2" name="city" onchange="_regionCity(this,'txt3')"></select>
              </td>  
              <td class="right" nowrap="nowrap">区/县：</td>
              <td nowrap="nowrap">
                   <select class="u-select" id="txt3" name="COUNTIES"></select>
              </td>
          </tr>
          <tr> 
             <td class="right" nowrap="nowrap">乡：</td>
             <td  nowrap="nowrap"> 
                <input type="text"  class="middle_txt" id="TOWNSHIP" name="TOWNSHIP" value=""/>
             </td>
             <td class="right">邮编：</td>
             <td>
                   <input type="text"  class="middle_txt" name="zipCode"  id="zipCode" value="" maxlength="10" datatype="1,is_digit_letter,30" />
             </td>
             <td class="right">联系人：</td>
             <td>
                   <input type="text"  class="middle_txt" name="linkMan"  id="linkMan"  datatype="1,is_name,50" value="" maxlength="10"/>
             </td>
         </tr>
         <tr>
              <td class="right">电话：</td>
              <td>
                   <input type="text"  class="middle_txt" name="phone"  id="phone" datatype="1,is_null,100" value="" maxlength="25"/>
              </td> 
              <td class="right">传真：</td>
              <td>
                   <input type="text"  class="middle_txt" name="faxNo"  id="faxNo" value=""  datatype="1,is_null,50" maxlength="25"/>
              </td>
              <td class="right">Email：</td>
              <td>
                   <input type="text"  class="middle_txt" name="email"  id="email" datatype="1,is_email,100" value="" maxlength="100"/>
              </td>
         </tr>
          <!-- <tr>
          <td class="right">联系人手机：</td>
          <td><input type="text"  class="middle_txt" name="linkManPhone"  id="linkManPhone"  datatype="1,is_name,50" value="<c:out value="${map.LINK_MAN_PHONE}"/>" maxlength="25"/></td>
          <td class="right"></td>
          <td></td> 
         </tr> -->
    <tr>
        <td class="right" nowrap="nowrap">经销商评级：</td>
        <td nowrap="nowrap"> 
        <label>
                <script type="text/javascript">
                    genSelBoxExp("DEALERCLASS",<%=Constant.DEALER_CLASS_TYPE%>,"",false,"",'',"false",'');
                </script>
        </label>
        </td>
        <td class="right">结算等级：</td>
        <td>
            <label>
                <script type="text/javascript">
                    genSelBoxExp("BALANCE_LEVEL",<%=Constant.BALANCE_LEVEL%>,"",true,"","","false",'');
                </script>
            </label>
        </td>
        <td class="right">开票等级：</td>
        <td>
        <label>
                <script type="text/javascript">
                    genSelBoxExp("INVOICE_LEVEL",<%=Constant.INVOICE_LEVEL%>,"",true,"","","false",'');
                </script>
            </label>
        </td>

    </tr>
    <tr>
        
        <td class="right">法人：</td>
        <td>
           <input type="text"  class="middle_txt" name="LEGAL"  id="LEGAL" value=""/>
        </td>
        <td class="right" nowrap="nowrap">站长电话：</td>
        <td nowrap="nowrap"> 
            <input type="text"  class="middle_txt" id="WEBMASTER_PHONE" name="WEBMASTER_PHONE" value=""/>
        </td>
        <td class="right">值班电话：</td>
        <td>
            <input type="text"  class="middle_txt" name="DUTY_PHONE"  id="DUTY_PHONE" value=""/>
        </td>
    </tr>

    <tr style="display:none">
        <td class="right">系统开通时间：</td>
        <td>
            <label>
                    ${map.CREATE_DATE}
            </label>
        </td>
        <!-- <td> </td>
        <td> </td> -->
    </tr>
    <!-- <tr>
        <td class="right" nowrap="nowrap">行政级别：</td>
        <td nowrap="nowrap"> 
        <label>
                <script type="text/javascript">
                    genSelBoxExp("ADMIN_LEVEL",<%=Constant.ADMIN_LEVEL%>,"${map.ADMIN_LEVEL}",true,"",'',"false",'');
                </script>
        </label>
        </td>
        <td class="right">形象等级：</td>
        <td>
        <label>
                <script type="text/javascript">
                    genSelBoxExp("IMAGE_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${map.IMAGE_LEVEL}",true,"",'',"false",'');
                </script>
        </label>
        </td>
    </tr> -->
          <tr>
            <td class="right">详细地址：</td>
            <td>
                <textarea name="address" id="address" cols="40" rows="2" datatype="1,is_textarea,50" >
                </textarea>
            </td>
 
          </tr>
          <tr>
           <td class="right">备注：</td>
            <td>
                <textarea name="remark" id="remark" cols="40" rows="2" datatype="1,is_textarea,1000">
                </textarea>
            </td>
          </tr>        
         
     </table> 
      <div id="Sale" style="display:none;">
          <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />经销商开票信息</div>
	     	<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		     	<tr>
			     	<td class="right">开票类型：</td>
		            <td >
		                <label>
		                    <select id="SALE_BILLING_TYPE" name="SALE_BILLING_TYPE" class="u-select" >
		                        <option selected="" value="">-请选择-</option>
		                        <option value="92701001" title="增值税专用发票">增值税专用发票</option>
		                        <option value="92701002" title="增值税普通发票">增值税普通发票</option>
		                    </select>
		                </label>
		                <span style="font-size: 9pt; color: red; padding-left: 2px; height: 18px;">*</span>
		            </td>
		            <td class="right" nowrap="nowrap">开票单位：</td>
	        		<td>
	        			<input type="text"  class="middle_txt" name="SALE_BILLING_UNIT" id="SALE_BILLING_UNIT" datatype="0,is_textarea,20" value="" maxlength="20"/>
	        		</td> 
	        		<td class="right" nowrap="nowrap">纳税人识别号：</td>
			        <td nowrap="nowrap"> 
			            <input type="text"  class="middle_txt" id="SALE_TAX_NO" name="SALE_TAX_NO" value=""  datatype="0,is_textarea,30" />
			        </td>
		     	</tr>
		     	<tr>
		        	<td class="right" nowrap="nowrap">开户行：</td>
        			<td nowrap="nowrap"> 
            			<input type="text"  class="middle_txt" id="SALE_BANK" name="SALE_BANK" value=""/>
        			</td>
        			<td class="right">账号：</td>
			        <td>
			            <input type="text"  class="middle_txt" name="SALE_ACCOUNT"  id="SALE_ACCOUNT" value=""/>
			        </td>
			        <td class="right">开票地址：</td>
            		<td>
                		<textarea name="SALE_BILLING_ADDRESS" id="SALE_BILLING_ADDRESS" cols="40" rows="2" datatype="1,is_textarea,50" >
                		</textarea>
            		</td>  
		     	</tr>
	     	</table>
      </div>
      <div id="afterSales" style="display:none;">
          <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />售后开票信息</div>
	     	<table border="0" align="center" cellpadding="0" cellspacing="0"  class="table_query">
		     	<tr>
			     	<td class="right">开票类型：</td>
		            <td >
		                <label>
		                    <select id="AFTE_BILLING_TYPE" name="AFTE_BILLING_TYPE" class="u-select" >
		                        <option selected="" value="">-请选择-</option>
		                        <option value="92701001" title="增值税专用发票">增值税专用发票</option>
		                        <option value="92701002" title="增值税普通发票">增值税普通发票</option>
		                    </select>
		                </label>
		                <span style="font-size: 9pt; color: red; padding-left: 2px; height: 18px;">*</span>
		            </td>
		            <td class="right" nowrap="nowrap">开票单位：</td>
	        		<td>
	        			<input type="text"  class="middle_txt" name="AFTE_BILLING_UNIT" id="AFTE_BILLING_UNIT" datatype="0,is_textarea,20" value="" maxlength="20"/>
	        		</td> 
	        		<td class="right" nowrap="nowrap">纳税人识别号：</td>
			        <td nowrap="nowrap"> 
			            <input type="text"  class="middle_txt" id="AFTE_TAX_NO" name="AFTE_TAX_NO" value=""  datatype="0,is_textarea,30" />
			        </td>
		     	</tr>
		     	<tr>
		        	<td class="right" nowrap="nowrap">开户行：</td>
        			<td nowrap="nowrap"> 
            			<input type="text"  class="middle_txt" id="AFTE_BANK" name="AFTE_BANK" value=""/>
        			</td>
        			<td class="right">账号：</td>
			        <td>
			            <input type="text"  class="middle_txt" name="AFTE_ACCOUNT"  id="AFTE_ACCOUNT" value=""/>
			        </td>
			        <td class="right">开票地址：</td>
            		<td>
                		<textarea name="AFTE_BILLING_ADDRESS" id="AFTE_BILLING_ADDRESS" cols="40" rows="2" datatype="1,is_textarea,50" >
                		</textarea>
            		</td>  
		     	</tr>
	     	</table>
      </div>
     <table class=table_query>
         <tr align="center">
             <td colspan="4" class="table_query_4Col_input" style="text-align: center">
                <input type="button" value="保存" name="saveBtn" class="normal_btn" onclick="saveDealerInfo();"/> 
                <input type="button" value="取消" name="cancelBtn"  class="u-button u-cancel" onclick="goBack();" />
            </td>
        </tr>
   </table>
</form>
</div>
</body>
</html>
