package egovframework.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.mapper.ArticleMapper;
import egovframework.service.ArticleService;
import egovframework.vo.ArticleVO;
import egovframework.vo.AttachFileVO;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleMapper articleMapper;

	// 계층형 목록 조회 + 검색 + 페이징
	@Override
	public Map<String, Object> getPagedArticleHierarchy(int page, int size, String searchType, String keyword) {
		int offset = (page - 1) * size; // 페이지의 시작점 계산, 특정 페이지 데이터를 가져오기 위해 건너뛸 데이터의 수 입니다. 페이지 - 1 * 10 
		int totalArticles = articleMapper.countArticlesWithSearch(searchType, keyword); // 총 게시글 수, 검색타입과, 검색어가 없으면 전체조회합니다.
		List<ArticleVO> articles = articleMapper.getPagedArticleHierarchy(offset, size, searchType, keyword);

		int totalPages = (int) Math.ceil((double) totalArticles / size); // 전체 페이지 수 ceil을 사용하여 올림처리, 페이지계산
		int startPage = ((page - 1) / 10) * 10 + 1; // 시작 페이지 번호 (10페이지씩 묶습니다)
		int endPage = Math.min(startPage + 9, totalPages); // 끝 페이지 번호, min사용해서 10개가 되지않을때 끝 페이지 지정
		
		// 결과를 Map에 담아서 반환합니다.
		Map<String, Object> result = new HashMap<>();
		result.put("articles", articles);
		result.put("currentPage", page);
		result.put("totalPages", totalPages);
		result.put("startPage", startPage);
		result.put("endPage", endPage);
		result.put("totalArticles", totalArticles);
		return result;
	}

	// 게시글 상세 조회
	@Override
	public ArticleVO getArticleById(int articleId) {
		return articleMapper.selectArticleById(articleId);
	}

	// 게시글 작성(답글 포함)
	@Transactional
	@Override
	public void addArticle(ArticleVO article) {
		if (article.getParentId() == null) {
			// 원글 작성
			article.setDepth(0); // 원글의 depth는 0
			articleMapper.insertArticle(article);
		} else {
			// 답글 작성
			ArticleVO parentArticle = articleMapper.selectArticleById(article.getParentId());
			article.setDepth(parentArticle.getDepth() + 1); // 부모 글의 depth + 1
			articleMapper.insertReply(article);
		}
	}

	// 게시글 수정 처리
	@Override
	public void updateArticle(ArticleVO article) {
		articleMapper.updateArticle(article);
	}

	// 게시글 삭제 처리
	@Override
	public void removeArticle(int articleId) {
		// 원글과 답변글의 ID 목록 가져오기
		List<Integer> articleIds = articleMapper.selectArticleAndRepliesIds(articleId);

		// 각 게시글 첨부파일 삭게
		for (Integer id : articleIds) {
			// 해당 게시글의 첨부파일 목록 가져오기
			List<AttachFileVO> attachFiles = articleMapper.getAttachFilesByArticleId(id);

			// 첨부파일 삭제
			for (AttachFileVO file : attachFiles) {
				articleMapper.deleteAttachFile(file.getFileId());
				// 파일 경로에서 실제 파일 삭제
				File targetFile = new File(file.getFilePath());
				if (targetFile.exists()) {
					boolean deleted = targetFile.delete();
					if (!deleted) {
						throw new RuntimeException("파일 삭제에 실패했습니다: " + file.getFilePath());
					}
				}

			}

			// 게시글 소프트 딜리트
			articleMapper.deleteArticle(articleId);
		}
	}

	// 조회수 증가
	@Override
	public void incrementViewCount(int articleId) {
		articleMapper.incrementViewCount(articleId);
	}

	// 비밀번호 확인
	@Override
	public boolean validatePassword(int articleId, String inputPassword) {
		String correctPassword = articleMapper.checkPassword(articleId);
		return correctPassword != null && correctPassword.equals(inputPassword);
	}

	// 첨부파일 저장
	@Transactional
	@Override
	public void saveAttachFile(AttachFileVO attachFile) {
		articleMapper.insertAttachFile(attachFile);
	}

	// 첨부파일 다중 가져오기
	@Override
	public List<AttachFileVO> getAttachFilesByArticleId(int articleId) {
		return articleMapper.getAttachFilesByArticleId(articleId);
	}

	// 파일 다운로드
	@Override
	public AttachFileVO getAttachFileById(int fileId) {
		return articleMapper.selectAttachFileById(fileId);
	}

	// 파일 삭제
	@Override
	public void deleteAttachFile(int fileId) {
		// DB에서 파일 정보 가져오기
		AttachFileVO file = articleMapper.selectAttachFileById(fileId);
		if (file != null) {
			// 파일 경로에서 실제 파일 삭제
			File targetFile = new File(file.getFilePath());
			if (targetFile.exists()) {
				boolean deleted = targetFile.delete();
				if (!deleted) {
					throw new RuntimeException("파일 삭제에 실패했습니다: " + file.getFilePath());
				}
			}
			// DB에서 파일정보 삭제
			articleMapper.deleteAttachFile(fileId);
		} else {
			throw new RuntimeException("파일이 존재하지 않습니다. ID: " + fileId);
		}
	}
}
