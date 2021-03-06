package io.jpress.core.wechat;

import io.jboot.Jboot;
import io.jboot.utils.ClassScanner;
import io.jpress.service.OptionService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.core.wechat
 */
public class WechatAddonManager {

    private static WechatAddonManager me = new WechatAddonManager();
    private static final String OPTION_PREFIX = "wechat_addon_enable_for_";

    private WechatAddonManager() {
    }

    public static WechatAddonManager me() {
        return me;
    }

    /**
     * 所有插件
     */
    private List<WechatAddonInfo> allWechatAddons = new ArrayList<>();

    /**
     * 已经启用的插件
     */
    private List<WechatAddonInfo> enableWechatAddons = new ArrayList<>();


    private OptionService optionService;

    public void init() {

        optionService = Jboot.bean(OptionService.class);

        List<Class<WechatAddon>> classes = ClassScanner.scanSubClass(WechatAddon.class, true);
        if (classes == null || classes.isEmpty()) {
            return;
        }

        for (Class<WechatAddon> addonClass : classes) {
            WechatAddonConfig wechatAddonConfig = addonClass.getDeclaredAnnotation(WechatAddonConfig.class);
            if (wechatAddonConfig == null) {
                continue;
            }

            WechatAddonInfo addon = createWechatAddon(wechatAddonConfig, addonClass);
            addWechatAddon(addon);
        }

        for (WechatAddonInfo addon : allWechatAddons) {
            Boolean enable = optionService.findAsBoolByKey(OPTION_PREFIX + addon.getId());
            if (enable != null && enable == true) {
                enableWechatAddons.add(addon);
            }
        }
    }

    private WechatAddonInfo createWechatAddon(WechatAddonConfig config, Class<WechatAddon> addonClass) {

        WechatAddonInfo wechatAddon = new WechatAddonInfo();
        wechatAddon.setId(config.id());
        wechatAddon.setAuthor(config.author());
        wechatAddon.setAuthorWebsite(config.authorWebsite());
        wechatAddon.setDescription(config.description());
        wechatAddon.setAddonClazz(addonClass.getCanonicalName());
        wechatAddon.setTitle(config.title());
        wechatAddon.setUpdateUrl(config.updateUrl());
        wechatAddon.setVersion(config.version());
        wechatAddon.setVersionCode(config.versionCode());

        Jboot.injectMembers(wechatAddon);
        return wechatAddon;
    }


    public void addWechatAddon(WechatAddonInfo wechatAddon) {
        allWechatAddons.add(wechatAddon);
    }

    public List<WechatAddonInfo> getWechatAddons() {
        return allWechatAddons;
    }

    public List<WechatAddonInfo> getEnableWechatAddons() {
        return enableWechatAddons;
    }

    public void doCloseAddon(String id) {
        optionService.saveOrUpdate(OPTION_PREFIX + id, "false");
        enableWechatAddons.remove(new WechatAddonInfo(id));
    }

    public void doEnableAddon(String id) {
        optionService.saveOrUpdate(OPTION_PREFIX + id, "true");
        for (WechatAddonInfo addon : enableWechatAddons) {
            if (addon.getId().equals(id)) {
                return;
            }
        }

        for (WechatAddonInfo addon : allWechatAddons) {
            if (addon.getId().equals(id)) {
                enableWechatAddons.add(addon);
                break;
            }
        }
    }

    public boolean isEnable(WechatAddonInfo addon) {
        Boolean bool =  enableWechatAddons.contains(addon);
        return bool;
    }

}
