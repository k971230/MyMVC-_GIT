package test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class Test1Controller extends AbstractController {

	@Override
	public String toString() {
		return "@@@ 클래스 Test1Controller 의 인스턴스 메소드 toString() 호출함 @@@"; 
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setAttribute("name", "서영학");
		
	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/test/test1.jsp");
		
	}
	
}
