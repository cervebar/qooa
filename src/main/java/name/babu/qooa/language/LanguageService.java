package name.babu.qooa.language;

import org.springframework.stereotype.Service;

/**
 * XXX TODO create javadoc
 */
@Service
public class LanguageService {

  private LanguageContext languageContext = new CZContext();

  public LanguageContext getContext() {
    return languageContext;
  }

}
