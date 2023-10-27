package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.ProductDAO;
import myshop.model.ProductDAO_imple;

public class MallHomeScrollAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// === Ajax(JSON) 를 사용하여 HIT 상품목록 "스크롤" 방식으로 페이징 처리해서 보여주겠다. === //
		ProductDAO pdao = new ProductDAO_imple();
		
		int totalHITCount = pdao.totalPspecCount("1"); // HIT 상품의 전체개수를 알아온다.
		
		// System.out.println("확인용 totalHITCount => " + totalHITCount );
		// 확인용 totalHITCount => 36
		
		request.setAttribute("totalHITCount", totalHITCount);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mallHomeScroll.jsp");


	}

}
