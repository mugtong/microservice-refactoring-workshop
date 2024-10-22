package app.anne.channel.application.commandservices;

import app.anne.channel.controller.dto.CreateChannelResource;
import app.anne.channel.controller.dto.CreateMemberResource;
import app.anne.channel.domain.model.Channel;
import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.domain.model.ChannelType;
import app.anne.channel.domain.model.Member;
import app.anne.channel.domain.model.MemberId;
import app.anne.channel.domain.repository.ChannelRepository;
import app.anne.channel.infrastructure.ChannelRecord;
import app.anne.channel.infrastructure.MemberRecord;
import app.anne.channel.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelCommandService {
    private final ChannelRepository channelRepository;
    private final MemberRepository memberRepository;

    public ChannelRecord createChannel(CreateChannelResource dto) {
        ChannelId channelId;
        if (dto.getType() == ChannelType.PUBLIC) {
            channelId = new ChannelId(dto.getIsbn13());
        } else {
            Ulid ulid = UlidCreator.getUlid();
            channelId = new ChannelId(ulid.toString());
        }
        
        Channel channel = Channel.builderForCreateNew(channelId)
            .type(dto.getType())
            .name(dto.getName())
            .description(dto.getDescription())
            .imageUrl(dto.getImageUrl())
            .build();

        return channelRepository.createChannel(channel);
    }

    // public Channel updateChannel(String channelId, Channel newChannel) {
    //     Channel oldChannel = channelRepository.findOne(String.format("%s#%s", prefix, channelId), partitionKey);

    //     newChannel.setPk(oldChannel.getPk());
    //     newChannel.setSk(oldChannel.getSk());
    //     // newChannel.setCreatedDate(oldChannel.getCreatedDate());

    //     channelRepository.save(newChannel);

    //     return newChannel;
    // }

    public void deleteChannel(ChannelId channelId) {
        channelRepository.removeItem(channelId);
    }

    public MemberRecord joinChannel(ChannelId channelId, CreateMemberResource dto) {
        MemberId memberId = new MemberId(dto.getMemberId());
        MemberRecord record = memberRepository.findMember(channelId, memberId);

        if (record != null) {
            return record;
        }

        Member member = Member.builderForCreateNew(channelId)
            .member(memberId)
            .name(dto.getName())
            .imageUrl(dto.getImageUrl())
            .build();

        return memberRepository.createMember(member);
    }
}
