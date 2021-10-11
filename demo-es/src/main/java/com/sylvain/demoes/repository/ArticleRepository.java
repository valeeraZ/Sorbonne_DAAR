package com.sylvain.demoes.repository;

import com.sylvain.demoes.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * Created by Wenzhuo Zhao on 11/10/2021.
 */
@Component
public interface ArticleRepository extends ElasticsearchRepository<Article,String> {

}
