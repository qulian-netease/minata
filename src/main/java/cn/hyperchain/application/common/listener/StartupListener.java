package cn.hyperchain.application.common.listener;

import cn.hyperchain.application.common.utils.ContractUtils;
import cn.hyperchain.application.common.utils.Logger;
import cn.hyperchain.application.common.utils.PropertiesUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Properties;


/**
 * 在启动时根据配置是否需要重新部署合约
 * @author sunligang
 */
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = Logger.Builder.getLogger(StartupListener.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event){
        //读取参数
        Properties properties = PropertiesUtils.getInstance("hyperchain.properties");
        boolean isReDploy= "true".equals(properties.getProperty("redeploy", "true"));
        if(isReDploy){
            logger.info("重新部署合约");
            ContractUtils.compileAndDeploy(properties.getProperty("access_token"));
        }else {
            logger.info("不重新部署合约");
        }
    }
}
