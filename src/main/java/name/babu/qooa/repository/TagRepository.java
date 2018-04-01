package name.babu.qooa.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import name.babu.qooa.model.Tag;

/**
 * XXX TODO create javadoc
 */
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {

  @Override
  Tag save(@Param("tag") Tag tag);

}
