package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.dao.QuestionDao;
import next.model.Question;

public class SaveController implements Controller {
	private QuestionDao questionDao = new QuestionDao();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String writer = request.getParameter("writer");
		String title = request.getParameter("title");
		String contents = request.getParameter("contents");
		
		Question question = new Question(writer, title, contents);
		questionDao.insert(question);
		return "redirect:index.jsp";
	}

}
