package com.example.myutils.utils;

import java.util.List;

/**
 * Html工具类
 * <p>
 * url ： http://news-at.zhihu.com/api/4/news/9697972
 * <p>
 * json：{"body":"<div class=\"main-wrap content-wrap\">\n<div class=\"headline\">\n\n<div class=\"img-place-holder\"><\/div>\n\n\n\n<\/div>\n\n<div class=\"content-inner\">\n\n\n\n\n<div class=\"question\">\n<h2 class=\"question-title\">如果华农兄弟养的是皮卡丘会怎么样?<\/h2>\n\n<div class=\"answer\">\n\n<div class=\"meta\">\n<img class=\"avatar\" src=\"http:\/\/pic1.zhimg.com\/v2-c309bdea697acd9942d2aa2e206dc9f0_is.jpg\">\n<span class=\"author\">Dictator Lin，<\/span><span class=\"bio\">you mean i am dictatorship.<\/span>\n<\/div>\n\n<div class=\"content\">\n<p>大家好，我们是华农兄弟，今天我们来看看皮卡丘长得怎么样了。<\/p>\r\n<p>这只皮卡丘好漂亮哦，毛色很有光泽，估计有 8 斤重了。再看看这只，也很漂亮的，我们这有两种不同的皮卡丘，一种是普通的，还有一种阿罗拉皮卡丘，它们最终进化不同哦。这只普通的雷丘能长到 60 斤，这只阿罗拉的就只能长到 42 斤。<\/p>\r\n<p>这只异色皮卡丘好漂亮的，很少见的，可以当宠物，我们把它放出来放放风。这种颜色的比一般皮卡丘能多一倍的价格的。<\/p>\r\n<p><img class=\"content-image\" src=\"http:\/\/pic4.zhimg.com\/70\/v2-1a31145e4da12aa5695d01258a12c403_b.jpg\" alt=\"\"><\/p>\r\n<p>养皮卡丘时要注意不要碰到它脸上的电气袋，大家看一下，滋滋的响，能电死非洲象（雷丘），被电一下估计是治不好了。也不要拉它的尾巴，它会咬人的，看它的牙好凶的，咬一口指头都会咬断的。<\/p>\r\n<p>它们很好养活的，平时就喂它们树果就可以了，它们可以自己电击树果让它更松软，也有些喜欢吃定制的食物，但那太贵了，都养不起了。<\/p>\r\n<p>养的时候注意，它们很喜欢电的，会啃电线，好危险的，如果惹到它们会一起放电引起雷暴，很凶的，所以每周都要定时给它们放放电。<\/p>\r\n<p>这只皮卡丘吸收太多电了，软绵绵的，估计是救不活了，这种的最适合烤来吃了。<\/p>\r\n<p>我们这里公的皮卡丘不多了，去兄弟家借几只回来配种。哦，这只毛色很亮，至少是 3v 的了，兄弟给我的都是很好的种皮，我们放进火箭队出的防电笼里带回去。<\/p>\r\n<p>100w 粉丝了，如果我女装就太难看了，就给大家看看这只贵妇皮卡丘吧！很漂亮的！这只还可以学习冰柱坠击，非常稀有的。<\/p>\r\n<p><img class=\"content-image\" src=\"http:\/\/pic1.zhimg.com\/70\/v2-7294399eb20f9481170bec47e9623130_b.jpg\" alt=\"\"><\/p>\n<\/div>\n<\/div>\n\n\n<div class=\"view-more\"><a href=\"http:\/\/www.zhihu.com\/question\/296772621\">查看知乎讨论<span class=\"js-question-holder\"><\/span><\/a><\/div>\n\n<\/div>\n\n\n<\/div>\n<\/div><script type=“text\/javascript”>window.daily=true<\/script>","image_source":"《宠物小精灵》","title":"大误 · 皮卡丘惨遇华农兄弟","image":"https:\/\/pic3.zhimg.com\/v2-e5e85087495d5674e5ddf942e35b16e2.jpg","share_url":"http:\/\/daily.zhihu.com\/story\/9697972","js":[],"id":9697972,"ga_prefix":"100812","images":["https:\/\/pic3.zhimg.com\/v2-495997f9c5b235e961b3e76df07c8aae.jpg"],"type":0,"section":{"thumbnail":"https:\/\/pic3.zhimg.com\/v2-495997f9c5b235e961b3e76df07c8aae.jpg","name":"大误","id":29},"css":["http:\/\/news-at.zhihu.com\/css\/news_qa.auto.css?v=4b3e3"]}
 * <p> 用法  Bean使用GsonFormat转化json的出来的
 * Bean bean = new Gson().fromJson(json,Bean.class);
 * String htmlData = HtmlUtils.createHtmlData(bean.getBody(), bean.getCss(), bean.getJs());
 * webView.loadData(htmlData, HtmlUtils.MIME_TYPE, HtmlUtils.ENCODING);
 */
public class HtmlUtils {

    //css样式,隐藏header
    private static final String HIDE_HEADER_STYLE = "<style>div.headline{display:none;}</style>";

    //css style tag,需要格式化
    private static final String NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" " +
            "type=\"text/css\" href=\"%s\"/>";

    // js script tag,需要格式化
    private static final String NEEDED_FORMAT_JS_TAG = "<script src=\"%s\"></script>";

    public static final String MIME_TYPE = "text/html; charset=utf-8";

    public static final String ENCODING = "utf-8";

    private HtmlUtils() {

    }

    /**
     * 根据css链接生成Link标签
     *
     * @param url String
     * @return String
     */
    public static String createCssTag(String url) {

        return String.format(NEEDED_FORMAT_CSS_TAG, url);
    }

    /**
     * 根据多个css链接生成Link标签
     *
     * @param urls List<String>
     * @return String
     */
    public static String createCssTag(List<String> urls) {

        final StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(createCssTag(url));
        }
        return sb.toString();
    }

    /**
     * 根据js链接生成Script标签
     *
     * @param url String
     * @return String
     */
    public static String createJsTag(String url) {

        return String.format(NEEDED_FORMAT_JS_TAG, url);
    }

    /**
     * 根据多个js链接生成Script标签
     *
     * @param urls List<String>
     * @return String
     */
    public static String createJsTag(List<String> urls) {

        final StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(createJsTag(url));
        }
        return sb.toString();
    }

    /**
     * 根据样式标签,html字符串,js标签
     * 生成完整的HTML文档
     */

    public static String createHtmlData(String html, List<String> cssList, List<String> jsList) {
        final String css = HtmlUtils.createCssTag(cssList);
        final String js = HtmlUtils.createJsTag(jsList);
        return css.concat(HIDE_HEADER_STYLE).concat(html).concat(js);
    }
}

