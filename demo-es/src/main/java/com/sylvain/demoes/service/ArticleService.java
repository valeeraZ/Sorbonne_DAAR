package com.sylvain.demoes.service;

import com.sylvain.demoes.DTO.ArticleSaveDTO;
import com.sylvain.demoes.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Wenzhuo Zhao on 11/10/2021.
 */
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ArticleService {
    private final ArticleRepository articleRepository;

    public void save(ArticleSaveDTO articleSaveDTO){
        System.out.println(articleSaveDTO);
        articleRepository.save(articleSaveDTO.toArticle());
    }
}
