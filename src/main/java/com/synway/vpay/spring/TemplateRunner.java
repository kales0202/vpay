package com.synway.vpay.spring;

import com.synway.vpay.base.util.BaseUtil;
import com.synway.vpay.bean.TemplateConfig;
import com.synway.vpay.util.VpayConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("初始化页面模板...");

        CONFIGS.put(TemplateConfig.DEFAULT.getKey(), TemplateConfig.DEFAULT);

        List<TemplateConfig> customTemplates = new ArrayList<>();

        // 添加自定义模板：经典模板(vmq)
        URL resourceStatic = this.getClass().getClassLoader().getResource("static");
        if (Objects.nonNull(resourceStatic)) {
            TemplateConfig vmq = this.getTemplateConfig(resourceStatic.getPath(), "vmq");
            if (Objects.nonNull(vmq)) {
                customTemplates.add(vmq);
            }
        }

        // 添加用户自定义模板
        customTemplates.addAll(this.getCustomTemplates());

        // 自定义模板检查
        this.dealTemplates(customTemplates);

        customTemplates.forEach(c -> CONFIGS.put(c.getKey(), c));

        // TODO... 暂时使用经典模板
        CONFIGS.put(VpayConstant.ACTIVE, CONFIGS.get("vmq"));

        log.info("初始化页面模板完成...");
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
            TemplateConfig config = this.getTemplateConfig(TEMPLATES_PATH, file.getName());
            if (Objects.nonNull(config)) {
                configs.add(config);
            }
        }
        return configs;
    }

    private TemplateConfig getTemplateConfig(String location, String key) throws IOException {
        File configFile = new File(location + File.separator + key + File.separator + "config.json");
        if (!configFile.exists()) {
            return null;
        }
        // 读取config.json
        String content;
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        TemplateConfig config = BaseUtil.json2Object(content, TemplateConfig.class);
        if (Objects.isNull(config)) {
            return null;
        }
        config.setKey(key);
        config.setLocation(location);
        return config;
    }

    private void dealTemplates(List<TemplateConfig> customTemplates) {
        for (TemplateConfig t : customTemplates) {
            t.setName(Strings.isBlank(t.getName()) ? "未命名" : t.getName());
            log.info("成功加载自定义模板：key={}, name={}", t.getKey(), t.getName());

            t.setLogin(this.getTemplatePage(t, t.getLogin(), TemplateConfig.DEFAULT.getLogin()));
            t.setIndex(this.getTemplatePage(t, t.getIndex(), TemplateConfig.DEFAULT.getIndex()));
            t.setPay(this.getTemplatePage(t, t.getPay(), TemplateConfig.DEFAULT.getPay()));
            t.setNotFound(this.getTemplatePage(t, t.getNotFound(), TemplateConfig.DEFAULT.getNotFound()));
        }
    }

    private String getTemplatePage(TemplateConfig t, String page, String defaultPath) {
        // 判断页面是否存在
        String pagePath = t.getLocation() + File.separator + t.getKey() + File.separator + page;
        if (Objects.isNull(page) || !new File(pagePath).exists()) {
            return defaultPath;
        }
        return t.getKey() + "/" + page;
    }
}
