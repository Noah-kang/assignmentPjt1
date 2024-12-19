<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- 날짜포맷을 위한 라이브러리 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>게시글 상세</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 20px;
}

h1 {
	font-size: 24px;
	margin-bottom: 10px;
	border-bottom: none;
} /* 타이틀 아래 줄 제거 */
.container {
	width: 60%;
	margin: auto;
}

.meta {
	font-size: 14px;
	color: #666;
	margin-bottom: 10px;
}

.separator {
	border-top: 2px solid #ddd;
	margin-bottom: 15px;
}

.content {
	border: 1px solid #ddd;
	padding: 15px;
	margin-bottom: 15px;
}

.button-section {
	margin-top: 20px;
	display: flex; /* 가로 정렬 */
	align-items: center; /* 세로 중앙 정렬 */
	gap: 10px; /* 요소 사이의 간격 */
}

.button-section button, .button-section a, .button-section input[type="password"]
	{
	padding: 5px 10px;
	margin: 0; /* 버튼과 입력 필드의 기본 여백 제거 */
	text-decoration: none;
	color: white;
	background-color: #007BFF;
	border: none;
	cursor: pointer;
	display: inline-block; /* 인라인 블록 요소로 설정 */
}

.button-section input[type="password"] {
	padding: 5px 10px; /* 내부 여백 */
	height: 30px; /* 높이 설정 */
	border: 1px solid #ccc; /* 테두리 추가 */
	color: black; /* 글자 색상 */
	background-color: white; /* 배경색 설정 */
	outline: none; /* 클릭 시 파란색 외곽선 제거 */
	box-shadow: none; /* 그림자 제거 */
	border-radius: 3px; /* 모서리 둥글게 */
}

.button-section a {
	background-color: #6c757d; /* 목록 버튼의 색상 */
	color: white;
}

.button-section button:hover, .button-section a:hover {
	opacity: 0.8; /* 호버 시 투명도 효과 */
}
</style>
</head>
<body>
	<div class="container">
		<!-- 게시글 제목 -->
		<h1>${article.title}</h1>

		<!-- 작성자, 작성일, 조회수 -->
		<div class="meta">
			작성자: ${article.writer} | 작성일:
			<!-- 날짜 포맷 변경 -->
			<fmt:formatDate value="${article.createdAt}" pattern="yyyy-MM-dd" />
			| 조회수: ${article.viewCount}
		</div>

		<!-- 구분선 -->
		<div class="separator"></div>

		<!-- 게시글 내용 -->
		<div class="content">
			<p>${article.content}</p>
		</div>

		<h2>첨부파일</h2>
		<ul>
			<c:forEach var="file" items="${attachFiles}">
				<li><a href="/myboard/article/download?fileId=${file.fileId}">${file.fileName}</a>
				[<fmt:formatNumber value="${file.fileSize}" type="number" groupingUsed="true" /> byte]
				</li>
			</c:forEach>
		</ul>

		<!-- 오류 메시지 출력 -->
		<c:if test="${not empty errorMessage}">
			<div class="error-message">${errorMessage}</div>
		</c:if>

		<!-- 버튼 영역 -->
		<div class="button-section">
			<a href="/myboard/article/list" class="btn btn-secondary">목록</a>
			<form id="editForm" action="/myboard/article/editForm" method="post">
				<input type="hidden" name="articleId" value="${article.articleId}">
				<input type="password" id="password" name="password"
					placeholder="비밀번호 입력">
				<button type="button" onclick="submitEdit()">수정</button>
			</form>

			<form id="deleteForm" action="/myboard/article/delete" method="post">
				<input type="hidden" name="articleId" value="${article.articleId}">
				<input type="hidden" id="deletePassword" name="password">
				<button type="button" onclick="submitDelete()">삭제</button>
			</form>

			<button type="button"
				onclick="location.href='/myboard/article/write?parentId=${article.articleId}'">답변
				등록</button>
		</div>
	</div>
	<script>
		function submitEdit() {
			var password = document.getElementById("password").value;
			if (!password) {
				alert("비밀번호를 입력하세요.");
				return;
			}
			document.getElementById("editForm").submit();
		}

		function submitDelete() {
			var password = document.getElementById("password").value;
			if (!password) {
				alert("비밀번호를 입력하세요.");
				return;
			}
			if (confirm("정말 삭제하시겠습니까?")) {
				document.getElementById("deletePassword").value = password;
				document.getElementById("deleteForm").submit();
			}
		}
	</script>
</body>
</html>
