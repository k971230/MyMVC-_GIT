package myshop.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import common.controller.AbstractController;
import member.domain.MemberVO;
import myshop.domain.*;
import myshop.model.*;

public class ProductRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// == 관리자(admin)로 로그인 했을 때만 제품등록이 가능하도록 한다. == //
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

		if (loginuser != null && "admin".equals(loginuser.getUserid())) {
			// 관리자(admin) 로 로그인 했을경우

			String method = request.getMethod();

			ProductDAO pdao = new ProductDAO_imple();

			if (!"POST".equals(method)) { // "GET" 이라면

				// 카테고리 목록을 조회해오기
				List<CategoryVO> categoryList = pdao.selectCategoryList();
				request.setAttribute("categoryList", categoryList);

				// SPEC 목록을 조회해오기
				List<SpecVO> specList = pdao.selectSpecList();
				request.setAttribute("specList", specList);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/myshop/admin/productRegister.jsp");

			}

			else { // "POST" 이라면
					// 새로운 제품 등록시 form 태그에서 입력한 값들을 알아오기

				/*
				 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 파일을 첨부해서 보내는 폼태그가
				 * enctype="multipart/form-data" 으로 되어었다라면 HttpServletRequest request 을 사용해서는
				 * 첨부되어지는 파일(<input type="file" />) 데이터값을 받아올 수 없다. 이때는 cos.jar 라이브러리를 다운받아
				 * 사용하도록 한 후 아래의 객체를 사용해서 데이터 값 및 첨부되어진 파일까지 받아올 수 있다.
				 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				 */
				MultipartRequest mtrequest = null;
				/*
				 * MultipartRequest mtrequest 은 HttpServletRequest request 가 하던일을 그대로 승계받아서 일처리를
				 * 해주고 동시에 파일을 받아서 업로드, 다운로드까지 해주는 기능이 있다.
				 */

				// 1. 첨부되어진 파일을 디스크의 어느 경로에 업로드 할 것인지 그 경로를 설정해야 한다.
				ServletContext svlCtx = session.getServletContext();
				String uploadFileDir = svlCtx.getRealPath("/images");

				// System.out.println("=== 첨부되어지는 이미지 파일이 올라가는 절대경로 uploadFileDir ==> " +
				// uploadFileDir);
				// === 첨부되어지는 이미지 파일이 올라가는 절대경로 uploadFileDir
				// ==>
				// C:\NCS\workspace_jsp\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\MyMVC\images

				// C:\NCS\workspace_jsp\MyMVC\src\main\webapp\images
				//String uploadFileDir = "C:\\NCS\\workspace_jsp\\MyMVC\\src\\main\\webapp\\images";
				//  위와 같이 하면 파일 업로드 후에 이클립스에서 새로고침을 해주어야 된다.  
				
				
				/*
				 * MultipartRequest의 객체가 생성됨과 동시에 파일 업로드가 이루어 진다.
				 * 
				 * MultipartRequest(HttpServletRequest request, String saveDirectory, -- 파일이 저장될
				 * 경로 int maxPostSize, -- 업로드할 파일 1개의 최대 크기(byte) String encoding,
				 * FileRenamePolicy policy) -- 중복된 파일명이 올라갈 경우 파일명다음에 자동으로 숫자가 붙어서 올라간다.
				 * 
				 * 파일을 저장할 디렉토리를 지정할 수 있으며, 업로드제한 용량을 설정할 수 있다.(바이트단위). 이때 업로드 제한 용량을 넘어서 업로드를
				 * 시도하면 IOException 발생된다. 또한 국제화 지원을 위한 인코딩 방식을 지정할 수 있으며, 중복 파일 처리 인터페이스를사용할 수
				 * 있다.
				 * 
				 * 이때 업로드 파일 크기의 최대크기를 초과하는 경우이라면 IOException 이 발생된다. 그러므로 Exception 처리를 해주어야
				 * 한다.
				 */
				// === 파일을 업로드 해준다 ===
				try {
					mtrequest = new MultipartRequest(request, uploadFileDir, 10 * 1024 * 1024, "UTF-8",
							new DefaultFileRenamePolicy());
				} catch (IOException e) {
					System.out.println("~~~ 파일업로드 실패 에러메시지 ==> " + e.getMessage());

					request.setAttribute("message", "업로드 되어질 경로가 잘못되었거나 또는 최대용량 10MB를 초과했으므로 파일업로드 실패함!!");
					request.setAttribute("loc", request.getContextPath() + "/shop/admin/productRegister.up");

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
					return; // 종료
				}

				// === 첨부 이미지 파일, 제품설명서 파일을 올렸으니 그 다음으로 제품정보를 (제품명, 정가, 제품수량,...) DB의
				// tbl_product 테이블에 insert 를 해주어야 한다. ===
				String fk_cnum = mtrequest.getParameter("fk_cnum");   // 카테고리 번호
				String pname = mtrequest.getParameter("pname");       // 제품명
				String pcompany = mtrequest.getParameter("pcompany"); // 제조사회사명

				// 업로드되어진 시스템의 첨부파일 이름(파일서버에 업로드 되어진 실제파일명)을 얻어 올때는
				// cos.jar 라이브러리에서 제공하는 MultipartRequest 객체의 getFilesystemName("form에서의 첨부파일 name명") 메소드를 사용 한다.
				// 이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
				
				String pimage1 = mtrequest.getFilesystemName("pimage1"); // 제품이미지1   이미지파일명
				String pimage2 = mtrequest.getFilesystemName("pimage2"); // 제품이미지1   이미지파일명
				
			/*
				System.out.println("확인용 pimage1 =>"+ pimage1);
				// 확인용 pimage1 =>쉐보레전면.jpg
				// 확인용 pimage1 =>쉐보레전면1.jpg
				// 확인용 pimage1 =>쉐보레전면2.jpg
				
				System.out.println("확인용 pimage2 =>"+ pimage2);
				// 확인용 pimage2 =>쉐보레후면.jpg
				// 확인용 pimage2 =>쉐보레후면1.jpg
				// 확인용 pimage2 =>쉐보레후면2.jpg
			*/
				
				String prdmanual_systemFileName = mtrequest.getFilesystemName("prdmanualFile"); // 파일서버에 업로드되어지는 실제 제품설명서 파일명 (파일명이 중복되는 것을 피하기 위해서 중복된 파일명이 있으면 파일명뒤에 숫자가 자동적으로 붙어 생성됨) 
				// 제품설명서 파일명(파일서버에 업로드 되어진 실제파일명)  
	            // 제품설명서 파일명 입력은 선택사항이므로 NULL 이 될 수 있다.
			 //	System.out.println("확인용 prdmanual_systemFileName => " + prdmanual_systemFileName);
				// 확인용 prdmanual_systemFileName => LG_싸이킹청소기_사용설명서.pdf
				// 확인용 prdmanual_systemFileName => LG_싸이킹청소기_사용설명서1.pdf
				// 확인용 prdmanual_systemFileName => LG_싸이킹청소기_사용설명서2.pdf
				
				String prdmanual_orginFileName  =  mtrequest.getOriginalFileName("prdmanualFile"); // 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올리는 제품설명서 파일명 
			 //	System.out.println("확인용 prdmanual_orginFileName => " + prdmanual_orginFileName);
				// 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올리는 제품설명서 파일명
	            // 제품설명서 파일명 입력은 선택사항이므로 NULL 이 될 수 있다.
		        // 첨부파일들 중 이것만 파일다운로드를 해주기 때문에 getOriginalFileName(String name) 메소드를 사용한다.
				// 확인용 prdmanual_orginFileName => LG_싸이킹청소기_사용설명서.pdf
				// 확인용 prdmanual_orginFileName => LG_싸이킹청소기_사용설명서.pdf
				// 확인용 prdmanual_orginFileName => LG_싸이킹청소기_사용설명서.pdf
				
				
			/*
	            <<참고>> 
	            ※ MultipartRequest 메소드

	              --------------------------------------------------
	              반환타입           설명
	            --------------------------------------------------
	            Enumeration       getFileNames()
	            
	                              업로드 된 파일들에 대한 이름을 Enumeration객체에 String형태로 담아 반환한다. 
	                              이때의 파일 이름이란 클라이언트 사용자에 의해서 선택된 파일의 이름이 아니라, 
	                              개발자가 form의 file타입에 name속성으로 설정한 이름을 말한다. 
	                              만약 업로드 된 파일이 없는 경우엔 비어있는 Enumeration객체를 반환한다.
	            
	             
	            String            getContentType(String name)
	            
	                              업로드 된 파일의 컨텐트 타입을 얻어올 수 있다. 
	                              이 정보는 브라우저로부터 제공받는 정보이다. 
	                              이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
	            
	            
	            File              getFile(String name)
	            
	                              업로드 된 파일의 File객체를 얻는다. 
	                              우리는 이 객체로부터 파일사이즈 등의 정보를 얻어낼 수 있다. 
	                              이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
	            
	            
	            String            getFilesystemName(String name)
	            
	                              시스템에 업로드되어진 파일의 이름을 반환한다.
	                              시스템에 "쉐보레전면.jpg" 가 올라가 있는데 또 사용자가 웹에서 "쉐보레전면.jpg" 파일을 올릴경우 
	                              FileRenamePolicy 에 의해 시스템에 업로드되어지는 파일명은 "쉐보레전면1.jpg" 가 되며
	                              "쉐보레전면1.jpg" 파일명을 리턴시켜주는 것이  getFilesystemName(String name) 이다.                       
	                              만약에, 이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
	            
	            
	            String            getOriginalFileName(String name)
	            
	                              중복 파일 처리 인터페이스에 의해 변환되기 이전의 파일 이름을 반환한다. 
	                              이때업로드 된 파일이 없는 경우에는 null을 반환한다.
	            
	            
	            String            getParameter(String name)
	            
	                              지정한 파라미터의 값을 반환한다. 
	                              이때 전송된 값이 없을 경우에는 null을 반환한다.
	            
	            
	            Enumeration       getParameternames()
	            
	                              폼을 통해 전송된 파라미터들의 이름을 Enumeration객체에 String 형태로 담아 반환한다. 
	                              전송된 파라미터가 없을 경우엔 비어있는 Enumeration객체를 반환한다
	            
	            
	            String[]          getparameterValues(String name)
	            
	                              동일한 파라미터 이름으로 전송된 값들을 String배열로 반환한다. 
	                              이때 전송된파라미터가 없을 경우엔 null을 반환하게 된다. 
	                              동일한 파라미터가 단 하나만 존재하는 경우에는 하나의 요소를 지닌 배열을 반환하게 된다.    
	         */
				
				String pqty = mtrequest.getParameter("pqty");             // 제품 재고량
				String price = mtrequest.getParameter("price");           // 제품 정가
				String saleprice = mtrequest.getParameter("saleprice");   // 제품 판매가(할인해서 팔 것이므로)
				String fk_snum = mtrequest.getParameter("fk_snum"); 	  // 'HIT', 'NEW', 'BEST' 에 대한 스펙번호인 시퀀스번호를 참조
				
				// !!!! 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어코드) 작성하기 !!!! // 
				String pcontent	= mtrequest.getParameter("pcontent");     // 제품설명 
				
				String point = mtrequest.getParameter("point");           // 포인트 점수     
				
				int pnum = pdao.getPnumOfProduct(); // 제품번호 채번 해오기
				
				ProductVO pvo = new ProductVO();
				pvo.setPnum(pnum);										   // 제품번호(Primary Key)
				pvo.setPname(pname);									   // 제품명
				pvo.setFk_cnum(Integer.parseInt(fk_cnum));				   // 카테고리코드(Foreign Key)의 시퀀스번호 참조
				pvo.setPcompany(pcompany); 								   // 제조회사명
				pvo.setPimage1(pimage1);								   // 제품이미지1   이미지파일명
				pvo.setPimage2(pimage2);								   // 제품이미지2   이미지파일명 
				pvo.setPrdmanual_systemFileName(prdmanual_systemFileName); // 파일서버에 업로드되어지는 실제 제품설명서 파일명 (파일명이 중복되는 것을 피하기 위해서 중복된 파일명이 있으면 파일명뒤에 숫자가 자동적으로 붙어 생성됨) 
				pvo.setPrdmanual_orginFileName(prdmanual_orginFileName);   // 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올리는 제품설명서 파일명 
				pvo.setPqty(Integer.parseInt(pqty)); 					   // 제품 재고량
				pvo.setPrice(Integer.parseInt(price)); 					   // 제품 정가
				pvo.setSaleprice(Integer.parseInt(saleprice)); 			   // 제품 판매가(할인해서 팔 것이므로)
				pvo.setFk_snum(Integer.parseInt(fk_snum)); 				   // 'HIT', 'NEW', 'BEST' 에 대한 스펙번호인 시퀀스번호를 참조
				pvo.setPcontent(pcontent); 								   // 제품설명 
				pvo.setPoint(Integer.parseInt(point)); 					   // 포인트 점수
				
				
				// tbl_product 테이블에 제품정보 insert 하기
				String message = "";
				String loc = "";
				try {
					int n = pdao.productInsert(pvo);
					
					// === 추가이미지파일이 있다라면 tbl_product_imagefile 테이블에 제품의 추가이미지 파일명 insert 해주기 === // 
                   String attachCount = mtrequest.getParameter("attachCount");
                   // attachCount 가 추가이미지 파일의 개수이다. "" "0" ~ "10"
                   
                   int n_attachCount = 0;
                   
                   if(!"".equals(attachCount)) {
                      
                      n_attachCount = Integer.parseInt(attachCount);
                      
                   }
                   
                   // 첨부파일의 파일명(파일서버에 업로드 되어진 실제파일명) 알아오기
                   if(n==1 && n_attachCount > 0) {
                	   Map<String, String> paraMap = new HashMap<>();
                	   paraMap.put("pnum", String.valueOf(pnum));
                	   // pnum 은 위에서 채번해온 제품번호이다.
                	   
                	   for(int i=0; i<n_attachCount; i++) {
                		   String attachFileName = mtrequest.getFilesystemName("attach"+i); // 파일서버에 업로드되어지는 실제 제품설명서 파일명 (파일명이 중복되는 것을 피하기 위해서 중복된 파일명이 있으면 파일명뒤에 숫자가 자동적으로 붙어 생성됨) 
                		   paraMap.put("attachFileName", attachFileName);
                		   
                		   // tbl_product_imagefile 테이블에 제품의 추가이미지 파일명 insert 하기
                		   pdao.product_imagefile_insert(paraMap);
                		   
                	   }// end of for-----------------
                   }
					
					
					message = "제품등록 성공!!";
					loc = request.getContextPath()+"/shop/mallHomeMore.up";
					
				} catch(SQLException e) {
					e.printStackTrace();
					message = "DB장애로 인해 제품등록 실패!!";
					loc = request.getContextPath()+"/shop/admin/productRegister.up";
				}
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setViewPage("/WEB-INF/msg.jsp");
				
			}

		}

		else {
			// 로그인을 안한 경우 또는 일반사용자로 로그인 한 경우
			String message = "관리자만 접근이 가능합니다.";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			// super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
	      
	      
		
	}

}
