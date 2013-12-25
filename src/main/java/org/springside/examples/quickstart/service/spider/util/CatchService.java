package org.springside.examples.quickstart.service.spider.util;

import java.util.List;

import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.entity.Url;

public interface CatchService {
	List<Subjects> catchPolicy(Url url);
}
