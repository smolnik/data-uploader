<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Data processing - launcher</title>
<link rel="stylesheet" type="text/css" href="simple.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="center">
		<div class="border" style="width: 400px">
			<div style="margin-left: 10px">Check activities to perform:</div>
			<form enctype="application/x-www-form-urlencoded" method="post" action="processinglauncher">
				<input type="text" name="objectKey" value="${param.objectKey}" readonly="readonly"> <br> 
				<input type="checkbox" name="actionTypes" value="NOTIFY_ON_START">Notify on workflow start <br>
				<input type="checkbox" name="actionTypes" value="IMPORT">Import <br>
				<input type="checkbox" name="actionTypes" value="DETECT">Detect <br>
				<input type="checkbox" name="actionTypes" value="DIGEST">Make SHA-256 digest <br>
				<input type="checkbox" name="actionTypes" value="EXTRACT">Extract items from zip <br>
				<input type="checkbox" name="actionTypes" value="NOTIFY_ON_FINISH">Notify on workflow finish <br>
				<input type="submit" value="Process">
			</form>
			<div style="font-size: 12px; margin: 10px">*Some activities implicitly require others to be accomplished prior to them. E.g. for the Extraction activity the
				workflow makes Import and Detection activities run beforehand. The actual flow is driven by the workflow implementation.</div>
		</div>
	</div>
</body>
</html>