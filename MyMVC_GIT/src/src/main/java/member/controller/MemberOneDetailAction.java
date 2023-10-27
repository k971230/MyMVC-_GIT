package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.domain.*;
import member.model.*;

public class MemberOneDetailAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// === 관리자(admin)로 로그인 했을때만 조회가 가능하도록 한다. === //
		HttpSession session = request.getSession();
		
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser != null && "admin".equals(loginuser.getUserid()) ) {
			// 관리자로 로그인 했을 경우
			String method = request.getMethod(); // "GET" 또는 "POST"
			
			
			if("POST".equals(method)) {
				// POST 방식일 경우
				String userid = request.getParameter("userid");
				String goBackURL = request.getParameter("goBackURL");
				
				// System.out.println("확인용 goBackURL => " + goBackURL);
				// 기대값 => /member/memberList.up?searchType=name&searchWord=%EB%AF%BC%EC%A4%80&sizePerPage=5
				
				MemberDAO mdao = new MemberDAO_imple();
				MemberVO mvo = mdao.selectOneMember(userid);
				
				request.setAttribute("mvo", mvo);
				request.setAttribute("goBackURL", goBackURL);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/member/admin/memberOneDetail.jsp");
				
			}
			
		}
		
	} // end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception

}
