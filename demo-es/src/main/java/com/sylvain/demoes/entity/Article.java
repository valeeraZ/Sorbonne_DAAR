package com.sylvain.demoes.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by Wenzhuo Zhao on 11/10/2021.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "articles")
public class Article {
    @Id
    String id;
    String title;
    String content;
    @Field(type = FieldType.Keyword)
    String author;
    int viewed_time;
    int words_number;
}
