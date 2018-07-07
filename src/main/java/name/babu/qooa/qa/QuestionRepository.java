package name.babu.qooa.qa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import name.babu.qooa.qa.model.Question;

public interface QuestionRepository extends PagingAndSortingRepository<Question, String> {

  @Override
  Question save(@Param("question") Question q);

}
