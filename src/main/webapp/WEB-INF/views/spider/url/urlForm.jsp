<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
	<head>
		<%@ include file="/common/global.jsp"%>
		<title>编辑网址</title>
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/common/include-base-styles.jsp"%>
		<%@ include file="/common/include-jquery-ui-theme.jsp"%>
		<link
			href="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.css"
			type="text/css" rel="stylesheet" />

		<script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript">
</script>
		<script
			src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js"
			type="text/javascript">
</script>
		<script
			src="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.js"
			type="text/javascript">
</script>
		<script
			src="${ctx }/js/common/plugins/jui/extends/i18n/jquery-ui-date_time-picker-zh-CN.js"
			type="text/javascript">
</script>
	</head>


	<body>

		<div class="container showgrid">
			<form:form id="inputForm" action="${ctx}/spider/url/${action}"
				method="post" class="form-horizontal">
				<input type="hidden" name="id" value="${url.id}" />
				<fieldset>
					<legend>
						<small>编辑网址</small>
					</legend>
					<table border="1">
						<tr>
							<td>
								编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
							</td>
							<td>
								<input type="text" id="code" name="code" value="${url.code}"
									size="100" />
							</td>
						</tr>
						<tr>
							<td>
								名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称:
							</td>
							<td>
								<input type="text" id="name" name="name" value="${url.name}"
									size="100" />
							</td>
						</tr>
						<tr>
							<td>
								中东西部:
							</td>
							<td>
								<select name="area" id="area">
									<option value="MIDDLE" <c:if test="${url.area=='MIDDLE'}">selected</c:if>>中部</option>
									<option value="EAST" <c:if test="${url.area=='EAST'}">selected</c:if>>东部</option>
									<option value="WEST" <c:if test="${url.area=='WEST'}">selected</c:if>>西部</option>
									<option value="OTHER" <c:if test="${url.area=='OTHER'}">selected</c:if>>其他</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								是否省会:
							</td>
							<td>
								<select name="isCapital" id="isCapital">
									<option value="0" <c:if test="${url.isCapital=='0'}">selected</c:if>>否</option>
									<option value="1" <c:if test="${url.isCapital=='1'}">selected</c:if>>是</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								级&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别:
							</td>
							<td>
								<input type="button" id="chooseLevelBtn" name="chooseLevelBtn"
									value="选择"></input>
								<span id="chooseLevel"> <c:if
										test="${url.level.id != null}">编码:${url.level.code}；名称:${url.level.name}</c:if>
								</span>
								<input type="hidden" id="level.id" name="level.id"
									value="${url.level.id}" />
							</td>
						</tr>
						<tr>
							<td>
								省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份:
							</td>
							<td>
								<input type="text" id="province" name="province"
									value="${url.province}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;委:
							</td>
							<td>
								<input type="text" id="department" name="department"
									value="${url.department}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								子&nbsp;&nbsp;&nbsp;版&nbsp;&nbsp;&nbsp;块:
							</td>
							<td>
								<input type="text" id="module" name="module"
									value="${url.module}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								板&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;块:
							</td>
							<td>
								<input type="text" id="submodule" name="submodule"
									value="${url.submodule}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								页面类型:
							</td>
							<td>
							
								<select name="catchType" id="catchType">
									<option value="NORMAL" <c:if test="${url.catchType=='NORMAL'}">selected</c:if>>普通</option>
									<option value="SCRIPT" <c:if test="${url.catchType=='SCRIPT'}">selected</c:if>>脚本</option>
								</select>
							</td>
						</tr>
						
						<tr>
							<td>
								网&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址:
							</td>
							<td>
								<input type="text" id="url" name="url" value="${url.url}"
									size="100" />
							</td>
						</tr>
						<tr>
							<td>
								前缀网址:
							</td>
							<td>
								<input type="text" id="urlPrefix" name="urlPrefix"
									value="${url.urlPrefix}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								主&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题xpath:
							</td>
							<td>
								<input type="text" id="subjPath" name="subjPath"
									value="${url.subjPath}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								替换字符:
							</td>
							<td>
								<input type="text" id="subjReplace" name="subjReplace"
									value="${url.subjReplace}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								链&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;接xpath:
							</td>
							<td>
								<input type="text" id="linkPath" name="linkPath"
									value="${url.linkPath}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期xpath:
							</td>
							<td>
								<input type="text" id="datePath" name="datePath"
									value="${url.datePath}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								替换字符:
							</td>
							<td>
								<input type="text" id="dateReplace" name="dateReplace"
									value="${url.dateReplace}" size="100" />
							</td>
						</tr>

						<tr>
							<td>
								开始序号:
							</td>
							<td>
								<input type="text" id="startBegin" name="startBegin"
									value="${url.startBegin}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								字符集:
							</td>
							<td>
								<input type="text" id="charset" name="charset"
									value="${url.charset}" size="100" />
							</td>
						</tr>
						
						<tr>
							<td>
								抓取下一页:
							</td>
							<td>
								<input type="text" id="catchNextPage" name="catchNextPage"
									value="${url.catchNextPage}" size="100" /><br/>提示：1为抓取，默认为0
							</td>
						</tr>
						<tr>
							<td>
								下一页xpath:
							</td>
							<td>
								<input type="text" id="nextPageXpath" name="nextPageXpath"
									value="${url.nextPageXpath}" size="100" />
							</td>
						</tr>
						<tr>
							<td>
								描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述:
							</td>
							<td>
								<textarea id="description" name="description"
									class="input-large">${url.description}</textarea>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
							</td>
							<td>
								<a class="startup-process" href="javascript:submit()">提交</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a class="startup-process" href="javascript:history.back()">返回</a>
							</td>
						</tr>
					</table>
				</fieldset>
			</form:form>
		</div>

		<div id="chooseLevelPanel" title="选择级别" style="display: none">
			<table>
				<thead>
					<tr>
						<td>
							序号
						</td>
						<td>
							编码
						</td>
						<td>
							名称
						</td>
						<td>
							描述
						</td>
					</tr>
					<tbody id="levelData"></tbody>
				</thead>
			</table>
		</div>

<%--		<div id="chooseGroupPanel" title="选择组别" style="display: none">--%>
<%--			<table>--%>
<%--				<thead>--%>
<%--					<tr>--%>
<%--						<td>--%>
<%--							序号--%>
<%--						</td>--%>
<%--						<td>--%>
<%--							编码--%>
<%--						</td>--%>
<%--						<td>--%>
<%--							名称--%>
<%--						</td>--%>
<%--						<td>--%>
<%--							描述--%>
<%--						</td>--%>
<%--					</tr>--%>
<%--				</thead>--%>
<%--				<tbody id="groupData"></tbody>--%>
<%--			</table>--%>
<%--		</div>--%>


	</body>
	<script type="text/javascript">
	
	/**
$('#chooseGroupPanel').dialog( {
	 autoOpen:false,
	modal : true,
	width : 500,
	height : 800
});
	*/
$('#chooseLevelPanel').dialog( {
	 autoOpen:false,
	modal : true,
	width : 500,
	height : 800
});

/////处理组别
/**
$(function() {
	$('#chooseGroupBtn').button( {
		icons : {
			primary : 'ui-icon-plus'
		}
	}).click(function() {
			getGroupData();//获取json数据  
			$("#chooseGroupPanel").dialog('open');//设置为‘open’时将显示对话框  
		});
});

function getGroupData() {
	var valid = jQuery.ajax( {
		type : 'GET',
		async : false,
		url : '${ctx}/bd/group/queryAll',
		dataType : "json",
		success : setGroupList
	});
}

function setGroupList(obj) {
	var strHTML = "";
	for ( var i = 0; i < obj.length; i++) {
		strHTML += "<tr ondblclick='setValue(1,this)' id='" + obj[i].id + "_" + obj[i].code + "_" + obj[i].name +"'>";
		strHTML += "<td>" + (i + 1) + "</td>";
		strHTML += "<td>" + obj[i].code + "</td>";
		strHTML += "<td>" + obj[i].name + "</td>";
		strHTML += "<td>" + obj[i].description + "</td>";
		strHTML += "</tr>";
	}
	$("#groupData").html(strHTML);//显示到tbody中  
}
*/
///////////处理级别
$(function() {
	$('#chooseLevelBtn').button( {
		icons : {
			primary : 'ui-icon-plus'
		}
	}).click(function() {
		getLevelData();//获取json数据  
			$("#chooseLevelPanel").dialog('open');//设置为‘open’时将显示对话框  
		});
});

function getLevelData() {
	var valid = jQuery.ajax( {
		type : 'GET',
		async : false,
		url : '${ctx}/bd/level/queryAll',
		dataType : "json",
		success : setLevelList
	});
}

function setLevelList(obj) {
	var strHTML = "";
	for ( var i = 0; i < obj.length; i++) {
		strHTML += "<tr ondblclick='setValue(2,this)' id='" + obj[i].id + "_" + obj[i].code + "_" + obj[i].name +"'>";
		strHTML += "<td>" + (i + 1) + "</td>";
		strHTML += "<td>" + obj[i].code + "</td>";
		strHTML += "<td>" + obj[i].name + "</td>";
		strHTML += "<td>" + obj[i].description + "</td>";
		strHTML += "</tr>";
	}
	$("#levelData").html(strHTML);//显示到tbody中  
}

//选中一行后双击，将值放到父页面上
function setValue(type, obj) {
	var id = obj.id;
	var str = id.split("_");
	if (2 == type) {//1为group，2为level
		document.getElementById("level.id").value = str[0];
		document.getElementById("chooseLevel").innerHTML = "编码:" + str[1]
				+ "；名称:" + str[2];
		$('#chooseLevelPanel').dialog("close");
	} else {
		document.getElementById("group.id").value = str[0];
		document.getElementById("chooseGroup").innerHTML = "编码:" + str[1]
				+ "；名称:" + str[2];
		$('#chooseGroupPanel').dialog("close");
	}
}

$(function() {
	$('.startup-process').button( {
		icons : {
			primary : 'ui-icon-play'
		}
	});
});
function submit() {
	$('#inputForm').submit();
}
</script>
</html>
