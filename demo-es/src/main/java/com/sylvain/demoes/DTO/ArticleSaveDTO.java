package com.sylvain.demoes.DTO;

import com.sylvain.demoes.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;

/**
 * Created by Wenzhuo Zhao on 11/10/2021.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSaveDTO {
    @NotBlank
    String title;
    @NotBlank
    String content;
    @NotBlank
    String author;
    @NotNull
    Integer viewed_time;
    @NotNull
    Integer words_number;

    public Article toArticle(){
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .viewed_time(viewed_time)
                .words_number(words_number)
                .build();
    }
}
