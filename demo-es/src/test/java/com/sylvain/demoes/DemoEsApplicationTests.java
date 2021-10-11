package com.sylvain.demoes;

import com.sylvain.demoes.repository.ArticleRepository;
import com.sylvain.demoes.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@SpringBootTest
class DemoEsApplicationTests {
    @Autowired
    private ArticleService articleService;

    @Test
    void save() {

    }

}
