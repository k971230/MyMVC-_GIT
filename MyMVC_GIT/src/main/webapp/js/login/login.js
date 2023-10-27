
$(document).ready(function(){
	
	$("button#btnSubmit").click(function(){
		  goLogin(); // 로그인 시도한다.
	});
	
	$("input#loginPwd").bind("keydown", function(e){
	    if(e.keyCode == 13) { // 암호입력란에 엔터를 했을 경우 
		  goLogin(); // 로그인 시도한다.	
		}	
	});
	
});// end of $(document).ready(function(){})------------------


// === 로그인 처리 함수 === //
function goLogin() {
 //	alert("확인용 로그인 처리 하러 간다.");
	
	const loginUserid = $("input#loginUserid").val().trim();
	const loginPwd = $("input#loginPwd").val().trim();
	
	if(loginUserid == "") {
		alert("아이디를 입력하세요!!");
		$("input#loginUserid").val("").focus();
		return; // goLogin() 함수 종료
	}
	
	if(loginPwd == "") {
		alert("암호를 입력하세요!!");
		$("input#loginPwd").val("").focus();
		return; // goLogin() 함수 종료
	}
	
	
	if( $("input:checkbox[id='saveid']").prop("checked") ) {
		// alert("아이디저장 체크를 하셨네요~~^^");
		
		localStorage.setItem('saveid', $("input#loginUserid").val());
	}
	else {
		// alert("아이디저장 체크를 해제 하셨네요!!");
		
		localStorage.removeItem('saveid');
	}
	
	const frm = document.loginFrm;
	frm.submit();
	
}// end of function goLogin()-------------


function goLogOut(ctx_Path) {
	
	// 로그아웃을 처리해주는 페이지로 이동
	location.href=`${ctx_Path}/login/logout.up`;
}// end of function goLogOut(ctx_Path)------------


// === 나의정보 수정하기 === //
function goEditMyInfo(userid, ctx_Path) {
	
	// 나의정보 수정하기 팝업창 띄우기
	const url = `${ctx_Path}/member/memberEdit.up?userid=${userid}`;
//  또는
//  const url = ctx_Path+"/member/memberEdit.up?userid="+userid;	
	
	// 너비 800, 높이 680 인 팝업창을 화면 가운데 위치시키기
	const width = 800;
	const height = 680;
/*	
	console.log("모니터의 넓이 : ",window.screen.width);
	// 모니터의 넓이 :  1440
	
	console.log("모니터의 높이 : ",window.screen.height);
	// 모니터의 높이 :  900
*/	
    const left = Math.ceil((window.screen.width - width)/2);  // 정수로 만듬 
    const top = Math.ceil((window.screen.height - height)/2); // 정수로 만듬
	window.open(url, "myInfoEdit", 
	            `left=${left}, top=${top}, width=${width}, height=${height}`); 
	
}// end of function goEditMyInfo(userid, ctx_Path)----------


// === 코인충전 결제금액 선택하기(실제로 카드 결제) === //
function goCoinPurchaseTypeChoice(userid, ctx_Path){
	
	// 코인충전 결제금액 선택하기 팝업창 띄우기
	const url = `${ctx_Path}/member/coinPurchaseTypeChoice.up?userid=${userid}`; 
	
	// 너비 650, 높이 570 인 팝업창을 화면 가운데 위치시키기
	const width = 650;
	const height = 570;
	
	const left = Math.ceil((window.screen.width - width)/2);  // 정수로 만듬 
    const top = Math.ceil((window.screen.height - height)/2); // 정수로 만듬
	window.open(url, "coinPurchaseTypeChoice", 
	            `left=${left}, top=${top}, width=${width}, height=${height}`); 
	
}// end of function goCoinPurchaseTypeChoice(userid, ctx_Path)----


// === 포트원(구 아임포트) 결제를 해주는 함수 === //
function goCoinPurchaseEnd(ctx_Path, coinmoney, userid){
  
 // alert(`확인용 부모창의 함수 호출함.\n결제금액 : ${coinmoney}원, 사용자id : ${userid}`);
 
 
 // 포트원(구 아임포트) 결제 팝업창 띄우기 
 // 코인충전 결제금액 선택하기 팝업창 띄우기
	const url = `${ctx_Path}/member/coinPurchaseEnd.up?coinmoney=${coinmoney}&userid=${userid}`;  
	
	// 너비 1000, 높이 600 인 팝업창을 화면 가운데 위치시키기
	const width = 1000;
	const height = 600;
	
	const left = Math.ceil((window.screen.width - width)/2);  // 정수로 만듬 
    const top = Math.ceil((window.screen.height - height)/2); // 정수로 만듬
	window.open(url, "coinPurchaseEnd", 
	            `left=${left}, top=${top}, width=${width}, height=${height}`);
    
	
}// end of function goCoinPurchaseEnd(ctx_Path, coinmoney, userid)------------


// ==== DB 상의 tbl_member 테이블에 해당 사용자의 코인금액 및 포인트를 증가(update)시켜주는 함수 === //
function goCoin_DB_Update(userid, coinmoney) {
	
//	console.log(`~~ 확인용 userid : ${userid}, coinmoney : ${coinmoney}원`);
	
	const frm = document.coin_DB_Update_Frm;
	frm.userid.value = userid;
	frm.coinmoney.value = coinmoney;
	
	frm.action = `/MyMVC/member/coinUpdateLoginUser.up`;
	frm.method = "post";
	frm.submit();
}






 
 