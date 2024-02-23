package com.synway.vpay.spring;

import com.synway.vpay.base.util.BaseUtil;
import com.synway.vpay.bean.TemplateConfig;
import com.synway.vpay.util.VpayConstant;
import com.synway.vpay.util.VpayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    private static final String TEMPLATES_DIR = "templates" + File.separator;

    private static final String TEMPLATES_PATH = System.getProperty("user.dir") + File.separator + TEMPLATES_DIR;

    @jakarta.annotation.Resource
    private ResourceLoader resourceLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("初始化页面模板...");

        CONFIGS.put(TemplateConfig.DEFAULT.getId(), TemplateConfig.DEFAULT);

        List<TemplateConfig> customTemplates = this.getCustomTemplates();

        this.dealTemplates(customTemplates);

        CONFIGS.put(VpayConstant.ACTIVE, TemplateConfig.DEFAULT);

        // TODO... 暂时用经典模板
        CONFIGS.put(VpayConstant.ACTIVE, CONFIGS.get("bec923cc-d5e2-44d5-bab9-a278331b59db"));

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
            File configFile = new File(TEMPLATES_PATH + file.getName() + File.separator + "config.json");
            if (!configFile.exists()) {
                continue;
            }
            // 读取config.json
            String content;
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            TemplateConfig config = BaseUtil.json2Object(content, TemplateConfig.class);
            if (Objects.isNull(config)) {
                continue;
            }

            if (Strings.isBlank(config.getId())) {
                config.setId(VpayUtil.md5(file.getName()));
            }
            Optional.ofNullable(config.getLogin()).ifPresent(s -> config.setLogin(file.getName() + "/" + s));
            Optional.ofNullable(config.getIndex()).ifPresent(s -> config.setIndex(file.getName() + "/" + s));
            Optional.ofNullable(config.getPay()).ifPresent(s -> config.setPay(file.getName() + "/" + s));
            Optional.ofNullable(config.getNotFound()).ifPresent(s -> config.setNotFound(file.getName() + "/" + s));
            configs.add(config);
        }
        return configs;
    }

    private void dealTemplates(List<TemplateConfig> customTemplates) {
        for (TemplateConfig t : customTemplates) {
            if (Strings.isBlank(t.getId())) {
                log.warn("自定义模板缺少ID: {}", BaseUtil.object2Json(t));
                continue;
            }
            t.setName(Strings.isBlank(t.getName()) ? "未命名" : t.getName());
            log.info("成功加载自定义模板：{}", t.getName());

            t.setLogin(this.getTemplatePath(t.getLogin(), TemplateConfig.DEFAULT.getLogin()));
            t.setIndex(this.getTemplatePath(t.getIndex(), TemplateConfig.DEFAULT.getIndex()));
            t.setPay(this.getTemplatePath(t.getPay(), TemplateConfig.DEFAULT.getPay()));
            t.setNotFound(this.getTemplatePath(t.getNotFound(), TemplateConfig.DEFAULT.getNotFound()));

            CONFIGS.put(t.getId(), t);
        }
    }

    private String getTemplatePath(String path, String defaultPath) {
        // 判断登录页是否存在
        if (Objects.isNull(path) || !new File(TEMPLATES_PATH + path).exists()) {
            return defaultPath;
        }
        return path;
    }
}
