package com.io.threegonew.config.filter;

import com.io.threegonew.util.AesUtil;
import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class ParamFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new RequestWrapper((HttpServletRequest)request), response);
    }

    private static class RequestWrapper extends HttpServletRequestWrapper {

        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String parameter) {
            String values[] = super.getParameterValues(parameter); // 전달받은 parameter 불러오기

            if (values == null) {
                return null;
            }

            for (int i = 0; i < values.length; i++) {
                if (values[i] != null) {
                    try {
                        values[i] = AesUtil.aesCBCDecode(values[i]); // parameter 복호화
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return values;
        }

        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter); // 전달받은 parameter 불러오기

            if(value == null) {
                return null;
            }

            try {
                value = AesUtil.aesCBCDecode(value); // parameter 복호화
            } catch(Exception e) {
                e.printStackTrace();
            }

            return value;
        }
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
