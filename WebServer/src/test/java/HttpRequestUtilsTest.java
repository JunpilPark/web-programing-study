import com.study.webserver.util.HttpRequestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpRequestUtilsTest {

    @Test
    public void getUriTest_InputStandardParmeter() {
        String uri = HttpRequestUtils.getUri("GET / HTTP/1.1");
        Assert.assertEquals("/", uri);

        uri = HttpRequestUtils.getUri("GET /index.html HTTP/1.1");
        Assert.assertEquals("/index.html", uri);
    }

    @Test(expected = NullPointerException.class)
    public void getUriTest_InputNullOrEmptyParmeter() {
        HttpRequestUtils.getUri(" ");
        HttpRequestUtils.getUri("");
        HttpRequestUtils.getUri(null);
    }

    @Test
    public void getQueryStringTest() {
        Map<String, String> values = HttpRequestUtils.getQueryString("/user/create?userId=a&password=&name=a&email=a%40gmail.com ");
        Assert.assertEquals("a",values.get("userId"));
        Assert.assertEquals("",values.get("password"));
        Assert.assertEquals("a",values.get("name"));
        Assert.assertEquals("a%40gmail.com",values.get("email"));
    }

    @Test
    public void getRequestPathHasParmetersTest() {
        String path = HttpRequestUtils.getRequestPath("/user/create?userId=a&password=&name=a&email=a%40gmail.com ");
        Assert.assertEquals("/user/create", path);
    }

    @Test
    public void getRequestPathNoParameterTest() {
        String uri = HttpRequestUtils.getUri("GET /index.html HTTP/1.1");
        String path = HttpRequestUtils.getRequestPath(uri);
        Assert.assertEquals("/index.html", path);
    }

    @Test
    public void isFileTest() {
        String uri = HttpRequestUtils.getUri("GET /index.html HTTP/1.1");
        String path = HttpRequestUtils.getRequestPath(uri);

        Assert.assertEquals(true, Pattern.matches("\\S*\\.\\S*", path));

        path = HttpRequestUtils.getRequestPath("/user/create?userId=a&password=&name=a&email=a%40gmail.com ");
        Assert.assertEquals(false, Pattern.matches("\\S*\\.\\S*", path));


    }
}
