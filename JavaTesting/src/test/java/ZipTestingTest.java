import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZipTestingTest {
    @Test
    void testNormalMethod() {
        ZipTesting zipTesting = new ZipTesting();
        List<String> unzip = zipTesting.unzip(getStream("test_auto.zip"));
        assertEquals(20000, unzip.size());
    }

    @Test
    void testThreadedMethod() {
        ZipTesting zipTesting = new ZipTesting();
        List<String> unzip = zipTesting.unzipThreaded(
                getStream("test_auto.zip"),
                300);
        assertEquals(20000, unzip.size());
    }

    private InputStream getStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }
}