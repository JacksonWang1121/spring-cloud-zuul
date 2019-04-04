package sdibt.group.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author wangjisen
 * @date 2019/4/1 9:15
 */
@Component
public class CustomZuulFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(CustomZuulFilter.class);

    /**
     * 返回一个字符串代表过滤器的类型
     * pre：路由之前
     * routing：路由之时
     * post：路由之后
     * error：发送错误调用
     * @return
     */
    @Override
    public String filterType() {
        return "CustomZuulFilter::filterType:pre";
    }

    /**
     * 过滤的顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 写逻辑判断，是否要过滤
     * true：永远过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的具体逻辑，可用很复杂，包括sql、nosql去判断该请求到底有没有权限访问
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if (accessToken == null) {
            logger.warn("CustomZuulFilter::run:Token is empty...");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("CustomZuulFilter::run:Token is empty...");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        logger.info("CustomZuulFilter::run:OK");
        return null;
    }
}
