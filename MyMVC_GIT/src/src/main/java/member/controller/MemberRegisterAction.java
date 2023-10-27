package member.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.domain.MemberVO;
import member.model.*;

public class MemberRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" 또는 "POST" 
		
		if("GET".equalsIgnoreCase(method)) {
        //  super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberRegister.jsp");
		}
		
		else {
			String name = request.getParameter("name");
			String userid = request.getParameter("userid");
			String pwd = request.getParameter("pwd");
			String email = request.getParameter("email");
			String hp1 = request.getParameter("hp1");
			String hp2 = request.getParameter("hp2");
			String hp3 = request.getParameter("hp3");
			String postcode = request.getParameter("postcode");
			String address = request.getParameter("address");
			String detailaddress = request.getParameter("detailaddress");
			String extraaddress = request.getParameter("extraaddress");
			String gender = request.getParameter("gender");
			String birthday = request.getParameter("birthday");
			
			String mobile = hp1+hp2+hp3;
			
			MemberVO member = new MemberVO(userid, pwd, name, email, mobile, postcode, address, detailaddress, extraaddress, gender, birthday);  
			
			MemberDAO mdao = new MemberDAO_imple();
		
			// === 회원가입이 성공되어지면 "회원가입 성공" 이라는 alert 를 띄우고 시작페이지로 이동한다. === //
		/*	
			String message = "";
			String loc = "";
			
			try {
				int n = mdao.registerMember(member);
				
				if(n==1) {
					message = "회원가입 성공";
					loc = request.getContextPath()+"/index.up"; // 시작페이지로 이동한다.
				}
			} catch(SQLException e) {
				message = "SQL구문 에러발생";
				loc = "javascript:history.back()"; // 자바스크립트를 이용한 이전페이지로 이동하는 것. 
				e.printStackTrace();
			}
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
		//	super.setRedirect(false); 
			super.setViewPage("/WEB-INF/msg.jsp");
		*/	
			
		// #### 회원가입이 성공되어지면 자동으로 로그인 되도록 하겠다. ### //
		   try {
				int n = mdao.registerMember(member);
				
				if(n==1) {
				   request.setAttribute("userid", userid); 
				   request.setAttribute("pwd", pwd); 
					
				   super.setRedirect(false);
				   super.setViewPage("/WEB-INF/login/memberRegister_after_autoLogin.jsp");
				}
			} catch(SQLException e) {
				e.printStackTrace();
				
				String message = "SQL구문 에러발생";
				String loc = "javascript:history.back()"; // 자바스크립트를 이용한 이전페이지로 이동하는 것. 
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false); 
				super.setViewPage("/WEB-INF/msg.jsp");
			}	
			
		}
			
	}

}
