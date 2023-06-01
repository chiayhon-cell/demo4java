package cn.chiayhon.demo;

import cn.chiayhon.spring.aop.declareparents.StorageProvider;
import cn.chiayhon.spring.aop.declareparents.extend.ExtendStorageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DeclareParentsDemo {

    @Autowired
    private List<StorageProvider> providers;
    @Autowired
    private List<ExtendStorageProvider> extendStorageProviders;

    @EventListener(ApplicationReadyEvent.class)
    public void storageProviderTest() {
        for (StorageProvider provider : providers) {
            String path = provider.upload(new byte[0]);
            provider.download(path);

            Optional<ExtendStorageProvider> opt = ExtendStorageProvider.convertToExtend(provider);
            opt.ifPresent(extendStorageProvider -> {
                boolean exist = extendStorageProvider.exist(path);
                if (exist) {
                    extendStorageProvider.delete(path);
                }
            });

            mytest();
        }
    }

    public void mytest() {

    }

}
