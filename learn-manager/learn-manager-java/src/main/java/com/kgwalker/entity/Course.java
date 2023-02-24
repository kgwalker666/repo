package com.kgwalker.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Course)表实体类
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    // 课程ID
    private Long id;
    // 课程名称
    private String title;
    // 课程地址
    private String url;
    // 课程作者
    private String author;
}
