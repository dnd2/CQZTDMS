/**2013-7-11 rj create*/
function myBlur(target){
	if(target.value==""){
		showMyErrMsg(target,'此处不能为空！');
	}
}
function showMyErrMsg(obj,errmsg) {
	var tipid = getTip();
	showTip(obj,errmsg,tipid);
}
function disabledButton(buttonObj,type){
	var arr=buttonObj;
	for(var i=0;i<buttonObj.length;i++){
		document.getElementById(buttonObj[i]).disabled=type;
	}
}

/**
 * 页面加载大区下拉列表
 * 需要在页面上引入
 * <select id="__large_org" name="__large_org" onchange="changeOrg(this.value)"></select>
 */
function loadOrgList() {
	makeCall(g_webAppName + '/common/AjaxSelectAction/getOrgList.json', function(json){
		if (!json.Exception) {
			var orgb = document.getElementById("__large_org");
			orgb.options.add(new Option('==请选择==',''));
			for(var i=0;i<json.info.length;i++) {
				var soption = new Option(json.info[i].ORG_NAME,json.info[i].ORG_ID+'');
				orgb.options.add(soption);
			}
		}
	},{level:2});
}

/**
 * 省份联动，配合loadOrgList使用
 * <select id="__province_org" name="orgs"><option value="">==请选择==</option></select>
 * @param orgid
 */
function changeOrg(orgid) {
	makeCall(g_webAppName + '/common/AjaxSelectAction/getOrgList.json', function(json){
		if (!json.Exception) {
			var orgs = document.getElementById("__province_org");
			var slength = orgs.options.length;
			for(var i=0;i<slength;i++) orgs.options.remove(1);
			//orgs.options.add(new Option('==请选择==',''));
			for(var i=0;i<json.info.length;i++) {
				var soption = new Option(json.info[i].ORG_NAME,json.info[i].ORG_ID+'');
				orgs.options.add(soption);
			}
		}
	},{level:3,orgid:orgid});
}