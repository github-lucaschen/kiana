package com.lucaschen.common.jpa.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "主键，自增 ID")
    private Long id;

    @Column(name = "is_deleted", columnDefinition = "tinyint UNSIGNED DEFAULT '0'")
    @Schema(description = "是否删除，1 是 0 否")
    private Short deleted;

    @Column(name = "create_time", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "数据库保存时间")
    private LocalDateTime createTime;

    @Column(name = "update_time",
            columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "数据库更新时间")
    private LocalDateTime updateTime;
}
