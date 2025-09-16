<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	request.setCharacterEncoding("UTF-8"); 
	String cp = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/springwebmybatis/resources/css/style.css" type="text/css"/>
<link rel="stylesheet" href="/springwebmybatis/resources/css/created.css" type="text/css"/>
<script type="text/javascript" src="/springwebmybatis/resources/js/util.js"></script>

<script type="text/javascript">

	function sendIt() {
		
		f = document.myForm;
		
		str = f.subject.value;
		str = str.trim();
		if(!str){
			alert("\n제목을 입력하세요.");
			f.subject.focus();
			return;
		}
		f.subject.value = str;
		
		str = f.name.value;
		str = str.trim();
		if(!str){
			alert("\n이름을 입력하세요.");
			f.name.focus();
			return;
		}
		/*
		if(!isValidKorean(str)){
			alert("\n이름을 한글로 입력하세요.");
			f.name.focus();
			return;
		}
		*/
		f.name.value = str;
		
		if(f.email.value){
			if(!isValidEmail(f.email.value)){
				alert("\n정상적인 E-Mail을 입력하세요.")
				f.email.focus();
				return;
			}
		}
		
		str = f.content.value;
		str = str.trim();
		if(!str){
			alert("\n내용을 입력하세요.");
			f.content.focus();
			return;
		}
		f.content.value = str;
		
		str = f.pwd.value;
		str = str.trim();
		if(!str){
			alert("\n비밀번호을 입력하세요.");
			f.pwd.focus();
			return;
		}
		if(str!="${dto.pwd}"){
			alert("비밀번호가 일치하지 않습니다.")
			f.pwd.focus();
			return;
		}
		f.pwd.value = str;
		
		f.action = "<%=cp%>/update_ok.action?${params}&num=${dto.num}";
		f.submit();
	}

</script>
</head>
<body>

<div id="bbs">
	<div id="bbs_title">게 시 판</div>
	<form action="" name="myForm" method="post">
		<div id="bbsCreated">
			<div class="bbsCreated_bottomLine">
				<dl>
					<dt>제&nbsp;&nbsp;&nbsp;&nbsp;목</dt>
					<dd>
					<input type="text" name="subject" size="60" 
						maxlength="100" class="boxTF" value="${dto.subject }"/>
					</dd>
				</dl>
			</div>
			
			<div class="bbsCreated_bottomLine">
				<dl>
					<dt>작 성 자</dt>
					<dd>
					<input type="text" name="name" size="35" 
						maxlength="20" class="boxTF" value="${dto.name }"
						readonly="readonly"/>
					</dd>
				</dl>
			</div>
			
			<div class="bbsCreated_bottomLine">
				<dl>
					<dt>E-Mail</dt>
					<dd>
					<input type="text" name="email" size="35" 
						maxlength="50" class="boxTF" value="${dto.email }"/>
					</dd>
				</dl>
			</div>
			
			<div id="bbsCreated_content">
				<dl>
					<dt>내&nbsp;&nbsp;&nbsp;&nbsp;용</dt>
					<dd>
					<textarea rows="12" cols="63" name="content" class="boxTA"
					 style="resize: none; background-color: #ffffff;" >${dto.content }</textarea>
					</dd>
				</dl>
			</div>
			
			<div class="bbsCreated_noLine">
				<dl>
					<dt>패스워드</dt>
					<dd>
					<input type="password" name="pwd" size="35" 
						maxlength="7" class="boxTF"/>
						&nbsp;(게시물 수정 및 삭제시 필요!)
					</dd>
				</dl>
			</div>
			
		</div>
		
		<div id="bbsCreated_footer">
			<input type="button" value="수정하기" class="btn2" onclick="sendIt();"/>
			<input type="button" value="수정취소" class="btn2" 
			onclick="location='<%=cp %>/article.action?${params}&num=${dto.num }'">
		</div>
		
	</form>
</div>

</body>
</html>