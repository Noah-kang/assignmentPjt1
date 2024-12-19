<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>게시글 수정</title>
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
	gap: 20px;
}

.row div {
	flex: 1;
}

.button-section {
	display: flex;
	justify-content: flex-end;
	gap: 10px;
	margin-top: 20px;
}

.button-section button {
	padding: 10px 20px;
	background-color: #007bff;
	color: white;
	border: none;
	cursor: pointer;
	border-radius: 4px;
	font-size: 14px;
}

.button-section button.cancel {
	background-color: #6c757d;
}

.button-section button:hover {
	opacity: 0.8;
}
</style>
</head>
<body>
	<div class="container">
		<h1>게시판 수정</h1>
		<form action="/myboard/article/edit" method="post"
			enctype="multipart/form-data">
			<!-- 게시글 ID (hidden) -->
			<input type="hidden" name="articleId" value="${article.articleId}" />

			<!-- 작성자 및 비밀번호 -->
			<div class="row">
				<div>
					<label for="writer">작성자 <span style="color: red;">*</span></label>
					<input type="text" id="writer" name="writer"
						value="${article.writer}" required />
				</div>
				<div>
					<label for="password">비밀번호 <span style="color: red;">*</span></label>
					<input type="password" id="password" name="password"
						value="${article.password}" required />
				</div>
			</div>

			<!-- 제목 -->
			<div>
				<label for="title">제목 <span style="color: red;">*</span></label> <input
					type="text" id="title" name="title" value="${article.title}"
					required />
			</div>

			<!-- 내용 -->
			<div>
				<label for="content">내용</label>
				<textarea id="content" name="content" required>${article.content}</textarea>
			</div>

			<!-- 첨부파일 -->
			<div>
				<label for="files">첨부파일:</label> <input type="file" name="files"
					id="files" multiple />
			</div>

			<ul>
				<c:forEach var="file" items="${attachFiles}">
					<li>${file.fileName}[<fmt:formatNumber
							value="${file.fileSize}" type="number" groupingUsed="true" />byte]
						<button type="button" onclick="deleteFile(${file.fileId})">삭제</button>
					</li>
				</c:forEach>
			</ul>

			<!-- 버튼 영역 -->
			<div class="button-section">
				<button type="submit">저장</button>
				<button type="button" class="cancel" onclick="history.back()">취소</button>
			</div>
		</form>
	</div>

	<script>
    function deleteFile(fileId) {
        if (confirm("정말 삭제하시겠습니까?")) {
            fetch(`/myboard/article/deleteFile?fileId=` + fileId, {
                method: 'POST'
            }).then(response => {
                if (response.ok) {
                    alert("파일이 삭제되었습니다.");
                    location.reload();
                } else {
                    alert("파일 삭제에 실패했습니다.");
                }
            });
        }
    }
</script>
</body>
</html>
