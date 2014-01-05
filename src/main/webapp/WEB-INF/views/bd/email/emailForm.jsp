<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
	<head>
		<%@ include file="/common/global.jsp"%>
		<title>编辑邮箱</title>
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/common/include-base-styles.jsp"%>
		<%@ include file="/common/include-jquery-ui-theme.jsp"%>
		<%@ include file="/common/include-custom-styles.jsp" %>		

		<script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
		<script	src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
	</head>


	<body>

		<div class="container showgrid">
			<form:form id="inputForm" action="${ctx}/bd/email/${action}"
				method="post">
				<input type="hidden" name="id" value="${email.id}" />
				<fieldset>
					<legend>
						<small>编辑邮箱</small>
					</legend>
					<table border="1">
						<tr>
							<td>
								编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
							</td>
							<td>
								<input type="text" id="code" name="code" value="${email.code}" />
							</td>
						</tr>
						<tr>
							<td>
								名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称:
							</td>
							<td>
								<input type="text" id="name" name="name" value="${email.name}" />
							</td>
						</tr>
						<tr>
							<td>
								邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱:
							</td>
							<td>
								<input type="text" id="email" name="email"
									value="${email.email}" />
							</td>
						</tr>
							<tr>
							<td>
								发送SMTP:
							</td>
							<td>
								<input type="text" id="host" name="host"
									value="${email.host}" />
							</td>
						</tr>
							<tr>
							<td>
								用户名:
							</td>
							<td>
								<input type="text" id="userName" name="userName"
									value="${email.userName}" />
							</td>
						</tr>
							<tr>
							<td>
								密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
							</td>
							<td>
								<input type="text" id="pwd" name="pwd"
									value="${email.pwd}" />
							</td>
						</tr>
							<tr>
							<td>
								主&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题:
							</td>
							<td>
								<input type="text" id="subject" name="subject"
									value="${email.subject}" />
							</td>
						</tr>
						<tr>
							<td>
								邮件正文:
							</td>
							<td>
								<input type="text" id="emailContent" name="emailContent"
									value="${email.emailContent}" />
							</td>
						</tr>
						<tr>
							<td>
								描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述:
							</td>
							<td>
								<textarea id="description" name="description"
									class="input-large">${email.description}</textarea>
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
