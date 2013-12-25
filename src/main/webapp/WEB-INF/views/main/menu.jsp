<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<ul id="css3menu">
	<li class="topfirst"><a rel="main/welcome">首页</a></li>
	<li>
		<a rel="#">基础数据</a>
		<ul>
		<!-- 
			<li><a rel="oa/leave/apply">请假申请(普通)</a></li>
			<li><a rel="oa/leave/list/task">请假办理(普通)</a></li>
			<li><a rel="oa/leave/list/running">运行中流程(普通)</a></li>
			<li><a rel="oa/leave/list/finished">已结束流程(普通)</a></li>
			<li><a rel="oa/leave/list/finished">其他</a></li>
			<li><a rel="quickstart/task">测试list和form</a></li>
			-->
			<li><a rel="bd/group">分组设置</a></li>
			<li><a rel="bd/level">分级设置</a></li>
			<li><a rel="bd/email">邮箱设置</a></li>
			<li><a rel="bd/keywords">关键词设置</a></li>
		</ul>
	</li>
	<li>
		<a rel="#">政策抓取</a>
		<ul>
		<!-- 
			<li><a rel="form/dynamic/process-list">流程列表(动态)</a></li>
			<li><a rel="form/dynamic/task/list">任务列表(动态)</a></li>
			<li><a rel="form/dynamic/process-instance/running/list">运行中流程表(动态)</a></li>
			<li><a rel="form/dynamic/process-instance/finished/list">已结束流程(动态)</a></li>
			 -->
			<li><a rel="spider/url">抓取网址</a></li>
			<li><a rel="spider/catchTask">抓取任务</a></li>
<%--			<li><a rel="spider/rule">抓取规则</a></li>--%>
			<li><a rel="spider/catchTaskHistory">抓取记录</a></li>
		</ul>
	</li>
	<li>
		<a rel="#">政策分析</a>
		<ul>
			<li><a rel="policy/subjects">政策查询</a></li>
			<li><a rel="form/formkey/task/list">任务列表(外置)</a></li>
			<li><a rel="form/formkey/process-instance/running/list">运行中流程表(外置)</a></li>
			<li><a rel="form/formkey/process-instance/finished/list">已结束流程(外置)</a></li>
			<li><a rel="form/formkey/process-list">抓取统计</a></li>
			<li><a rel="form/formkey/task/list">政策查询</a></li>
			<li><a rel="form/formkey/process-instance/running/list">政策分析</a></li>
			<li><a rel="form/formkey/process-instance/finished/list">已结束流程(外置)</a></li>
		</ul>
	</li>
	<li>
		<a rel='#'>流程管理</a>
		<ul>
			<li><a rel='workflow/process-list'>流程定义及部署管理</a></li>
			<li><a rel='workflow/processinstance/running'>运行中流程</a></li>
			<li><a rel='workflow/model/list'>模型工作区</a></li>
			<li><a rel='workflow/process-list'>系统日志</a></li>
		</ul>
	</li>
</ul>