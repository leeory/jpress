package io.jpress.web.directives;

import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import io.jboot.utils.StrUtils;
import io.jboot.web.directive.annotation.JFinalDirective;
import io.jboot.web.directive.base.JbootDirectiveBase;
import io.jpress.model.User;
import io.jpress.service.RoleService;
import io.jpress.web.base.UserInterceptor;

import javax.inject.Inject;
import java.util.Set;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.core.directives
 */
@JFinalDirective("anyRole")
public class AnyRoleDirective extends JbootDirectiveBase {

    @Inject
    private RoleService roleService;

    @Override
    public void onRender(Env env, Scope scope, Writer writer) {

        User user = UserInterceptor.getThreadLocalUser();
        if (user == null || !user.isStatusOk()) {
            return;
        }


        Set<String> roles = StrUtils.splitToSet(getParam(0, scope), ",");
        if (roles == null || roles.size() == 0) {
            throw new IllegalArgumentException("#anyRole(...) argument must not be empty");
        }

        if (roleService.hasAnyRole(user.getId(), roles.toArray(new String[]{}))) {
            renderBody(env, scope, writer);
        }
    }
}

