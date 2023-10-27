<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<% 
	String ctxPath = request.getContextPath();
%>

<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/test.css" />

<style>
.error{
display:none;
}
div.hideInput {
display:none;
}
</style>
<script type="text/javascript">
$(function(){
	//$(".error").hide();
})
$(document).ready(function(){
/*
	$("#spanUserid").click(function(){
		$("div#useridInput").show();
	})
	
	$("#spanPwd").click(function(){
		$("div#pwdInput").show();
	})
	
	$("#spanPwdCheck").click(function(){
		$("div#pwdCheckInput").show();
	})
	
	$("#spanEmail").click(function(){
		$("div#emailInput").show();
		
	})
	
	$("#spanPhone").click(function(){
		$("div#phoneInput").show();
	})
	
	$("#spanName").click(function(){
		$("div#nameInput").show();
	})
	*/
});

function showInputTag(tagName){
	$("div#"+tagName).show();
}

function inputClose(inputTag) {
	
	$("input#"+inputTag).val("");
	$("input#"+inputTag).focus();
	
}

</script>

</head>
<body>
	<div id="container">
		<form>
			<div class="div1">
				<label for="name" onclick="showInputTag('nameInput');">
				<span style="font-size: 12pt;">이름</span> <span style="font-size: 12pt; color: #cccccc;">실명 입력</span>
				</label>
				<div id="nameInput" class="hideInput">
					<input type="text" name="name" id="name" class="inputFocus" maxlength="10" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('name')">&times;</button>
				</div>
				<p class="error">실명을 입력해 주세요.</p>
			</div>
		
			<div class="div1">
				<label for="userid" onclick="showInputTag('useridInput');">
				<span style="font-size: 12pt;" >아이디</span> <span style="font-size: 12pt; color: #cccccc;">영문 또는 영문+숫자 조합 4~12자</span>
				</label>
				<div id="useridInput" class="hideInput">
					<input type="text" name="userid" id="userid" class="inputFocus" maxlength="12" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('userid')">&times;</button>
				</div>
				<p class="error">영문 또는 영문+숫자 조합 4~12자로 입력해주세요.</p>
			</div>
			
			<div class="div1">
				<label for="pwd" onclick="showInputTag('pwdInput');">
				<span style="font-size: 12pt;">비밀번호</span> <span style="font-size: 12pt; color: #cccccc;">영문 대.소문자+숫자+특수문자 조합 8~15자</span>
				</label>
				<div id="pwdInput" class="hideInput">
					<input type="password" name="pwd" id="pwd" class="inputFocus" maxlength="15" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('pwd')">&times;</button>
				</div>
				<p class="error">영문 대.소문자+숫자+특수문자 조합 8~15자로 입력해 주세요.</p>
			</div>
			
			<div class="div1">
				<label for="pwdCheck" onclick="showInputTag('pwdCheckInput');">
				<span style="font-size: 12pt;">비밀번호 확인</span> <span style="font-size: 12pt; color: #cccccc;">비밀번호 재입력</span>
				</label>
				<div id="pwdCheckInput" class="hideInput">
					<input type="password" id="pwdCheck" class="inputFocus" maxlength="15" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('pwdCheck')">&times;</button>
				</div>
				<p class="error">비밀번호가 일치하지 않습니다</p>
			</div>
			
			<div class="div1">
				<label for="email" onclick="showInputTag('emailInput');">
				<span style="font-size: 12pt;">이메일</span> <span style="font-size: 12pt; color: #cccccc;">이메일 형식에 맞게 입력</span>
				</label>
				<div id="emailInput" class="hideInput">
					<input type="text" name="email" id="email" class="inputFocus" maxlength="30" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('email')">&times;</button>
				</div>
				<p class="error">주소가 올바르지 않습니다.</p>
			</div>
			
			<div class="div1">
				<label for="phone" onclick="showInputTag('phoneInput');">
				<span style="font-size: 12pt;">휴대폰번호</span> <span style="font-size: 12pt; color: #cccccc;">하이픈(-) 없이 입력</span>
				</label>
				<div id="phoneInput" class="hideInput">
					<input type="text" name="phone" id="phone" class="inputFocus" maxlength="11" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('phone')">&times;</button>
				</div>
				<p class="error">정확히 입력해 주세요.</p>
			</div>
			
			<div class="div1">
				<label for="postcode" onclick="showInputTag('postcodeInput');">
				<span style="font-size: 12pt;">우편번호</span> <span style="font-size: 12pt; color: #cccccc;">하이픈(-) 없이 입력</span>
				</label>
				<div id="postcodeInput" class="hideInput">
					<input type="text" name="postcode" id="postcode" class="inputFocus" maxlength="11" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('postcode')">&times;</button>
				</div>
				<p class="error">정확히 입력해 주세요.</p>
			</div>
			
			<div class="div1 div3">
				<label for="address" onclick="showInputTag('addressInput');">
				<span style="font-size: 12pt;">주소</span> <span style="font-size: 12pt; color: #cccccc;">하이픈(-) 없이 입력</span>
				</label>
				<div id="addressInput" class="hideInput">
					<input type="text" name="address" id="address" class="inputFocus" maxlength="11" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('address')">&times;</button>
					<input type="text" name="detailaddress" id="phone" class="inputFocus" maxlength="11" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('detailaddress')">&times;</button>
					<input type="text" name="extraaddress" id="extraaddress" class="inputFocus" maxlength="11" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('extraaddress')">&times;</button>
				</div>
				<p class="error">정확히 입력해 주세요.</p>
			</div>
			
			
			
			<div class="div1 div2" >
				<label for="birthday" onclick="showInputTag('birthdayInput');">
				<span style="font-size: 12pt;">생년월일(선택)</span> <span style="font-size: 12pt; color: #cccccc;">8자리 숫자 입력 (예시 19990101)</span>
				</label>
				<div id="birthdayInput" class="hideInput">
					<input type="text" name="birthday" id="birthday" class="inputFocus" maxlength="12" autocomplete="off">
					<button type="button" class="close" onclick="inputClose('birthday')">&times;</button>
				</div>
			</div>
			
			<div class="div1 div3">
				<label>
				<span style="font-size: 12pt;">성별 선택</span> 
				</label>
				<span id="gender">
					<input type="radio" id="male" /><label for="male">남자</label>
		            <input type="radio" id="female" /><label for="female">여자</label>
				</span>
				
			</div>
			
			<div >
				<label for="agree">이용약관에 동의합니다</label>&nbsp;&nbsp;<input type="checkbox" id="agree" />
				<iframe src="<%= ctxPath%>/iframe_agree/agree.html" style="border: solid 1px navy;"></iframe>
			</div>
			
			<div>
				<input type="button" class="btn btn-success btn-lg mr-5" value="가입하기" onclick="goRegister()" />
	        	<input type="reset"  class="btn btn-danger btn-lg" value="취소하기" onclick="goReset()" />
			</div>
		</form>
	</div>
</body>
</html>