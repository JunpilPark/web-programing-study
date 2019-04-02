import com.study.webserver.JoinService;
import com.study.webserver.model.User;
import org.junit.Assert;
import org.junit.Test;

public class JoinServiceTest {

    @Test
    public void joinTest() {
        User user = new User("test1","test2","test3", "a@gmail.com");
        JoinService joinService = new JoinService();
        Assert.assertEquals(0, joinService.join(user));
    }

    @Test
    public void joinErrorTest() {
        User user = new User("test","test","", "a@gmail.com");
        JoinService joinService = new JoinService();
        Assert.assertEquals(-1, joinService.join(user));
    }
}
