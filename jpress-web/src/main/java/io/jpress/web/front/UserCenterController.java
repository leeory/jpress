package io.jpress.web.front;

import com.jfinal.kit.Ret;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.model.User;
import io.jpress.service.UserService;
import io.jpress.web.base.UcenterControllerBase;

import javax.inject.Inject;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.web
 */
@RequestMapping("/ucenter")
public class UserCenterController extends UcenterControllerBase {

    @Inject
    private UserService userService;


    @Override
    public void render(String view) {
        super.render("/WEB-INF/views/ucenter/" + view);
    }


    /**
     * 用户中心首页
     */
    public void index() {
        render("index.html");
    }


    public void info() {
        render("info.html");
    }

    public void doSaveUser() {
        User user = getBean(User.class);
        user.keep("nickname", "realname", "identity", "email", "mobile",
                "signature", "birthday", "company", "occupation", "address",
                "zipcode", "site", "graduateschool", "education", "idcardtype", "idcard");
        user.setId(getLoginedUser().getId());
        userService.saveOrUpdate(user);
        renderJson(Ret.ok());
    }


    /**
     * 个人签名
     */
    public void signature() {
        render("signature.html");
    }


    /**
     * 头像设置
     */
    public void avatar() {
        render("avatar.html");
    }

    /**
     * 账号密码
     */
    public void pwd() {
        render("pwd.html");
    }


}
