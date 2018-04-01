package name.babu.qooa.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import name.babu.qooa.model.Question;

/**
 * XXX TODO create javadoc
 */
public interface QARepository extends PagingAndSortingRepository<Question, String> {

  @Override
  Question save(@Param("question") Question q);

}
