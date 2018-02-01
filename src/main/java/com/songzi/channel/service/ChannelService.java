package com.songzi.channel.service;

import com.songzi.channel.domain.Channel;
import com.songzi.channel.domain.User;
import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.repository.UserRepository;
import com.songzi.channel.security.SecurityUtils;
import com.songzi.channel.service.dto.UserDTO;
import com.songzi.channel.web.rest.vm.ChannelVM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChannelService {

    private UserRepository userRepository;

    private ChannelRepository channelRepository;

    private UserService userService;

    public ChannelService(UserRepository userRepository, ChannelRepository channelRepository, UserService userService) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.userService = userService;
    }



    public Channel findOneBylogin(){
         String login =  SecurityUtils.getCurrentUserLogin().get();
         User user = userRepository.findOneByLogin(login).get();
         return channelRepository.findOneByUserId(user.getId());
    }

    public Channel save(ChannelVM channelVM) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(channelVM.getLogin());
        User user = userService.registerUser(userDTO, channelVM.getPassword());
        user.setActivated(true);
        userRepository.saveAndFlush(user);

        return channelRepository.saveAndFlush(channelVM.getChannel());
    }

    public Channel update(ChannelVM channelVM) {

        Channel channel = channelRepository.saveAndFlush(channelVM.getChannel());

        userService.updateLoginAndPassword(channel.getUserId(), channelVM.getLogin(), channelVM.getPassword());

        return channel;
    }
}
