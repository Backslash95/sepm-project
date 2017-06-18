<%@ page session="false" %>
<html>
<head>
<title>Upload File Request Page</title>
</head>
<body>
	<form method="POST" action="uploadFile" enctype="multipart/form-data">
		<p>File has to be in Format: username;Firstname;Lastname;password</p>
		File to upload: <input type="file" name="file"> 
		<input type="submit" value="Upload"> Press here to upload the file!
	</form>	
</body>
</html>