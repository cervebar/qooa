package name.babu.qooa.qa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import name.babu.qooa.qa.model.Answer;

public interface AnswerRepository extends PagingAndSortingRepository<Answer, String> {

  @Override
  Answer save(@Param("answer") Answer a);

}
