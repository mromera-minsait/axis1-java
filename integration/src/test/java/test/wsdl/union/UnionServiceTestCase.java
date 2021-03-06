package test.wsdl.union;

import java.util.Date;
import java.util.Calendar;

import org.apache.axis.types.URI;
import org.apache.axis.encoding.ser.CalendarSerializer;

import junit.framework.TestCase;

public class UnionServiceTestCase extends TestCase {

    public UnionServiceTestCase(String name) {
        super(name);
    }

    public void testBasicUnion1() throws Exception {
        // make sure WSDL2Java generated the right stuff
        
        // we don't really need to test sending a request
        //   and getting a response, since many other tests comprehend that
        // all we need to do is make sure WSDL2Java generated the right beans
        // so, basically, if this compiles, we are good to go
        // but running it won't hurt anything

        FooOpenEnum e = null;

        String testStrURI = "http://foobar/A";
        e = new FooOpenEnum(testStrURI);
        assertEquals(testStrURI, e.toString());
        
        URI testURI = new URI(testStrURI);
        e = new FooOpenEnum(testURI);
        assertEquals(testStrURI, e.toString());
        assertEquals(testURI, e.getAnyURIValue());
        
        e = new FooOpenEnum(FooEnum.value1);
        assertEquals(FooEnum.value1.toString(), e.toString());
        assertEquals(FooEnum.value1, e.getFooEnumValue());
        assertEquals(FooEnum._value1, e.getAnyURIValue());
    }

    public void testBasicUnion2() throws Exception {
        // make sure WSDL2Java generated the right stuff
        
        // we don't really need to test sending a request
        //   and getting a response, since many other tests comprehend that
        // all we need to do is make sure WSDL2Java generated the right beans
        // so, basically, if this compiles, we are good to go
        // but running it won't hurt anything

        DateOrDateTimeType type = null;

        type = new DateOrDateTimeType(5);
        assertEquals("5", type.toString());

        type = new DateOrDateTimeType(false);
        assertEquals("false", type.toString());
        
        Date date = new Date();
        type = new DateOrDateTimeType(date);
        assertEquals(date.toString(), type.toString());

        Calendar cal = Calendar.getInstance();
        type = new DateOrDateTimeType(cal);

        CalendarSerializer cs = new CalendarSerializer();
        assertEquals(cs.getValueAsString(cal, null), type.toString());
    }

}
