<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
	<head>
		<%@ include file="/common/global.jsp"%>
		<title>编辑分级</title>
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
			<form:form id="inputForm" action="${ctx}/bd/level/${action}"
				method="post" class="form-horizontal">
				<input type="hidden" name="id" value="${level.id}" />
				<fieldset>
					<legend>
						<small>编辑分级</small>
					</legend>
					<table border="1">
						<tr>
							<td>
								编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
							</td>
							<td>
								<input type="text" id="code" name="code" value="${level.code}" />
							</td>
						</tr>
						<tr>
							<td>
								名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称:
							</td>
							<td>
								<input type="text" id="name" name="name" value="${level.name}" />
							</td>
						</tr>
						<tr>
							<td>
								直接上级:
							</td>
							<td>
								<input type="text" id="parent_id" name="parent_id"
									value="${level.parent.id}" />
							</td>
						</tr>
						<tr>
							<td>
								描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述:
							</td>
							<td>
								<textarea id="description" name="description"
									class="input-large">${level.description}</textarea>
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


	</body>
	<script type="text/javascript">
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
