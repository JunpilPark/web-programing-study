import com.study.webserver.util.CustomUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomUtilTest {

    @Test
    public void getUriTest_InputStandardParmeter() {
        String uri = CustomUtil.getUri("GET / HTTP/1.1");
        Assert.assertEquals("/", uri);

        uri = CustomUtil.getUri("GET /index.html HTTP/1.1");
        Assert.assertEquals("/index.html", uri);
    }

    @Test(expected = NullPointerException.class)
    public void getUriTest_InputNullOrEmptyParmeter() {
        CustomUtil.getUri(" ");
        CustomUtil.getUri("");
        CustomUtil.getUri(null);
    }



}
