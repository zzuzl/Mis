package cn.zzuzl.common.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * Created by Administrator on 2016/9/11.
 */
public class ResultConvertHandler extends RequestResponseBodyMethodProcessor {
    private Logger logger = LogManager.getLogger(getClass());

    public ResultConvertHandler(List<HttpMessageConverter<?>> converters) {
        super(converters);
        logger.info("ResultConvertHandler");
    }


}
