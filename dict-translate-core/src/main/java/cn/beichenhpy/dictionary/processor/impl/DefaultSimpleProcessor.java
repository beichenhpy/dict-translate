/*
 * MIT License
 *
 * Copyright (c) 2022-2032 Pengyu Han
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cn.beichenhpy.dictionary.processor.impl;

import cn.beichenhpy.dictionary.annotation.Dict;
import cn.beichenhpy.dictionary.annotation.plugin.SimplePlugin;
import cn.beichenhpy.dictionary.enums.TranslateConstant;
import cn.beichenhpy.dictionary.processor.SimpleTranslateProcessor;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author beichenhpy
 * @version 0.0.1
 * @since 0.0.1
 * <p> 2022/1/19 18:59
 */
public class DefaultSimpleProcessor implements SimpleTranslateProcessor {

    @Override
    public Object process(Dict dict, Object result, Object keyValue) {
        String ref = dict.ref();
        //判断字段类型 boolean 在 getFieldValue时已经装箱为Boolean了
        SimplePlugin simplePlugin = dict.plugin().simplePlugin();
        String text = simplePlugin.text();
        //添加对直接赋值的判断，优先级高于转换
        if (StrUtil.isNotBlank(text)) {
            ReflectUtil.setFieldValue(result, ref, text);
        } else {
            boolean revert = simplePlugin.isRevert();
            String value = keyValue.toString();
            if (keyValue instanceof Boolean) {
                switch (value) {
                    case TranslateConstant.LOWER_TRUE:
                        setValueForSimple(revert, result, ref, TranslateConstant.YES, TranslateConstant.NO);
                        break;
                    case TranslateConstant.LOWER_FALSE:
                        setValueForSimple(revert, result, ref, TranslateConstant.NO, TranslateConstant.YES);
                        break;
                    default:
                        break;
                }
            }
            if (keyValue instanceof Integer) {
                switch (value) {
                    case TranslateConstant.ONE:
                        setValueForSimple(revert, result, ref, TranslateConstant.YES, TranslateConstant.NO);
                        break;
                    case TranslateConstant.ZERO:
                        setValueForSimple(revert, result, ref, TranslateConstant.NO, TranslateConstant.YES);
                        break;
                    default:
                        break;
                }
            }
        }
        return result;
    }

    /**
     * 设置字段值SIMPLE模式
     *
     * @param isRevert      是否翻转
     * @param current       当前值
     * @param ref           赋值字段名
     * @param noRevertValue 未翻转时的值
     * @param revertValue   翻转后的值
     */
    private void setValueForSimple(boolean isRevert, Object current, String ref, String noRevertValue, String revertValue) {
        if (!isRevert) {
            ReflectUtil.setFieldValue(current, ref, noRevertValue);
        } else {
            ReflectUtil.setFieldValue(current, ref, revertValue);
        }
    }
}
