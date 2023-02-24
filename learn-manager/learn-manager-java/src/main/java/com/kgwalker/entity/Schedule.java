package com.kgwalker.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Schedule)表实体类
 *
 * @author kgwalker
 * @since 2023-02-20 13:58:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule implements Serializable {
    // 进度ID
    private Long id;
    // 课程索引号
    private Integer idx;
    // 状态【0，未完成，1已完成】
    private Integer status;
    // 该idx所属的组
    private Long groupId;
    // 该idx所属的课程
    private Long courseId;
}
