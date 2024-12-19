package egovframework.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import egovframework.service.ArticleService;
import egovframework.vo.ArticleVO;
import egovframework.vo.AttachFileVO;

@Controller
@RequestMapping("/article")
public class ArticleController {

	//커밋테스트
	@Autowired
	private ArticleService articleService;

	// 게시글 목록 조회 (계층형 + 검색 + 페이징)
	@GetMapping("/list")
	public String list(
			// 파라미터로 전달
	        @RequestParam(value = "searchType", required = false) String searchType, // 검색타입 (제목, 작성자)
	        @RequestParam(value = "keyword", required = false) String keyword, // 검색어
	        @RequestParam(value = "page", defaultValue = "1") int page, // 현재 페이지 (기본값: 1)
	        @RequestParam(value = "size", defaultValue = "10") int size, // 페이지당 글 수 (기본값: 10) 
	        Model model) {
	    
	    // 페이징 및 검색 결과 가져오기
	    Map<String, Object> result = articleService.getPagedArticleHierarchy(page, size, searchType, keyword);

	    // 모델에 데이터 추가
	    model.addAttribute("articles", result.get("articles"));
	    model.addAttribute("currentPage", result.get("currentPage"));
	    model.addAttribute("totalPages", result.get("totalPages"));
	    model.addAttribute("startPage", result.get("startPage"));
	    model.addAttribute("endPage", result.get("endPage"));
	    model.addAttribute("totalArticles", result.get("totalArticles")); // 총 게시글 수 
	    model.addAttribute("searchType", searchType);  // 검색 타입 유지
	    model.addAttribute("keyword", keyword);        // 검색어 유지

	    return "article/list";
	}
	
	// 게시글 상세 조회
	@GetMapping("/{articleId}")
	public String detail(@PathVariable int articleId, Model model) {
		articleService.incrementViewCount(articleId); // 조회수 증가
	    List<AttachFileVO> attachFiles = articleService.getAttachFilesByArticleId(articleId);
		
		model.addAttribute("article", articleService.getArticleById(articleId));
		model.addAttribute("attachFiles", attachFiles);
		return "article/detail";
	}

    // 게시글(답글포함) 작성 페이지
    @GetMapping("/write")
    public String writeForm(@RequestParam(required = false) Integer parentId, Model model) {
        model.addAttribute("parentId", parentId);  // 부모글 ID를 넘김
        return "article/write";
    }

    // 게시글(답글포함) 작성 처리
    @PostMapping("/write")
    public String write(@ModelAttribute ArticleVO article, 
                        @RequestParam("files") MultipartFile[] files) {
        // 게시글 저장
        articleService.addArticle(article); // articleId 반환됩니다. 파일저장시 외래키로 사용하기위함
        									// MyBatis의 useGeneratedKeys, keyProperty를 사용하여 자동증가키(postgresql에서 SERIAL)을 반환시킵니다.

        // 첨부파일 처리
        for (MultipartFile file : files) {
	        if (!file.isEmpty()) {
	            String fileName = file.getOriginalFilename(); // 원본 파일명
	            String uuid = UUID.randomUUID().toString(); // UUID 생성
	            String filePath = "C:\\Users\\winitech\\Desktop\\myboardfile\\" + uuid + "_" + fileName;
	
	            try {
	                // 지정된 경로에 실제 파일 저장
	                file.transferTo(new File(filePath));
	
	                // 파일 메타데이터 저장
	                AttachFileVO attachFile = new AttachFileVO();
	                attachFile.setArticleId(article.getArticleId()); // 외래키
	                attachFile.setFileName(fileName);
	                attachFile.setFilePath(filePath);
	                attachFile.setFileSize(file.getSize());
	                attachFile.setUuid(uuid);
	
	                articleService.saveAttachFile(attachFile);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
        }
        return "redirect:/article/list";
    }

	// 게시글 삭제 처리
	@PostMapping("/delete")
	public String delete(@RequestParam int articleId, 
	                     @RequestParam String password, 
	                     Model model) {
	    if (articleService.validatePassword(articleId, password)) {
	        articleService.removeArticle(articleId);
	        return "redirect:/article/list";
	    } else {
	        model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
	        model.addAttribute("article", articleService.getArticleById(articleId));
	        return "article/detail";
	    }
	}

	// 게시글 수정 페이지 진입
	@PostMapping("/editForm")
	public String editForm(@RequestParam int articleId, 
	                       @RequestParam String password, 
	                       Model model) {
	    if (articleService.validatePassword(articleId, password)) {
	    	List<AttachFileVO> attachFiles = articleService.getAttachFilesByArticleId(articleId);
	    	
	        model.addAttribute("article", articleService.getArticleById(articleId));
	        model.addAttribute("attachFiles", attachFiles);
	        
	        return "article/edit";
	    } else {
	        model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
	        model.addAttribute("article", articleService.getArticleById(articleId));
	        return "article/detail";
	    }
	}
	
	// 게시글 수정 처리
	@PostMapping("/edit")
	public String edit(@ModelAttribute ArticleVO article,
			@RequestParam(value = "files", required = false) List<MultipartFile> files,
			Model model) {
	    try {
	        // 게시글을 업데이트 처리
	        articleService.updateArticle(article);
	     // 첨부파일 처리
	        for (MultipartFile file : files) {
		        if (!file.isEmpty()) {
		            String fileName = file.getOriginalFilename();
		            String uuid = UUID.randomUUID().toString();
		            String filePath = "C:\\Users\\winitech\\Desktop\\myboardfile\\" + uuid + "_" + fileName;
		
		            try {
		                // 파일 저장
		                file.transferTo(new File(filePath));
		
		                // 파일 메타데이터 저장
		                AttachFileVO attachFile = new AttachFileVO();
		                attachFile.setArticleId(article.getArticleId());
		                attachFile.setFileName(fileName);
		                attachFile.setFilePath(filePath);
		                attachFile.setFileSize(file.getSize());
		                attachFile.setUuid(uuid);
		
		                articleService.saveAttachFile(attachFile);
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
	        }
	        return "redirect:/article/" + article.getArticleId(); // 수정 후 상세 페이지로 이동
	    } catch (Exception e) {
	        // 예외 처리 시 에러 메시지 전달
	        model.addAttribute("errorMessage", "수정 처리 중 오류가 발생했습니다.");
	        model.addAttribute("article", articleService.getArticleById(article.getArticleId()));
	        return "article/edit";
	    }
	}

	// 파일 다운로드 처리
    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> downloadFile(@RequestParam("fileId") int fileId) {
        // 파일 정보를 조회합니다.
    	AttachFileVO file = articleService.getAttachFileById(fileId);

    	// 파일이 존재하지 않으면 404 응답을 보냅니다.
        if (file == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 파일 경로 가져와서 설정합니다.
        String filePath = file.getFilePath();
        File downloadFile = new File(filePath);

        // 실제 파일이 존재하지 않으면 404 응답을 보냅니다.
        if (!downloadFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 파일 리소스와 헤더 설정
        FileSystemResource resource = new FileSystemResource(downloadFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(downloadFile.length()));

        // 파일과 헤더를 포함한 ResponseEntity 반환
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    
    // 수정페이지에서 파일 한개 삭제
    @PostMapping("/deleteFile")
    @ResponseBody
    public ResponseEntity<String> deleteFile(@RequestParam int fileId) {
        try {
        	//파일 삭제 로직
            articleService.deleteAttachFile(fileId);
            
            //성공 시 응답
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
        	//실패 시 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File deletion failed");
        }
    }

}
