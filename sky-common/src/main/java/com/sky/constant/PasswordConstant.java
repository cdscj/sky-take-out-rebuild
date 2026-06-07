package com.sky.constant;

/**
 * 密码常量。
 * 默认密码必须通过环境变量 {@code SKY_DEFAULT_PASSWORD} 注入，禁止硬编码。
 */
public class PasswordConstant {

    public static final String DEFAULT_PASSWORD = System.getenv().getOrDefault("SKY_DEFAULT_PASSWORD", "");

}
