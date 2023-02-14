package com.banana.visual.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.banana.visual.interceptor.LoginInterceptor;
import hashids.formatter.HashidsFormat;
import hashids.formatter.HashidsFormatterFactory;
import hashids.utils.AnnotationUtils;
import hashids.utils.HashidsUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/access/login");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        HashidsFormatterFactory hashidsFormatterFactory = new HashidsFormatterFactory();
        registry.addFormatterForFieldAnnotation(hashidsFormatterFactory);
    }

    /**
     * 添加json输入和输出的hashid转换器，并且依赖使用FastJson，如@RequestBody注解的参数和返回json格式的数据
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter convert = fastJsonConfigure(getLongCodec(), getSerializeFilter());
        converters.add(convert);
    }

    /**
     * json输入参数转换器
     *
     * @return
     */
    protected LongCodec getLongCodec() {
        LongCodec longCodec = new LongCodec() {
            @Override
            public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
                // 获取需要转换的类的实例
                Object contentObject = parser.getContext().object;
                // 判断字段属性是否包含HashidsFormat注解
                if (contentObject != null && AnnotationUtils.fieldHasAnnotation(contentObject.getClass(), ((String) fieldName), HashidsFormat.class)) {
                    JSONLexer lexer = parser.lexer;
                    int token = lexer.token();
                    if (token == JSONToken.NULL) {
                        lexer.nextToken(JSONToken.COMMA);
                        return null;
                    }
                    // 字符串Id转换为整型
                    if (token == JSONToken.LITERAL_STRING) {
                        String value = (String) parser.parse();
                        if (!value.matches("\\d+")) {
                            Long longObj = HashidsUtils.decode(value);
                            return (T) longObj;
                        }
                    }
                }
                return super.deserialze(parser, clazz, fieldName);
            }
        };
        return longCodec;
    }

    /**
     * json输出转换器
     *
     * @return
     */
    protected ValueFilter getSerializeFilter() {
        ValueFilter filter = (object, name, value) -> {
            // 判断字段属性是否包含HashidsFormat注解
            if (object != null && AnnotationUtils.fieldHasAnnotation(object.getClass(), name, HashidsFormat.class)) {
                return HashidsUtils.encode((Long) value);
            }
            return value;
        };
        return filter;
    }

    /**
     * 配置FastJson转换器
     *
     * @param longCodec
     * @param filter
     * @return
     */
    public FastJsonHttpMessageConverter fastJsonConfigure(LongCodec longCodec, ValueFilter filter) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter() {
            @Override
            public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                try {
                    InputStream in = inputMessage.getBody();
                    byte[] bytes = IOUtils.toByteArray(in);
                    Charset charset = getFastJsonConfig().getCharset();
                    if (charset == null) {
                        charset = com.alibaba.fastjson.util.IOUtils.UTF8;
                    }
                    String json = new String(bytes, charset);
                    // 使用自定义的ParserConfig类
                    return JSON.parseObject(json, type, getFastJsonConfig().getParserConfig(), JSON.DEFAULT_PARSER_FEATURE, getFastJsonConfig().getFeatures());
                } catch (JSONException ex) {
                    throw new HttpMessageNotReadableException("JSON parse error: " + ex.getMessage(), ex);
                } catch (IOException ex) {
                    throw new HttpMessageNotReadableException("I/O error while reading input message", ex);
                }
            }
        };
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        /*fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);*/
        // 设置序列化，id => hash id
        fastJsonConfig.setSerializeFilters(filter);
        // 设置反序列化 has id => id
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.putDeserializer(long.class, longCodec);
        parserConfig.putDeserializer(Long.class, longCodec);
        fastJsonConfig.setParserConfig(parserConfig);
        converter.setFastJsonConfig(fastJsonConfig);
        return converter;
    }
}
