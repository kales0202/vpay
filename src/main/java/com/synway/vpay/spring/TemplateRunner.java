package com.synway.vpay.spring;

import com.google.common.collect.Sets;
import com.synway.vpay.base.util.BaseUtil;
import com.synway.vpay.bean.TemplateConfig;
import com.synway.vpay.util.VpayConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 初始化页面模板
 *
 * @since 0.1
 */
@Slf4j
@Component
@Order
public class TemplateRunner implements ApplicationRunner {

    public static final Map<String, TemplateConfig> CONFIGS = new HashMap<>();

    private static final String TEMPLATES_DIR = "templates";

    private static final String TEMPLATES_PATH = System.getProperty("user.dir") + File.separator + TEMPLATES_DIR;

    @jakarta.annotation.Resource
    private ResourceLoader resourceLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("初始化页面模板...");
        List<TemplateConfig> customTemplates = new ArrayList<>();

        // 添加默认模板
        CONFIGS.put(TemplateConfig.DEFAULT.getKey(), TemplateConfig.DEFAULT);

        // 添加内置模板
        customTemplates.addAll(this.getInternalTemplates());

        // 添加用户自定义模板
        customTemplates.addAll(this.getCustomTemplates());

        customTemplates.forEach(c -> CONFIGS.put(c.getKey(), c));

        // TODO... 暂时使用经典模板
        CONFIGS.put(VpayConstant.ACTIVE, CONFIGS.get("vmq"));

        log.info("初始化页面模板完成...");
    }

    private List<TemplateConfig> getInternalTemplates() throws IOException {
        Set<String> keys = Sets.newHashSet("vmq");
        List<TemplateConfig> configs = new ArrayList<>();

        Resource staticResource = resourceLoader.getResource("classpath:/static/");
        for (String key : keys) {
            Resource configResource = staticResource.createRelative(key + File.separator + "config.json");
            if (!configResource.exists()) {
                continue;
            }
            // 读取config.json
            String content;
            try (InputStream inputStream = configResource.getInputStream()) {
                content = new String(inputStream.readAllBytes());
            }
            TemplateConfig t = BaseUtil.json2Object(content, TemplateConfig.class);
            if (Objects.isNull(t)) {
                continue;
            }
            t.setKey(key);

            t.setLogin(this.getInternalPage(t, t.getLogin(), TemplateConfig.DEFAULT.getLogin()));
            t.setIndex(this.getInternalPage(t, t.getIndex(), TemplateConfig.DEFAULT.getIndex()));
            t.setPay(this.getInternalPage(t, t.getPay(), TemplateConfig.DEFAULT.getPay()));
            t.setNotFound(this.getInternalPage(t, t.getNotFound(), TemplateConfig.DEFAULT.getNotFound()));

            log.info("成功加载内置模板：key={}, name={}", t.getKey(), t.getName());
            configs.add(t);
        }
        return configs;
    }

    private List<TemplateConfig> getCustomTemplates() throws IOException {
        // 读取/static/templates下的文件夹
        File[] files = new File(TEMPLATES_PATH).listFiles();
        if (Objects.isNull(files)) {
            return Collections.emptyList();
        }

        List<TemplateConfig> configs = new ArrayList<>();
        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }
            File configFile = new File(TEMPLATES_PATH + File.separator + file.getName() + File.separator + "config.json");
            if (!configFile.exists()) {
                continue;
            }
            // 读取config.json
            String content;
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            TemplateConfig t = BaseUtil.json2Object(content, TemplateConfig.class);
            if (Objects.isNull(t)) {
                continue;
            }

            t.setKey(file.getName());
            t.setLogin(this.getTemplatePage(t, t.getLogin(), TemplateConfig.DEFAULT.getLogin()));
            t.setIndex(this.getTemplatePage(t, t.getIndex(), TemplateConfig.DEFAULT.getIndex()));
            t.setPay(this.getTemplatePage(t, t.getPay(), TemplateConfig.DEFAULT.getPay()));
            t.setNotFound(this.getTemplatePage(t, t.getNotFound(), TemplateConfig.DEFAULT.getNotFound()));

            log.info("成功加载自定义模板：key={}, name={}", t.getKey(), t.getName());
            configs.add(t);
        }
        return configs;
    }

    private String getTemplatePage(TemplateConfig t, String page, String defaultPath) {
        // 判断页面是否存在
        String pagePath = TEMPLATES_PATH + File.separator + t.getKey() + File.separator + page;
        if (Objects.isNull(page) || !new File(pagePath).exists()) {
            return defaultPath;
        }
        return t.getKey() + "/" + page;
    }

    private String getInternalPage(TemplateConfig t, String page, String defaultPath) {
        // 判断页面是否存在
        if (Objects.isNull(page) || !resourceLoader.getResource("classpath:/static/" + t.getKey() + "/" + page).exists()) {
            return defaultPath;
        }
        return t.getKey() + "/" + page;
    }
}
