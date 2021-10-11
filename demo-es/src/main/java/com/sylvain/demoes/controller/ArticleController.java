package com.sylvain.demoes.controller;

import com.sylvain.demoes.DTO.ArticleSaveDTO;
import com.sylvain.demoes.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

/**
 * Created by Wenzhuo Zhao on 11/10/2021.
 */
@RestController
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RequestMapping("/api")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity<Void> addArticle(@RequestBody @Valid ArticleSaveDTO articleSaveDTO){
        articleService.save(articleSaveDTO);
        return ResponseEntity.ok().build();
    }
}
