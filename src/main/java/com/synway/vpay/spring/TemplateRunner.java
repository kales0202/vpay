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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    private static final String TEMPLATES_DIR = "templates/";

    private static final String TEMPLATES_PATH = "classpath:/static/" + TEMPLATES_DIR;

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
        Resource folderResource = resourceLoader.getResource(TEMPLATES_PATH);
        File[] files = folderResource.getFile().listFiles();
        if (Objects.isNull(files)) {
            return Collections.emptyList();
        }

        List<TemplateConfig> configs = new ArrayList<>();
        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }

            // 读取文件夹下的config.json
            Resource configResource = resourceLoader.getResource(TEMPLATES_PATH + file.getName() + "/config.json");
            if (!configResource.exists()) {
                continue;
            }
            String content;
            try (InputStream inputStream = configResource.getInputStream()) {
                content = new String(inputStream.readAllBytes());
            }
            TemplateConfig config = BaseUtil.json2Object(content, TemplateConfig.class);
            if (Objects.isNull(config)) {
                continue;
            }

            String resourcePath = TEMPLATES_DIR + file.getName();
            if (Strings.isBlank(config.getId())) {
                config.setId(VpayUtil.md5(resourcePath));
            }
            Optional.ofNullable(config.getLogin()).ifPresent(s -> config.setLogin(resourcePath + "/" + s));
            Optional.ofNullable(config.getIndex()).ifPresent(s -> config.setIndex(resourcePath + "/" + s));
            Optional.ofNullable(config.getPay()).ifPresent(s -> config.setPay(resourcePath + "/" + s));
            Optional.ofNullable(config.getNotFound()).ifPresent(s -> config.setNotFound(resourcePath + "/" + s));
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
        if (Objects.isNull(path) || !resourceLoader.getResource("classpath:/static/" + path).exists()) {
            return defaultPath;
        }
        return path;
    }
}
