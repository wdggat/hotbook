package com.iread.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

/**
 * Created by liuxiaolong on 16/11/23.
 */
public class UrlUtilTest {
    @Test
    public void testParseParams() {
        String url = "http://www.cnblogs.com/huhuuu/p/4704839.html?hehe=haha&Ac=no";
        Map<String, String> params = UrlUtil.parseParams(url);
        for(String key: params.keySet()) {
            System.out.println(key + " : " + params.get(key));
        }
    }

    @Test
    public void testExtractParamUrl() {
        String url = "https://www.amazon.cn/gp/slredirect/picassoRedirect.html/ref=pa_sp_btf_browse_stripbooks_sr_pg6_2?ie=UTF8&adId=A0812313XPC589274HC&url=https%3A%2F%2Fwww.amazon.cn%2F%25E8%2594%25A1%25E9%25AA%258F%25E5%25B0%258F%25E8%25AF%25B4%25E5%2585%25A87%25E5%2586%258C-%25E8%25B0%258B%25E6%259D%2580%25E4%25BC%25BC%25E6%25B0%25B4%25E5%25B9%25B4%25E5%258D%258E-%25E7%258C%25AB%25E7%259C%25BC-%25E7%2597%2585%25E6%25AF%2592-%25E5%259C%25B0%25E7%258B%25B1%25E5%258F%2598-%25E5%259C%25A3%25E5%25A9%25B4-%25E7%2588%25B1%25E4%25BA%25BA%25E7%259A%2584%25E5%25A4%25B4%25E9%25A2%2585-%25E8%25BF%25B7%25E5%259F%258E%25E7%25AD%25897%25E5%2586%258C-%25E6%2582%25AC%25E7%2596%2591%25E6%2595%2599%25E7%2588%25B6%25E8%2594%25A1%25E9%25AA%258F%25E4%25BD%259C%25E5%2593%2581-%25E6%2582%25AC%25E7%2596%2591%25E5%25B0%258F%25E8%25AF%25B4-%25E4%25BE%25A6%25E6%258E%25A2%25E6%258E%25A8%25E7%2590%2586%25E5%25B0%258F%25E8%25AF%25B4-%25E8%2594%25A1%25E9%25AA%258F%2Fdp%2FB01CV52COS%2Fref%3Dsr_1_362_twi_pap_1%3Fs%3Dbooks%26ie%3DUTF8%26qid%3D1479055556%26sr%3D1-362-spons%26psc%3D1&qualifier=1479055556&id=705784056146182&widgetName=sp_btf_browse";
        String expected = "https://www.amazon.cn/%E8%94%A1%E9%AA%8F%E5%B0%8F%E8%AF%B4%E5%85%A87%E5%86%8C-%E8%B0%8B%E6%9D%80%E4%BC%BC%E6%B0%B4%E5%B9%B4%E5%8D%8E-%E7%8C%AB%E7%9C%BC-%E7%97%85%E6%AF%92-%E5%9C%B0%E7%8B%B1%E5%8F%98-%E5%9C%A3%E5%A9%B4-%E7%88%B1%E4%BA%BA%E7%9A%84%E5%A4%B4%E9%A2%85-%E8%BF%B7%E5%9F%8E%E7%AD%897%E5%86%8C-%E6%82%AC%E7%96%91%E6%95%99%E7%88%B6%E8%94%A1%E9%AA%8F%E4%BD%9C%E5%93%81-%E6%82%AC%E7%96%91%E5%B0%8F%E8%AF%B4-%E4%BE%A6%E6%8E%A2%E6%8E%A8%E7%90%86%E5%B0%8F%E8%AF%B4-%E8%94%A1%E9%AA%8F/dp/B01CV52COS/ref=sr_1_362_twi_pap_1?s=books&ie=UTF8&qid=1479055556&sr=1-362-spons&psc=1";
        String actual = UrlUtil.extractParamUrl(url, "url");
        System.out.println(url.length() + ", " + actual.length());
        Assert.assertEquals(expected, actual);
    }

    @Ignore
    public void testBookInfo() {
        String url = "https://www.amazon.cn/s/ref=lp_658495051_il_ti_stripbooks?rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051%2Cn%3A658495051&ie=UTF8&qid=1509759353&lo=stripbooks";
        HttpClientVM clientVM = new HttpClientVM();
        String response = clientVM.get(url);
        System.out.println(response);
    }
}
