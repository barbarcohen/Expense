package cz.rudypokorny.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by Rudolf on 02/03/2018.
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static File readFile(final String fileName) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        logger.info("Reading file: " + fileName);
        return new File(classLoader.getResource(fileName).getFile());
    }
}
