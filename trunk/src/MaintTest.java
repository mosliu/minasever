import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MaintTest {
    /** The logger. */
    static Logger logger = Logger.getLogger(MaintTest.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
	new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    // public void initializeViaSpring() throws Exception {
    // new ClassPathXmlApplicationContext("trapReceiverContext.xml");
    // }

    /**
     * ����Log4Jģ��.
     */
    public static void loadLog4j() {
	PropertyConfigurator.configure("./bin/conf/log4j.properties");
	// DOMConfigurator.configure("E:/study/log4j/log4j.xml"); //����.xml�ļ�
	// File f = new File("./");
	// System.out.println(f.getAbsolutePath());
	logger.info("����Log4J");
    }

    /** Spring Bean Factory. */
    private static XmlBeanFactory beanFactory;

    /**
     * Load spring bean factory. ����Spring����
     */
    public static void loadSpringBeanFactory() {
	// InputStream is = new
	// FileInputStream("E:\\proj\\applicationContext.xml");
	// Resource resource = new InputStreamResource(is);
	// XmlBeanFactory bean = new XmlBeanFactory(resource);
	try {
	    Resource resource = new ClassPathResource(
		    "./conf/applicationContext.xml");
	    beanFactory = new XmlBeanFactory(resource);
	} catch (Exception e) {
	    logger.error("Spring�����ļ�����ʧ�ܣ�" + e.getMessage());
	}
    }

    /**
     * Gets the bean factory.
     * 
     * @return the bean factory
     */
    public static XmlBeanFactory getBeanFactory() {
	if (beanFactory == null) {
	    loadSpringBeanFactory();
	}
	return beanFactory;
    }
}
