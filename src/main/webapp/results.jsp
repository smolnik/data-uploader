<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Data processing - results</title>
<link rel="stylesheet" type="text/css" href="simple.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	var fetchAndDisplayStateInterval;

	var resultReceived = false;
	
	function createXmlHttp() {
		var xmlHttpReq;
		if (window.XMLHttpRequest) {
			xmlHttpReq = new XMLHttpRequest();
		} else {
			xmlHttpReq = new ActiveXObject("Microsoft.xmlHttp");
		}
		return xmlHttpReq;
	}
	
	function fetchAndDisplayState() {
		xmlHttpReq = createXmlHttp();
		xmlHttpReq.onreadystatechange = function() {
			if (xmlHttpReq.readyState == 4 && xmlHttpReq.status == 200) {
				rt = xmlHttpReq.responseText;
				if (!resultReceived && "Completed" == rt) {
					fetchAndDisplayResults();
					resultReceived = true;
					clearInterval(fetchAndDisplayStateInterval);
				}
				document.getElementById("state").innerHTML = rt;
				
			}
		}
		xmlHttpReq
				.open(
						"GET",
						"getstate?objectKey=<%=URLEncoder.encode(request.getParameter("objectKey"), "UTF-8")%>&workflowId=<%=URLEncoder.encode(request.getParameter("workflowId"), "UTF-8")%>&runId=<%=URLEncoder.encode(request.getParameter("runId"), "UTF-8")%>", true);
		xmlHttpReq.send();
	}

	function fetchAndDisplayResults() {
		xmlHttpReq = createXmlHttp();
		xmlHttpReq.onreadystatechange = function() {
			if (xmlHttpReq.readyState == 4 && xmlHttpReq.status == 200) {
				document.getElementById("results").innerHTML = xmlHttpReq.responseText;
			}
		}
		xmlHttpReq.open("GET", "results?objectKey=<%=URLEncoder.encode(request.getParameter("objectKey"), "UTF-8")%>",
						true);
		xmlHttpReq.send();
	}

	fetchAndDisplayStateInterval = setInterval(function() {
		fetchAndDisplayState();
	}, 1000);
</script>
</head>
<body>
	<div class="center">
		<div style="width: 800px">
			<div class="border" style="width: 760px">
				<div style="margin-left: 10px">
					Processing state for workflowId <span style="font-weight: bold;">${param.workflowId}</span>:&nbsp;<span id="state"></span>
				</div>
			</div>
			<div class="border" style="width: 760px">
				<div style="margin-left: 10px">Results:</div>
				<div style="margin-left: 20px" id="results">Still awaiting...</div>
			</div>
			<div style="text-align: right;">
				<a href="<%=application.getContextPath()%>">Back</a>
			</div>
		</div>
	</div>
</body>
</html>