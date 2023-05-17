/*
 * Copyright 2015-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.luckykuang.auth.utils;

import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 工具类
 * @author luckykuang
 * @date 2023/4/23 22:42
 */
public class Base64Utils {

    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();
    private static final Base64.Encoder MIME_ENCODER = Base64.getMimeEncoder();
    private static final Base64.Decoder MIME_DECODER = Base64.getMimeDecoder();


    private Base64Utils(){}

    /**
     * 标准 Base64 加密
     * @param text 需要加密的字符串
     * @return 加密串
     */
    public static String encode(String text){
        return ENCODER.encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 标准 Base64 加密
     * @param bytes 需要加密的字节数组
     * @return 加密串
     */
    public static String encode(byte[] bytes){
        return ENCODER.encodeToString(bytes);
    }

    /**
     * 标准 Base64 解密
     * @param encodeText 需要解密的密文
     * @return 解密串
     */
    public static String decode(String encodeText){
        return new String(DECODER.decode(encodeText),StandardCharsets.UTF_8);
    }

    /**
     * Url Base64 加密
     * @param text 需要加密的字符串
     * @return 加密串
     */
    public static String urlEncode(String text){
        return URL_ENCODER.encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Url Base64 加密
     * @param bytes 需要加密的字节数组
     * @return 加密串
     */
    public static String urlEncode(byte[] bytes){
        return URL_ENCODER.encodeToString(bytes);
    }

    /**
     * Url Base64 解密
     * @param encodeText 需要解密的密文
     * @return 解密串
     */
    public static String urlDecode(String encodeText){
        return new String(URL_DECODER.decode(encodeText),StandardCharsets.UTF_8);
    }

    /**
     * Mime Base64 加密
     * @param text 需要加密的字符串
     * @return 加密串
     */
    public static String mimeEncode(String text){
        return MIME_ENCODER.encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Mime Base64 加密
     * @param bytes 需要加密的字节数组
     * @return 加密串
     */
    public static String mimeEncode(byte[] bytes){
        return MIME_ENCODER.encodeToString(bytes);
    }

    /**
     * Mime Base64 解密
     * @param encodeText 需要解密的密文
     * @return 解密串
     */
    public static String mimeDecode(String encodeText){
        return new String(MIME_DECODER.decode(encodeText),StandardCharsets.UTF_8);
    }

    /**
     * 图片转 标准 Base64
     * @param imgFilePath 图片url
     * @return 不带前缀的 标准 Base64 字符串 如需页面显示，需添加前缀：data:image/jpg;base64,
     */
    public static String imageToBase64(String imgFilePath) {
        int offset = 0;
        int bytesRead;
        byte[] data = new byte[1024];
        try(InputStream in = new FileInputStream(imgFilePath)) {
            while ((bytesRead = in.read(data, offset, data.length - offset))
                    != -1) {
                offset += bytesRead;
                if (offset >= data.length) {
                    break;
                }
            }
        } catch (IOException e){
            throw new BusinessException(ErrorCode.IMAGE_TO_BASE64_ERROR);
        }
        return encode(new String(data, 0, offset, StandardCharsets.UTF_8));
    }

    /**
     * 标准 Base64 转图片
     * @param imageBase64Data 标准 Base64 图片字符串
     * @param imageFilePath 图片输出本地路径
     * @return 图片url
     */
    public static String base64ToImage(String imageBase64Data,String imageFilePath) {
        if (imageBase64Data == null || imageFilePath == null) {
            return null;
        }
        File path = new File(imageFilePath);
        if (!path.exists()){
            try {
                path.createNewFile();
            } catch (Exception e){
                throw new BusinessException(ErrorCode.FILE_CREATED_ERROR);
            }
        }
        try(OutputStream out = new FileOutputStream(path.getAbsolutePath())) {
            byte[] bytes = DECODER.decode(imageBase64Data);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            out.write(bytes);
            out.flush();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.BASE64_TO_IMAGE_ERROR);
        }
        return imageFilePath;
    }
}
