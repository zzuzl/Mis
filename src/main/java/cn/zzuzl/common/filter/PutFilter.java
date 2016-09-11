package cn.zzuzl.common.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Administrator on 2016/9/11.
 */

/**
 * 自定义处理angular put请求
 */
public class PutFilter extends OncePerRequestFilter {
    private final FormHttpMessageConverter formConverter = new AllEncompassingFormHttpMessageConverter();
    private Logger logger = LogManager.getLogger(getClass());

    public PutFilter() {
    }

    protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("method is " + request.getMethod() + "contentType is " + request.getContentType());

        if (("PUT".equals(request.getMethod()) || "PATCH".equals(request.getMethod())) && this.isJsonContentType(request)) {
            ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request) {
                public InputStream getBody() throws IOException {
                    return request.getInputStream();
                }
            };
            MultiValueMap parameters = this.formConverter.read((Class) null, inputMessage);
            HttpPutContentRequestWrapper wrapper = new HttpPutContentRequestWrapper(request, parameters);
            filterChain.doFilter(wrapper, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isJsonContentType(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            try {
                MediaType ex = MediaType.parseMediaType(contentType);
                return MediaType.APPLICATION_JSON.includes(ex);
            } catch (IllegalArgumentException var4) {
                return false;
            }
        } else {
            return false;
        }
    }

    private static class HttpPutContentRequestWrapper extends HttpServletRequestWrapper {
        private MultiValueMap<String, String> parameters;

        public HttpPutContentRequestWrapper(HttpServletRequest request, MultiValueMap<String, String> parameters) {
            super(request);
            this.parameters = (MultiValueMap) (parameters != null ? parameters : new LinkedMultiValueMap());
        }

        public String getParameter(String name) {
            String queryStringValue = super.getParameter(name);
            String formValue = (String) this.parameters.getFirst(name);
            return queryStringValue != null ? queryStringValue : formValue;
        }

        public Map<String, String[]> getParameterMap() {
            LinkedHashMap result = new LinkedHashMap();
            Enumeration names = this.getParameterNames();

            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                result.put(name, this.getParameterValues(name));
            }

            return result;
        }

        public Enumeration<String> getParameterNames() {
            LinkedHashSet names = new LinkedHashSet();
            names.addAll(Collections.list(super.getParameterNames()));
            names.addAll(this.parameters.keySet());
            return Collections.enumeration(names);
        }

        public String[] getParameterValues(String name) {
            String[] queryStringValues = super.getParameterValues(name);
            List formValues = (List) this.parameters.get(name);
            if (formValues == null) {
                return queryStringValues;
            } else if (queryStringValues == null) {
                return (String[]) formValues.toArray(new String[formValues.size()]);
            } else {
                ArrayList result = new ArrayList();
                result.addAll(Arrays.asList(queryStringValues));
                result.addAll(formValues);
                return (String[]) result.toArray(new String[result.size()]);
            }
        }
    }
}
