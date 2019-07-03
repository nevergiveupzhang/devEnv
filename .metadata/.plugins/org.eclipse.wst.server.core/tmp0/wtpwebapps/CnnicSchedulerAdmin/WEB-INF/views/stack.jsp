<%@ page import="java.util.Map" pageEncoding="UTF-8"%>
<html>
<head>
<title>服务器线程信息</title>
</head>
<body>
	<pre>
<%
	for(Map.Entry<Thread,StackTraceElement[]> stackTrace:Thread.getAllStackTraces().entrySet()){
		Thread thread=stackTrace.getKey();
		StackTraceElement [] stack=stackTrace.getValue();
		if(thread.equals(Thread.currentThread())){
			continue;
		}
		out.print("\n线程："+thread.getName()+"\n");
		for(StackTraceElement element:stack){
			out.print("\t"+element+"\n");
		}
	}
%>
</pre>
</body>
</html>