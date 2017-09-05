/*
 * 文件名称: stree.js
 * 创建人员: 邬晓波
 * 创建时间: 2014-11-27
 * 文件说明:
 * 在DMS业务范围内实现通用的下拉树组件，主要作用是规范下拉树数据的规范(比如数据的名称、数据交换的格式等)、提供统一的调用接口等。
 * 注意：该组件是在ddlevelsmenu组件(ddlevelsmenu.js)的基础上实现的，并且需要InfoAjax.js的支持，使用本组件之前要引入这两个js文件
 *
 * 下拉树的数据必须是JSON格式，核心字段说明：
 * TREE_ID：节点ID
 * PARENT_ID:父节点ID
 * TREE_NAME：节点名称
 * TREE_LEVEL:节点当前层级
 * NEXT_COUNT:子节点数量
 */
var stree = {
	level : 1, //构建下拉树时开始循环的层级
	chars : ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'],
	
	/*
 	 * 加载下拉树的统一调用接口。
 	 * 调用示例：<a href="javascript:void();" onclic="stree.loadTree(this, '/crm/basedata/PcGroup/initData.json', 'fm');">加载下拉树</a>
 	 *
 	 * @params obj       [Object] 加载下拉树的元素对象
 	 * @params url       [String] 获取树形结构数据的路径
 	 * @params func      [Function] 选中下拉树时指定的js函数名称
 	 * @params formName  [String] 表单名称
 	 */
	loadtree : function(obj, url, func, formName) {
		//如果已经加载下拉树了就不用再次发起请求获取数据了(解决点击一次就加载解析数据而造成效率慢的问题)
		if (obj.getAttribute("isload") === "true") {
			return ;
		}
		var form = formName || "fm";
		var firstid = stree.generateMixed(8);
		stree.ajax(url, form, stree.loadCallBack(obj, firstid, func));
	},
	
	/*
	 * 加载下拉数数据成功后的回调函数
	 *
	 * @params obj [Object] 加载下拉树的元素对象
	 */
	loadCallBack : function(obj, firstid, func) {
		return function(transport) {
			var subul = document.getElementById(obj.getAttribute("rel")); //加载下拉树的容器
			var list = transport.responseText.evalJSON().dataList; //下拉树的数据源数据
			var topid = transport.responseText.evalJSON().topID;
			subul.innerHTML = "";
			
			stree.load(obj, subul, list, firstid, topid, func);
			var menuid = obj.parentNode.parentNode.parentNode.getAttribute("id"); //得到整个下拉框的容器ID
			ddlevelsmenu.setmenupointer(menuid, subul, "topbar");
			obj.setAttribute("isload", "true"); //成功解析数据以后，将是否加载的标识设置为已加载
		}
	},
	
	/*
	 * 加载数据
	 *
	 * @param obj [Object] 下拉树的点击对象
	 * @param subul [Object] 装载下拉树的容器对象
	 * @param list [JsonArray] 下拉树的数据源(json格式的数组)
	 * @param firstid [String] 下拉数据第一行li对象的ID，用于自动生成类似"--请选择--"之类的不选择的数据项
	 * @param topid [String] 组装下拉树数据的父级ID
	 * @param func [Function] 点击加载下拉树以后的回调函数，用于与调用者交付使用，该函数只有一个下拉树节点对象的参数
	 */
	load : function(obj, subul, list, firstid, topid, func) {
		subul.appendChild(stree.createFirstLi(obj, firstid, func));
		var notselected = obj.getAttribute("notselected") || ""; //获取不允许被点击的节点ID
		stree.buildtree(obj, subul, list, firstid, topid, func, notselected);
		
		//如果下拉树的文本不等于deftitle属性的值，说明已经选择了其他值，需要显示"--请选择--"
		if (obj.innerText != obj.getAttribute("deftitle")) {
			document.getElementById(firstid).style.display = "";
		}
	},
	
	/*
	 * 构建下拉树
	 *
	 * @param obj [Object] 下拉树的点击对象
	 * @param subul [Object] 装载下拉树的容器对象
	 * @param list [JsonArray] 下拉树的数据源(json格式的数组)
	 * @param firstid [String] 下拉数据第一行li对象的ID，用于自动生成类似"--请选择--"之类的不选择的数据项
	 * @param topid [String] 组装下拉树数据的父级ID
	 * @param func [Function] 点击加载下拉树以后的回调函数，用于与调用者交付使用，该函数只有一个下拉树节点对象的参数
	 * @param notselected [String] 不允许点击的下拉树节点的ID串，多个节点ID串之间用(",")隔开。比如：16501,16502
	 */
	buildtree : function(obj, subul, list, firstid, topid, func, notselected) {
		for (var i = 0; i < list.length; i++) {
			var treeid = list[i].TREE_ID; //ID
			var parentid = list[i].PARENT_ID; //父ID
			var treename= list[i].TREE_NAME; //名称
			var treelevel = parseInt(list[i].TREE_LEVEL, 10); //所属层级
			var nextcount = parseInt(list[i].NEXT_COUNT, 10); //子级数量
			var ischild = nextcount > 0; //是否有子级：true=有子级; false=没有子级
			
			//a元素的属性
			var elm_a_setting = stree.extend({
				href:"javascript:void();",
				title:treename,
				TREE_ID:treeid,
				PARENT_ID:parentid,
				TREE_NAME:treename,
				TREE_LEVEL:treelevel,
				NEXT_COUNT:nextcount
			}, ischild ? {rel:treeid} : {});
			
			var elm_li = null;
			if (parentid === topid) {
				var elm_a = stree.createElm("a", elm_a_setting, true);
				if (!stree.isExistsSelected(notselected, treeid)) {
					stree.addAttachEvent(elm_a, "click", function() {
						stree.chooseData(obj, this, firstid, func);
					});
				}
				
				elm_li = stree.createElm("li", {}, false);
				elm_li.appendChild(elm_a);
				subul.appendChild(elm_li);
			}
			
			if (parentid === topid && ischild) {
				var elm_ul = stree.createElm("ul", {
					id:treeid
				}, false);
				elm_li.appendChild(elm_ul);
				stree.buildtree(obj, elm_ul, list, firstid, treeid, func, notselected);
			}
		}
	},
	
	isExistsSelected : function(notselected, tree_id) {
		return ("," + notselected + ",").indexOf("," + tree_id + ",") != -1;
	},
	
	/*
	 * 选择数据
	 *
	 * @param parentobj [Object] 下拉树的点击对象
	 * @param obj [Object] 下拉数据行的对象
	 * @param firstid [String] 下拉数据第一行li对象的ID
	 * @param func [Function] 点击加载下拉树以后的回调函数，用于与调用者交付使用，该函数只有一个下拉树节点对象的参数
	 */
	chooseData : function(parentobj, obj, firstid, func) {
		var name = obj.getAttribute("title");
		var firsli = document.getElementById(firstid);
		if (firsli.innerText === name) {
			firsli.style.display = "none";
		} else {
			firsli.style.display = "";
		}
		parentobj.innerHTML = name;
		parentobj.setAttribute("title", name);
		ddlevelsmenu.initpointer(parentobj, "topbar");
		if (func) {
			func(obj);
		}
		var subul = document.getElementById(parentobj.getAttribute("rel"));
		ddlevelsmenu.hidemenu(subul);
	},
	
	/*
	 * 创建第一个li元素对象
	 *
	 * @param obj [Object] 下拉树的点击对象
	 * @param firstid [String] 下拉数据第一行li对象的ID
	 * @param func [Function] 点击加载下拉树以后的回调函数
	 */
	createFirstLi : function(obj, firstid, func) {
		//创建a元素对象
		var _eml_a = stree.createElm("a", {
			href : "javascript:void();",
			title : obj.getAttribute("deftitle") || obj.innerHTML,
			TREE_ID:"",
			PARENT_ID:"",
			TREE_NAME:obj.getAttribute("deftitle") || obj.innerHTML,
			TREE_LEVEL:"",
			NEXT_COUNT:0
		}, true);
		
		//绑定click事件
		stree.addAttachEvent(_eml_a, "click", function() {
			stree.chooseData(obj, this, firstid, func);
		});
		
		//创建li元素对象
		var _elm_li = stree.createElm("li", {
			id : firstid
		}, false);
		
		_elm_li.appendChild(_eml_a);
		_elm_li.style.display = "none";
		
		return _elm_li;
	},
	
	/*
	 * 创建指定类型的元素
	 *
	 * @params elmtype [String] 需要创建的元素类型。比如：a、li、span等
	 * @params attrs [JSON] json格式的元素属性
	 * @params isInnerHTML [Boolean] 是否需要设置元素的innerHTML值
	 */
	createElm : function(elmtype, attrs, isInnerHTML) {
		var obj = document.createElement(elmtype);
		stree.setAttrs(obj, attrs); //设置属性
		if (isInnerHTML) {
			obj.innerHTML = attrs["title"];
		}
		return obj;
	},
	
	/*
	 * 设置指定元素对象的属性
	 *
	 * @params obj   [Object] 需要设置属性的元素对象
	 * @params attrs [JSON]   json格式的属性
	 */
	setAttrs : function(obj, attrs) {
		for (var key in attrs) {
			obj.setAttribute(key, attrs[key]);
		}
	},
	
	/*
	 * 给指定元素对象绑定事件
	 *
	 * @params obj [Object] 需要绑定事件的元素对象
	 * @params type [String] 需要绑定的事件类型(注意:不需要事件类型前的"on")。比如:click
	 * @params func [Fcuntion] 事件执行的函数
	 */
	addAttachEvent : function(obj, type, func) {
		if (obj.addEventListener) {
			obj.addEventListener(type, func, false);
		} else if (obj.attachEvent) {
			obj.attachEvent('on' + type, function() {
				return func.call(obj, window.event);
			});
		}
	},
	
	/*
	 * 生成随机码
	 *
	 * @params n [Integer] 需要生成随机码的个数
	 * @return [String] 返回指定个数的随机码
	 */
	generateMixed : function(n) {
		var res = "";
     	for(var i = 0; i < n ; i++) {
         	var id = Math.ceil(Math.random() * 35);
         	res += this.chars[id];
     	}
     	return res;
	},
	
	/*
	 * 合并json
	 *
	 * @params arg0 [JSON] 参照的json
	 * @params arg1 [JSON] 合并的json
	 * @return [JSON] 返回合并后并去重的json
	 */
	extend : function(arg0, arg1){
		var result = {};
  		for (var key in arg0) {
  			result[key] = arg0[key]
  		}
  		for (var key in arg1) {
  			result[key] = arg1[key]
  		}
  		return result;
	},
	
	ajax : function(url, formName, callback) {
		new Ajax.Request(url, {
        	method: 'post',
        	parameters: $(formName).serialize(true),
        	onFailure: function(){
            	alert('无法链接服务器！')
        	},
        	onSuccess: callback
    	});
	}
}