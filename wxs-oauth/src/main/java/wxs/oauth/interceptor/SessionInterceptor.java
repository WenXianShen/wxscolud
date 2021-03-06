package wxs.oauth.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wxs.common.util.RedisHelper;
import wxs.common.vo.UserLoginResVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**自定义拦截器
 * @author : imperater
 * @date : 2019/12/6
 */
@Component
@Slf4j
public class SessionInterceptor  implements HandlerInterceptor {

    @Autowired
    private RedisHelper redisHelper;

    /**
     * 存放所有登录用户
     * key :userId+sessionId
     */
    public   static Map<String,String> optionMap=new HashMap<String,String> ();

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception e)
            throws Exception {
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView e)
            throws Exception {
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Object) throws Exception {
        String token=request.getHeader("token");
        log.info("正在访问的url:{}",request.getRequestURL());
        log.info("token为:{}",token);
        //普通路径放行
        if ( "/WebLogin/login.do".equals(request.getServletPath())) {
            return true;
        }

        /**
         * 这里当前只验证是否携带token，后期还有判断token是否正确
         */
        if(token == "" || token == null) {
            ((HttpServletResponse) response).setHeader("content-type", "text/html;charset=UTF-8");
            response.getWriter().write("{\"status\":0,\"message\":\"请登录！\",\"result\":null}");
            return false;
        }
        //验证token是否正确
        //权限路径拦截
        //UserResVo user=(UserResVo) request.getSession().getAttribute("user");
        UserLoginResVo user = (UserLoginResVo) redisHelper.get(token);
        if(null != user) {
            String oldToken = (String) redisHelper.get(user.getId().toString());
            if(token != null && token.equals(oldToken)) {
               return true;
            } else {
                //页面提示登录失效或您的账号已在其它地点登录
                log.info("用户编号为:{}被强制挤下线,sesionId为:{}",user.getId(),request.getRequestedSessionId());;
                ((HttpServletResponse) response).setHeader("content-type", "text/html;charset=UTF-8");
                redisHelper.del(token);
                response.getWriter().write("{\"status\":0,\"message\":\"该账号已在别处登录！\",\"result\":null}");
                return false;
            }
        }else{
            ((HttpServletResponse) response).setHeader("content-type", "text/html;charset=UTF-8");
            response.getWriter().write("{\"status\":0,\"message\":\"登录已失效！\",\"result\":null}");
            return false;
        }
    }


}
