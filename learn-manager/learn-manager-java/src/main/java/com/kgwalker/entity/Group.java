package com.kgwalker.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Group)表实体类
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Serializable {
    // 组ID
    private Long id;
    // 组名
    private String name;
}
