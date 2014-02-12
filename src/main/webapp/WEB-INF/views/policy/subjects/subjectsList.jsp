<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<%@ include file="/common/global.jsp"%>
		<title>政策标题</title>
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/common/include-base-styles.jsp"%>
		<%@ include file="/common/include-jquery-ui-theme.jsp"%>
		<link
			href="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.css"
			type="text/css" rel="stylesheet" />
		<link href="${ctx }/js/common/plugins/qtip/jquery.qtip.min.css"
			type="text/css" rel="stylesheet" />
		<%@ include file="/common/include-custom-styles.jsp"%>

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
		<script src="${ctx }/js/common/plugins/qtip/jquery.qtip.pack.js"
			type="text/javascript">
</script>
		<script src="${ctx }/js/common/plugins/html/jquery.outerhtml.js"
			type="text/javascript">
</script>
		<script src="${ctx }/js/module/activiti/workflow.js"
			type="text/javascript">
</script>

		<style type="text/css">
body {
	TEXT-ALIGN: center;
}

#querydiv {
	MARGIN-RIGHT: auto;
	MARGIN-LEFT: auto;
}
</style>
		<script type="text/javascript">
jQuery(function() {
	// 时间设置
	jQuery('#search_EQ_publishDate').datepicker( {
		dateFormat : "yy-mm-dd"
	});

});
</script>

		<style>
</style>
	</head>
	<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/qktags"%>

	<body>
		<c:if test="${not empty message}">
			<div class="ui-widget">
				<div class="ui-state-highlight ui-corner-all"
					style="margin-top: 20px; padding: 0 .7em;">
					<p>
						<span class="ui-icon ui-icon-info"
							style="float: left; margin-right: .3em;"></span>
						<strong>提示：</strong>${message}
					</p>
				</div>
			</div>
		</c:if>

		<div id="querydiv" align="center" style="width: 100%; margin:0px ">
			<form name="queryForm" id="queryForm" action="#">
				<table style="width: 70%; margin-bottom: 0px" align="center">
					<tr>
						<td style="background-color: white">
							<label class="ui-button-text">
								主题：
							</label>
							<input type="text" name="search_LIKE_subject"
								id="search_LIKE_subject" class="input-medium"
								value="${param.search_LIKE_subject}">
						</td>
						<td style="background-color: white">
							<label class="ui-button-text">
								发布日期：
							</label>
							<input type="text" name="search_EQ_publishDate"
								id="search_EQ_publishDate" class="input-medium"
								value="${param.search_EQ_publishDate}" readonly />

						</td>
						<td style="background-color: white">
							<label class="ui-button-text">
								省&nbsp;&nbsp;&nbsp;&nbsp;份：
							</label>
							<input type="text" name="search_LIKE_url.province"
								class="input-medium" value="${param.search_LIKE_url.province}">
						</td>
						<td style="background-color: white">
							<label class="ui-button-text">
								部门：
							</label>
							<input type="text" name="search_LIKE_url.department"
								class="input-medium" value="${param.search_LIKE_url.department}">
						</td>
					</tr>
					<tr>
						<td style="background-color: white">
							<label class="ui-button-text">
								板块：
							</label>
							<input type="text" name="search_LIKE_url.module"
								class="input-medium" value="${param.search_LIKE_url.module}">
						</td>
						<td style="background-color: white">
							<label class="ui-button-text">
								子&nbsp;&nbsp;版&nbsp;块：
							</label>
							<input type="text" name="search_LIKE_url.submodule"
								class="input-medium" value="${param.search_LIKE_url.submodule}">
						</td>
						<td style="background-color: white">
							<label class="ui-button-text">
								级&nbsp;&nbsp;&nbsp;&nbsp;别：
							</label>
							<input type="text" name="search_LIKE_level.name"
								class="input-medium" value="${param.search_LIKE_level.name}">
						</td>
						<td style="background-color: white" align="center">
							<a class="startup-process" href="javascript:querySubmit()">检索</a>
						</td>
					</tr>
				</table>
			</form>
			
		</div>
	<div style="width: 80%; margin:0px 120px 5px 120px; background-color: #C9C8B0 " align="left">
				<font style="size: 14; font-weight: bold;">关键词：</font>
				<c:set var="no" value="1"></c:set>
				<c:forEach items="${words}" var="word">
					<a href="javascript:submitWords('${word.name}')">${word.name}</a>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<c:if test="${no%15==0}">
						<br />
					</c:if>
					<c:set var="no" value="${no+1}"></c:set>
				</c:forEach>
			</div>
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>
						编码
					</th>
					<th>
						名称
					</th>
<%--					<th>--%>
<%--						省份--%>
<%--					</th>--%>
					<th>
						部委
					</th>
					<th>
						板块
					</th>
					<th>
						子版块
					</th>
					<th>
						网址
					</th>
					<th>
						政策标题
					</th>
					<th>
						发布日期
					</th>
					<%--					<th>--%>
					<%--						组别--%>
					<%--					</th>--%>
					<%--					<th>--%>
					<%--						级别--%>
					<%--					</th>--%>
					<th>
						操作
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${subjects.content}" var="subject">
					<tr>
						<td>
							${subject.url.code}
						</td>
						<td>
							${subject.url.name}
						</td>
<%--						<td>--%>
<%--							${subject.url.province}--%>
<%--						</td>--%>
						<td>
							${subject.url.department}
						</td>
						<td>
							${subject.url.module}
						</td>
						<td>
							${subject.url.submodule}
						</td>
						<td>
							${subject.url.url}
						</td>
						<td>
							<a target="_blank" href="${subject.subjUrl}">${subject.subject}</a>
						</td>
						<td>
							<fmt:formatDate pattern="yyyy-MM-dd"
								value="${subject.publishDate}" type="both" />
						</td>
						<td>
							<a href="${ctx}/policy/subjects/delete/${subject.id}">删除</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<tags:pagination page="${subjects}" paginationSize="50" />


	</body>
	<script type="text/javascript">
$(function() {
	$('.startup-process').button( {
		icons : {
			primary : 'ui-icon-play'
		}
	});
});

function querySubmit() {
	$('#queryForm').submit();
}
function submitWords(obj) {
	document.getElementById("search_LIKE_subject").value = obj;
	$('#queryForm').submit();
}
</script>
</html>
