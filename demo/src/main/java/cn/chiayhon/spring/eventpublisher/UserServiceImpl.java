package cn.chiayhon.spring.eventpublisher;

import cn.chiayhon.pojo.Company;
import cn.chiayhon.pojo.User;
import cn.chiayhon.spring.eventpublisher.dto.MessageData;
import cn.chiayhon.spring.eventpublisher.dto.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MessageServiceProvider provider;

    @Override
    public boolean register(User user) {
        // 参数校验
        final User exist = this.getByUsername(user.getUsername());
        if (exist != null) {
            return false;
        } else {
            final Company company = new Company();
            company.setName("阿里巴巴");
            user.setCompany(company);
        }
        // 通过则发注册成功短信通知
        final MessageData messageData = new MessageData();
        messageData.setType(MessageType.REGISTER_SUCCESS);
        messageData.setUser(user);
        // 异步发送
        provider.sendMessage(messageData);
        return true;
    }

    @Override
    public User getByUsername(String username) {
        if (username.equals("admin")) {
            final User user = new User();
            user.setUsername("admin");
            user.setPassword("1234");
            return user;
        }
        return null;
    }

}
