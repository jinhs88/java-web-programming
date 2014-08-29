package next.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

import org.springframework.beans.factory.annotation.Autowired;

import core.mvc.Controller;

public class ShowController implements Controller {
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private AnswerDao answerDao;
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long questionId = Long.parseLong(request.getParameter("questionId"));
		Question question;
		List<Answer> answers;
		question = questionDao.findById(questionId);
		answers = answerDao.findAllByQuestionId(questionId);
		request.setAttribute("question", question);
		request.setAttribute("answers", answers);
		
		return "show";
	}
}
