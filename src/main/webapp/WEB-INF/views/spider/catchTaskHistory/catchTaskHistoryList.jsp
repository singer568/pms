<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<%@ include file="/common/global.jsp"%>
		<title>抓取任务</title>
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

		<div id="querydiv">
			<form name="queryForm" id="queryForm" action="#">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label class="ui-button-text">
					编码：
				</label>
				<input type="text" name="search_LIKE_code" class="input-medium"
					value="${param.search_LIKE_code}">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label class="ui-button-text">
					名称：
				</label>
				<input type="text" name="search_LIKE_name" class="input-medium"
					value="${param.search_LIKE_name}">
				<a class="startup-process" href="javascript:querySubmit()">检索</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</form>
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
					<th>
						开始时间
					</th>
					<th>
						结束时间
					</th>
					<th>
						时长
					</th>
					<th>
						状态
					</th>
					<th>
						日志
					</th>
					<th>
						操作
					</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${catchTaskHistorys.content}" var="catchTaskHistory">
					<tr>
						<td>
							<a href="${ctx}/spider/catchTaskHistory/update/${catchTaskHistory.id}">${catchTaskHistory.task.code}</a>
						</td>
						<td>
							${catchTaskHistory.task.name}
						</td>
						<td>
							${catchTaskHistory.startDate}
						</td>
						<td>
							${catchTaskHistory.endDate}
						</td>
						<td>
							${catchTaskHistory.duration}
						</td>
						<td>
							${catchTaskHistory.status}
						</td>
						<td>
							${catchTaskHistory.logInfo}
						</td>
						<td>
							<a href="${ctx}/spider/catchTaskHistory/delete/${catchTaskHistory.id}">明细</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<tags:pagination page="${catchTaskHistorys}" paginationSize="5" />


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
</script>
</html>
