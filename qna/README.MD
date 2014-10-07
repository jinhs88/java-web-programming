1단계

http://localhost:8080 으로 브라우져에서 접속하면
web.xml 에 설정된 <welcome-file-list>에 설정된 index.jsp로 이동된다.
index.jsp에 보면 list.next 로 리다이렉트 하라는 내용이 써있다.
브라우져는 응답을 받은뒤 다시 list.next로 재 요청한다.
web.xml을보면 list.next는 FrontController 서블릿이 담당하게 되있다.
FrontController에서 Controller를 결정하게 되는데, list.next 는 ListController가 담당하게 된다. 
ListController.execute를 보면 questionDato.findAll()메소드를 통해 질문의 내용을 찾은뒤에 request.setAttribute를 하고 해당 뷰(list.jsp)를 리턴한다.
FrontController.movePage에 에 의해서 해당 뷰(list.jsp)로 포워딩 된다.
뷰에 해당하는 list.jsp에서 넘어온 질문내용들을 html로 변경한뒤에 브라우져에 표시된다.


 
