package name.babu.qooa.user.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import name.babu.qooa.qa.model.Tag;

/**
 * XXX TODO create javadoc
 */
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {

  @Override
  Tag save(@Param("tag") Tag tag);

}
