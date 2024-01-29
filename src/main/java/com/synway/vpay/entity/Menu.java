package com.synway.vpay.entity;

import com.synway.vpay.base.entity.BaseEntity;
import com.synway.vpay.enums.MenuType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 菜单
 *
 * @since 0.1
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Menu extends BaseEntity {

    /**
     * 菜单名称
     *
     * @since 0.1
     */
    private String name;

    /**
     * 菜单类型
     *
     * @since 0.1
     */
    private MenuType type = MenuType.URL;

    /**
     * url地址
     *
     * @since 0.1
     */
    private String url;

    /**
     * 父级菜单ID
     *
     * @since 0.1
     */
    private UUID parentId;

    /**
     * 子菜单集合
     *
     * @since 0.1
     */
    @OneToMany(targetEntity = Menu.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "parentId")
    private List<Menu> children = new ArrayList<>();

    public Menu(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Menu(String name, List<Menu> children) {
        this.name = name;
        this.type = MenuType.MENU;
        if (!CollectionUtils.isEmpty(children)) {
            this.children.addAll(children);
        }
    }
}
