package login.controller;

import java.util.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class PwdFindAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" 또는 "POST"
		
		if("POST".equalsIgnoreCase(method)) {
			// 비밀번호 찾기 모달창에서 "찾기" 버튼을 클릭했을 경우
			String userid = request.getParameter("userid");
			String email = request.getParameter("email");
			
			MemberDAO mdao = new MemberDAO_imple();
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("email", email);
			
			boolean isUSerExist = mdao.isUserExist(paraMap);
			
			////////////////////////////////////////////////
			boolean sendMailSuccess = false; // 메일이 정상적으로 전송되었는지 유무를 알아오기 위한 용도
			
			if(isUSerExist) {
				
				//회원으로 존재하는 경우
				
				// 인증키를 랜덤하게 생성하도록 한다.
				Random rnd = new Random();
				
				String certification_code = "";
				// 인증키는 영문소문자 5글자 + 숫자 7글자로 만들겠습니다.
				
				char randchar = ' ';
				for(int i=0; i<5; i++) {
				/*
                	min 부터 max 사이의 값으로 랜덤한 정수를 얻으려면 
                	int rndnum = rnd.nextInt(max - min + 1) + min;
                    영문 소문자 'a' 부터 'z' 까지 랜덤하게 1개를 만든다.     
				*/   
					randchar = (char) (rnd.nextInt('z' - 'a' + 1)+ 'a');
					certification_code += randchar;
				} // end of for -----
				
				int randnum = 0;
	            for(int i=0; i<7; i++) {
	               /*
	                   min 부터 max 사이의 값으로 랜덤한 정수를 얻으려면 
	                   int rndnum = rnd.nextInt(max - min + 1) + min;
	                   영문 소문자 'a' 부터 'z' 까지 랜덤하게 1개를 만든다.     
	               */   
	               randnum = rnd.nextInt(9 - 0 + 1) + 0;
	               certification_code += randnum;
	            } // end of for ---------------------
				System.out.println("인증해라");
				System.out.println(certification_code);
				// 인증해라 vmfpg0411275
				
				// 랜덤하게 생성한 인증코드(certification_code)를 비밀번호 찾기를 하는 사용자의 email로 전송시킨다.
				GoogleMail mail = new GoogleMail();
				
				try {
					mail.send_certification_code(email, certification_code);
					sendMailSuccess = true; // 메일 전송이 실패했음을 기록함
				
					//세션불러오기
					HttpSession session = request.getSession();
		            session.setAttribute("certification_code", certification_code);
		            // 발급한 인증코드들 세션에 저장시킴.
				
				} catch(Exception e) {
					// 메일 전송이 실패한 경우
					e.printStackTrace();
					sendMailSuccess = false; // 메일 전송이 실패했음을 기록함
					System.out.println("tlqkf");
				}
				////////////////////////////////////////////////
			} /// end of if(isUSerExist)  -----
			

			request.setAttribute("isUSerExist", isUSerExist);
			request.setAttribute("userid", userid);
			request.setAttribute("email", email);
			request.setAttribute("sendMailSuccess", sendMailSuccess);
			
		}// end of if("POST".equalsIgnoreCase(method)){}------
		
		request.setAttribute("method", method);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/pwdFind.jsp");
		
	}

}
