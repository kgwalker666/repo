package com.kgwalker.syt.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表(User)表实体类
 *
 * @author kgwalker
 * @since 2023-02-26 10:43:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    // 用户ID
    private Long id;
    // 姓名
    private String name;
    // 年龄
    private Integer age;
    // 邮箱
    private String email;
}
