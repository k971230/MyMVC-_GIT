package test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class Test3Controller extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setAttribute("center", "쌍용강북교육센터");
		
	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/test/test3.jsp");
		
	}

}
