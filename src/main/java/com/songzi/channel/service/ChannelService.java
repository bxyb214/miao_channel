package com.songzi.channel.service;

import com.songzi.channel.domain.Channel;
import com.songzi.channel.domain.User;
import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.repository.UserRepository;
import com.songzi.channel.security.SecurityUtils;
import com.songzi.channel.service.dto.UserDTO;
import com.songzi.channel.web.rest.errors.LoginAlreadyUsedException;
import com.songzi.channel.web.rest.vm.ChannelVM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class ChannelService {

    private UserRepository userRepository;

    private ChannelRepository channelRepository;

    private UserService userService;

    private StatisticsService statisticsService;

    public ChannelService(UserRepository userRepository,
                          ChannelRepository channelRepository,
                          UserService userService,
                          StatisticsService statisticsService) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.userService = userService;
        this.statisticsService = statisticsService;
    }



    public Channel findOneBylogin(){
         String login =  SecurityUtils.getCurrentUserLogin().get();
         User user = userRepository.findOneByLogin(login).get();
         return channelRepository.findOneByUserId(user.getId());
    }

    public Channel save(ChannelVM channelVM) {
        if(userRepository.findOneByLogin(channelVM.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(channelVM.getLogin());
        User user = userService.registerUser(userDTO, channelVM.getPassword());
        user.setActivated(true);
        user = userRepository.saveAndFlush(user);
        channelVM.getChannel().setUserId(user.getId());

        Channel c =  channelRepository.saveAndFlush(channelVM.getChannel());

        statisticsService.createChannelStatistics(c);
        return c;
    }

    public Channel update(ChannelVM channelVM) {

        Channel channel = channelRepository.getOne(channelVM.getChannel().getId());
        channelVM.getChannel().setUserId(channel.getUserId());
        channelRepository.saveAndFlush(channelVM.getChannel());

        userService.updateLoginAndPassword(channel.getUserId(), channelVM.getLogin(), channelVM.getPassword());

        return channel;
    }

    public ChannelVM getChannel(Long id){

        Channel channel = channelRepository.findOne(id);
        ChannelVM channelVM = null;

        if (channel != null){
            channelVM = new ChannelVM();
            channelVM.setChannel(channel);
            String login = userRepository.findOne(channel.getUserId()).getLogin();
            channelVM.setLogin(login);
        }
        return channelVM;
    }

    public void delete(Long id) {
        Channel channel = channelRepository.getOne(id);
        userRepository.delete(channel.getUserId());
        channelRepository.delete(id);
    }
}
