<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="me.kafeitu.demo.activiti.util.PropertyFileUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp"%>

	<%@ include file="/common/include-base-styles.jsp" %>
    <%@ include file="/common/include-jquery-ui-theme.jsp" %>
    <link href="${ctx }/js/common/plugins/jui/extends/portlet/jquery.portlet.min.css?v=1.1.2" type="text/css" rel="stylesheet" />
    <link href="${ctx }/js/common/plugins/qtip/jquery.qtip.css?v=1.1.2" type="text/css" rel="stylesheet" />
    <%@ include file="/common/include-custom-styles.jsp" %>
    <style type="text/css">
    	.template {display:none;}
    	.version {margin-left: 0.5em; margin-right: 0.5em;}
    	.trace {margin-right: 0.5em;}
    </style>

    <script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/extends/portlet/jquery.portlet.pack.js?v=1.1.2" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/qtip/jquery.qtip.pack.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/plugins/html/jquery.outerhtml.js" type="text/javascript"></script>
	<script src="${ctx }/js/module/activiti/workflow.js" type="text/javascript"></script>
    <script src="${ctx }/js/module/main/welcome-portlet.js" type="text/javascript"></script>
</head>
<body style="margin-top: 0.5em;">
	<div id='portlet-container'></div>

	<div id="multiInstance" class="template">
<%--			在填写<strong>会签参与人</strong>字段时使用用户的ID作为值，例如：<code>kafeitu,admin</code>表示两个用户，即两个任务实例被创建。--%>
	</div>
	<div class="project-info template">
<%--		<ul>--%>
<%--			<li><a target="_blank" href='https://github.com/henryyan/kft-activiti-demo'>kft-activiti-demo</a>为Activiti初学者快速入门所设计。</li>--%>
<%--			<li>源代码托管Github：<a target="_blank" href='https://github.com/henryyan/kft-activiti-demo'>https://github.com/henryyan/kft-activiti-demo</a></li>--%>
<%--		</ul>--%>
	</div>

	<div class="arch template">
<%--		<ul>--%>
<%--			<li>Activiti版本：${prop['activiti.version']}</li>--%>
<%--			<li>Spring版本：3.1</li>--%>
<%--			<li>使用<a href="http://maven.apache.org" target="_blank">Maven</a>管理依赖</li>--%>
<%--		</ul>--%>
	</div>


	<div class="links template">
<%--		<p>--%>
<%--			<b>Wiki：</b><a target="_blank" href="https://github.com/henryyan/kft-activiti-demo/wiki">https://github.com/henryyan/kft-activiti-demo/wiki</a>--%>
<%--		</p>--%>
<%--		<p>--%>
<%--			<b>Demo源码：</b><a target="_blank" href="https://github.com/henryyan/kft-activiti-demo">https://github.com/henryyan/kft-activiti-demo</a>--%>
<%--		</p>--%>
<%--		<p>--%>
<%--			<b>Activiti资料：</b><a target="_blank" href="http://www.kafeitu.me/categories.html#activiti-ref">http://www.kafeitu.me/categories.html#activiti-ref</a>--%>
<%--		</p>--%>
	</div>
</body>
</html>
