package login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;

public class VerifyCertificationAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		
		if("POST".equalsIgnoreCase(method)) {
			
			String userCertificationCode = request.getParameter("userCertificationCode");
			String userid = request.getParameter("userid");
			
			
			HttpSession session = request.getSession();
			String certification_code = (String) session.getAttribute("certification_code");
			
			String message = "";
			String loc ="";
			
			if(certification_code.equals(userCertificationCode)){
				message = "인증성공 되었습니다.";
				loc = request.getContextPath()+"/login/pwdUpdateEnd.up?userid="+userid;
			}
			else {
				message ="발급된 인증코드가 아닙니다.\\n 인증코드를 다시 발급받으세요.!!";
				loc = request.getContextPath()+"/login/pwdFind.up";	
			}
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			// !!! 중요 !!!
			// !!! 세션에 저장된 인증코드 삭제하기 !!! //
			session.removeAttribute("certification_code");
			
			
			
		} // end of if("POST".equalsIgnoreCase(method)) -----
		
		
		
	}

}
