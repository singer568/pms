package org.springside.examples.quickstart.service.spider.util;

import java.util.List;

import org.springside.examples.quickstart.entity.KeyWords;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.entity.Url;

public interface CatchService {
	
	List<Subjects> catchPolicy(Url url) throws Exception;
	
	List<Subjects> catchPolicy(Url url ,List<KeyWords> keyWords) throws Exception;
	
	List<Subjects> updateSubjects(List<Subjects> lst) throws Exception;
	
	Subjects updateSubject(Subjects subj) throws Exception;
}
