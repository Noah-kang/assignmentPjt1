<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>게시판 등록</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 0;
}

.container {
	width: 80%;
	margin: auto;
	padding: 20px;
	box-sizing: border-box;
}

h1 {
	font-size: 24px;
	color: #333;
	margin-bottom: 20px;
	border-bottom: 2px solid #ddd;
	padding-bottom: 10px;
}

form {
	display: flex;
	flex-direction: column;
	gap: 15px;
}

label {
	display: block;
	margin-bottom: 5px;
	font-weight: bold;
}

input[type="text"], input[type="password"], textarea {
	width: 100%;
	padding: 10px;
	box-sizing: border-box;
	border: 1px solid #ddd;
	border-radius: 4px;
}

textarea {
	resize: vertical;
	height: 150px;
}

.row {
	display: flex;
	align-items: center;
	gap: 20px;
}

.row div {
	flex: 1;
}

.button-section {
	display: flex;
	justify-content: flex-end;
	gap: 10px;
	margin-top: 10px;
}

.button-section button {
	padding: 10px 20px;
	background-color: #007bff;
	border: none;
	color: white;
	font-size: 14px;
	cursor: pointer;
	border-radius: 4px;
}

.button-section button:hover {
	opacity: 0.8;
}

.button-section button.reset {
	background-color: #6c757d;
}
</style>
</head>
<body>
	<div class="container">
		<h1>게시판 등록</h1>
		<form action="/myboard/article/write" method="post"
			enctype="multipart/form-data">
			<!-- 부모 아이디 히든 -->
			<input type="hidden" name="parentId" value="${parentId}">
			<!-- 작성자 및 비밀번호 -->
			<div class="row">
				<div>
					<label for="writer">작성자 <span style="color: red;">*</span></label>
					<input type="text" id="writer" name="writer" required />
				</div>
				<div>
					<label for="password">비밀번호 <span style="color: red;">*</span></label>
					<input type="password" id="password" name="password" required />
				</div>
			</div>

			<!-- 제목 -->
			<div>
				<label for="title">제목 <span style="color: red;">*</span></label> <input
					type="text" id="title" name="title" required />
			</div>

			<!-- 내용 -->
			<div>
				<label for="content">내용</label>
				<textarea id="content" name="content" rows="10" required></textarea>
			</div>

			<!-- 다중 파일 업로드 -->
			<div>
			<label for="files">첨부파일</label> <input type="file" name="files"
				id="files" multiple>
			</div>
			

			<!-- 버튼 -->
			<div class="button-section">
				<button type="submit">저장</button>
				<button type="button" class="cancel" onclick="history.back()">취소</button>
			</div>
		</form>
	</div>
</body>
</html>
