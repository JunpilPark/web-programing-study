import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringCalcuratorTest {

    StringCalcurator stringCalcurator;

    @Before
    public void StringCalcuratorTestBefore() {
        stringCalcurator =  new StringCalcurator();
    }

    @Test
    public void inputNullCalcurateTest() {
        int result = stringCalcurator.calculate(null);
        Assert.assertEquals(0, result);
    }

    @Test
    public void inputBlankCalcurateTest() {
        int result = stringCalcurator.calculate(" ");
        Assert.assertEquals(0, result);
    }

    @Test
    public void inputEmptyCalcurateTest() {
        int result = stringCalcurator.calculate("");
        Assert.assertEquals(0, result);
    }


    @Test
    public void inputOneNumberCalcurateTest() {
        int result = stringCalcurator.calculate("5");
        Assert.assertEquals(5, result);
    }

    @Test
    public void inputCommaCalcurateTest() {
        int result =  stringCalcurator.calculate("1,2,3");
        Assert.assertEquals(6, result);
    }

    @Test
    public void inputCloneCalcurateTest() {
        int result =  stringCalcurator.calculate("1:2:3");
        Assert.assertEquals(6, result);
    }

    @Test
    public void inputCommaAndCloneCalcurateTest() {
        int result =  stringCalcurator.calculate("1,2:3");
        Assert.assertEquals(6, result);
    }

    @Test
    public void inputExceptionCalcurateTest() {
        int result =  stringCalcurator.calculate(",2:3");
        Assert.assertEquals(5, result);
    }

    @Test
    public void inputCustomSeperatorCalcurateTest() {
        int result =  stringCalcurator.calculate("//;\n1;2;3");
        Assert.assertEquals(6, result);
    }

    @Test
    public void inputCustomSeperatorCalcurateTest2() {
        int result =  stringCalcurator.calculate("//.\n1.2.3");
        Assert.assertEquals(6, result);
    }


    @Test(expected = RuntimeException.class)
    public void inputMinusNumbersCalcurateTest() {
        int result = stringCalcurator.calculate("-1:2:3");
    }
}
